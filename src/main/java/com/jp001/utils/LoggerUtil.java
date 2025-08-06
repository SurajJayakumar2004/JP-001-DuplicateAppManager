package com.jp001.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger("DuplicateAppManager");

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }
}