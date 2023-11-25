package com.simon.core.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Configurator {
    private static final String DEFAULT_REPORT_DIRECTOR = System.getProperty("user.dir") + "/report";
    private static final String DEFAULT_LOG_DIRECTOR = System.getProperty("user.dir") + "/log";

    private String backendUrl;
    private String frontendUrl;
    private static Configurator instance = null;
    private Path reportPath;
    private Path logPath;
    private Map<String, String> params;

    private Configurator() {
    }

    public static synchronized Configurator getInstance() {
        if (instance == null) {
            instance = new Configurator();
        }
        return instance;
    }

    public void initParameters(Map<String, String> params) {
        this.params = normalizeParameters(params);
        this.backendUrl = normalizeUrl(getParameter(ConfigKeys.BACKEND_URL));
        this.frontendUrl = normalizeUrl(getParameter(ConfigKeys.FRONTEND_URL));
        this.reportPath = Path.of(getParameterOrDefault(ConfigKeys.REPORT_LOCATION, DEFAULT_REPORT_DIRECTOR));
        this.logPath = Path.of(getParameterOrDefault(ConfigKeys.LOG_LOCATION, DEFAULT_LOG_DIRECTOR));
    }

    public String getParameterOrDefault(ConfigKeys key, String defaultValue) {
        return this.params.getOrDefault(key.name(), defaultValue);
    }

    public String getParameter(ConfigKeys key) {
        return this.params.get(key.name());
    }

    private Map<String, String> normalizeParameters(Map<String, String> params) {
        return params.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> entry.getKey().toUpperCase(),
                        Map.Entry::getValue
                )
        );
    }

    private String normalizeUrl(String url) {
        if (StringUtils.isEmpty(url)) return null;
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }
}
