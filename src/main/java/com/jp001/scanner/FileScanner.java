package com.jp001.scanner;

import com.jp001.utils.LoggerUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileScanner {

    // Define allowed extensions (you can customize this list)
    private static final String[] ALLOWED_EXTENSIONS = { "exe", "msi", "apk", "jar", "bat", "sh" };

    /**
     * Scans the specified directories recursively and returns a list of valid application files.
     *
     * @param directoryPaths List of root directories to scan
     * @return List of application files
     */
    public static List<File> scanDirectories(List<String> directoryPaths) {
        List<File> applicationFiles = new ArrayList<>();

        for (String dirPath : directoryPaths) {
            File root = new File(dirPath);

            if (!root.exists() || !root.isDirectory()) {
                LoggerUtil.logWarning("Skipping invalid directory: " + dirPath);
                continue;
            }

            LoggerUtil.logInfo("Scanning directory: " + root.getAbsolutePath());

            Collection<File> files = FileUtils.listFiles(
                    root,
                    ALLOWED_EXTENSIONS,
                    true // recursive
            );

            applicationFiles.addAll(files);
        }

        return applicationFiles;
    }
}