package com.jp001.scanner;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileScanner {
    private static final String[] ALLOWED_EXTENSIONS = {"exe", "apk", "jar"};

    public static List<ApplicationFile> scanDirectories(List<String> paths) {
        List<ApplicationFile> applications = new ArrayList<>();

        for (String path : paths) {
            File root = new File(path);
            if (!root.exists() || !root.isDirectory()) {
                LoggerUtil.logWarning("Skipping invalid directory: " + path);
                continue;
            }

            try {
                Collection<File> files = FileUtils.listFiles(
                    root,
                    new SuffixFileFilter(ALLOWED_EXTENSIONS),
                    TrueFileFilter.INSTANCE
                );

                for (File file : files) {
                    try {
                        String hash = DigestUtils.sha256Hex(FileUtils.openInputStream(file));
                        applications.add(new ApplicationFile(
                            file.getName(),
                            file.getAbsolutePath(),
                            file.length(),
                            hash
                        ));
                    } catch (IOException e) {
                        LoggerUtil.logWarning("Could not hash file: " + file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                LoggerUtil.logError("Error scanning directory " + path + ": " + e.getMessage());
            }
        }
        return applications;
    }
}


// ✅ LoggerUtil.java (correct context logger)
package com.jp001.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

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