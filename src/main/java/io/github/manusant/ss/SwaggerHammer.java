package io.github.manusant.ss;

import com.typesafe.config.Config;
import io.github.manusant.ss.conf.Theme;
import io.github.manusant.ss.ui.UiTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author manusant
 */
public class SwaggerHammer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerHammer.class);

    public void prepareUi(final Config config, Swagger swagger) throws IOException {
        LOGGER.debug("Spark-Swagger: Start compiling Swagger UI");

        String uiFolder = SwaggerHammer.getUiFolder(config.getString("spark-swagger.basePath"));

        // 1 - Extract UI/Templates folder to a temporary folder
        extractUi(uiFolder);

        // 2 - Decorate index.html according to configurations
        String newIndex = decorateIndex(config);

        // 3 - Save new Index to UI folder
        saveFile(uiFolder, "index.html", newIndex);

        // 4 - Parse Swagger definitions and save it to UI folder
        SwaggerParser.parseJs(swagger, uiFolder + "swagger-spec.js");
        SwaggerParser.parseYaml(swagger, uiFolder + "doc.yaml");
        SwaggerParser.parseJson(swagger, uiFolder + "doc.json");

        // 5 - Apply theme according to configurations
        applyTheme(uiFolder, config);
    }

    private void extractUi(String uiFolder) throws IOException {
        extractUiFolder(uiFolder);
        extractTemplatesFolder(uiFolder);
        LOGGER.debug("Spark-Swagger: UI resources and templates successfully extracted");
    }

    private void extractUiFolder(String uiFolder) throws IOException {

        String dir = "ui";
        List<String> uiFiles = listFiles(dir)
                .stream()
                .map(filePath -> filePath.substring(filePath.indexOf(dir) + dir.length() + 1).trim())
                .filter(fileName -> !fileName.contains("/") && !fileName.isEmpty())
                .collect(Collectors.toList());

        for (String uiFileName : uiFiles) {
            InputStream uiFile = SparkSwagger.class.getClassLoader().getResourceAsStream(dir + "/" + uiFileName);
            File file = new File(uiFolder + uiFileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            if (uiFile != null) {
                Files.copy(uiFile, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void extractTemplatesFolder(String uiFolder) throws IOException {

        File templatesFolder = new File(uiFolder + "templates/");
        if (!templatesFolder.exists()) {
            templatesFolder.mkdir();
        }

        String dir = "ui/templates";
        List<String> templateFiles = listFiles(dir)
                .stream()
                .map(filePath -> filePath.substring(filePath.indexOf(dir) + dir.length() + 1).trim())
                .filter(fileName -> !fileName.contains("/") && !fileName.isEmpty())
                .collect(Collectors.toList());

        for (String templateFileName : templateFiles) {
            InputStream templateFile = SparkSwagger.class.getClassLoader().getResourceAsStream(dir + "/" + templateFileName);
            File file = new File(uiFolder + "templates/" + templateFileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            if (templateFile != null) {
                Files.copy(templateFile, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private List<String> listFiles(String prefix) throws IOException {
        List<String> uiFiles = new ArrayList<>();

        CodeSource src = SparkSwagger.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith(prefix)) {
                        uiFiles.add(name);
                    }
                }
            }
        }
        return uiFiles;
    }

    private void applyTheme(String uiFolder, final Config config) throws IOException {
        LOGGER.debug("Spark-Swagger: Start applying configured CSS Theme");
        String themeName = config.getString("spark-swagger.theme");
        Theme theme = Theme.fromValue(themeName);

        String themeCss = readFile(uiFolder, "templates/" + theme.getValue() + ".css", StandardCharsets.UTF_8);
        saveFile(uiFolder, "swagger-ui.css", themeCss);
        LOGGER.debug("Spark-Swagger: CSS Theme successfully applied");
    }

    private String readFile(String uiFolder, String name, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(uiFolder + name));
        return new String(encoded, encoding);
    }

    private void saveFile(String uiFolder, String fileName, String content) throws IOException {
        File file = new File(uiFolder + fileName);
        file.delete();

        FileWriter f2 = new FileWriter(file, false);
        f2.write(content);
        f2.close();
        LOGGER.debug("Spark-Swagger: Swagger UI file " + fileName + " successfully saved");
    }

    public static String getUiFolder(String basePath) {
        if (basePath.isEmpty()) return getSwaggerUiFolder();
        return System.getProperty("java.io.tmpdir") + "/swagger-ui" + (basePath.startsWith("/") ? "" : "/") + basePath + (basePath.endsWith("/") ? "" : "/");
    }

    public static String getSwaggerUiFolder() {
        return System.getProperty("java.io.tmpdir") + "/swagger-ui/";
    }

    public static void createDir(final String path) {
        File uiFolder = new File(path);
        if (!uiFolder.exists()) {
            uiFolder.mkdir();
        }
    }

    private String decorateIndex(final Config config) {
        LOGGER.debug("Spark-Swagger: Start decorating index.html according to ui configurations");
        String indexTemplate = UiTemplates.indexTemplate();
        int scriptStartIndex = indexTemplate.indexOf("window.onload");
        int scriptEndIndex = indexTemplate.indexOf("</script>", scriptStartIndex);
        String currentScript = indexTemplate.substring(scriptStartIndex, scriptEndIndex);

        String scriptTemplate = UiTemplates.scriptTemplate();
        scriptTemplate = setStringProperty(scriptTemplate, "docExpansion", config.getString("spark-swagger.docExpansion"), "list");
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "deepLinking", config.getString("spark-swagger.deepLinking"), false);
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "displayOperationId", config.getString("spark-swagger.displayOperationId"), false);
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "defaultModelsExpandDepth", config.getString("spark-swagger.defaultModelsExpandDepth"), 1);
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "defaultModelExpandDepth", config.getString("spark-swagger.defaultModelExpandDepth"), 1);
        scriptTemplate = setStringProperty(scriptTemplate, "defaultModelRendering", config.getString("spark-swagger.defaultModelRendering"), "example");
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "displayRequestDuration", config.getString("spark-swagger.displayRequestDuration"), false);
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "filter", config.getString("spark-swagger.filter"), false);
        scriptTemplate = setStringProperty(scriptTemplate, "operationsSorter", config.getString("spark-swagger.operationsSorter"), "alpha");
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "showExtensions", config.getString("spark-swagger.showExtensions"), false);
        scriptTemplate = setPrimitiveProperty(scriptTemplate, "showCommonExtensions", config.getString("spark-swagger.showCommonExtensions"), false);
        scriptTemplate = setStringProperty(scriptTemplate, "tagsSorter", config.getString("spark-swagger.tagsSorter"), "alpha");
        LOGGER.debug("Spark-Swagger: index.html successfully decorated");
        return indexTemplate.replace(currentScript, scriptTemplate);
    }

    private String setStringProperty(String scriptTemplate, String propertyName, String value, String defaultValue) {
        if (value != null) {
            scriptTemplate = replaceProperty(scriptTemplate, propertyName, value.toLowerCase(), true);
        } else {
            scriptTemplate = replaceProperty(scriptTemplate, propertyName, defaultValue, true);
        }
        return scriptTemplate;
    }

    private String setPrimitiveProperty(String scriptTemplate, String propertyName, Object value, Object defaultValue) {
        if (value != null) {
            scriptTemplate = replaceProperty(scriptTemplate, propertyName, value.toString(), false);
        } else {
            scriptTemplate = replaceProperty(scriptTemplate, propertyName, defaultValue.toString(), false);
        }
        return scriptTemplate;
    }

    private String replaceProperty(String script, String property, String newValue, boolean isString) {
        if (newValue == null) newValue = "null";
        if (property != null) {
            int propertyIndex = script.indexOf(property);

            if (propertyIndex != -1 && script.indexOf(":", propertyIndex) != -1) {
                String propertyValue = script.substring(script.indexOf(":", propertyIndex) + 1, script.indexOf(",", propertyIndex)).trim();

                if (isString && !newValue.equals("null")) {
                    if (newValue.isEmpty()) {
                        script = replace(script, property, propertyValue, "null");
                    } else {
                        script = replace(script, property, propertyValue, "'" + newValue + "'");
                    }
                } else {
                    script = replace(script, property, propertyValue, newValue);
                }
            }
        }
        return script;
    }

    private String replace(String script, String property, String oldValue, String newValue) {
        String oldConf = property + ": " + oldValue;
        String newConf = property + ": " + newValue;
        return script.replace(oldConf, newConf);
    }
}
