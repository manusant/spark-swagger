package io.github.manusant.ss.conf;

import io.github.manusant.ss.SparkSwagger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Options {
    private String confPath;
    private String version;
    private boolean enableStaticMapping;
    private boolean enableCors;

    public static Options.OptionsBuilder defaultOptions() {
        return Options.builder()
                .confPath(SparkSwagger.CONF_FILE_NAME)
                .enableCors(true)
                .enableStaticMapping(true);
    }
}
