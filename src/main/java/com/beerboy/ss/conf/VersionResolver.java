package com.beerboy.ss.conf;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author manusant
 */
public class VersionResolver {

    public static synchronized String resolveVersion(final String groupId, final String artifactId) {
        String version = null;
        ClassLoader context = Thread.currentThread().getContextClassLoader();

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = context.getResourceAsStream("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = context.getClass().getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "";
        }

        return version;
    }
}
