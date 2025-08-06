package com.jp001.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp001.utils.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ConfigLoader {

    /**
     * Loads configuration from a JSON file.
     *
     * @param filePath Path to the config.json file
     * @return A map containing the config keys and values
     */
    public static Map<String, Object> loadConfig(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LoggerUtil.logError("Config file not found at path: " + filePath);
                throw new RuntimeException("Missing config file: " + filePath);
            }

            return mapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            LoggerUtil.logError("Failed to load config file: " + e.getMessage());
            throw new RuntimeException("Unable to parse config file: " + filePath, e);
        }
    }
}