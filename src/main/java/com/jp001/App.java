package com.jp001;

import com.jp001.config.ConfigLoader;
import com.jp001.hashing.HashGenerator;
import com.jp001.model.ApplicationFile;
import com.jp001.scanner.FileScanner;
import com.jp001.duplicate.DuplicateDetector;
import com.jp001.remover.DuplicateRemover;
import com.jp001.categorizer.Categorizer;
import com.jp001.utils.LoggerUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        LoggerUtil.logInfo("🔍 Starting Duplicate Application Manager...");

        try {
            // 1. Load configuration
            Map<String, Object> config = ConfigLoader.loadConfig("src/main/resources/config/config.json");
            List<String> directoriesToScan = (List<String>) config.get("scan_paths");
            String rulesFilePath = (String) config.get("rules_path");

            // 2. Scan for application files
            List<File> applicationFiles = FileScanner.scanDirectories(directoriesToScan);
            LoggerUtil.logInfo("📁 Scanned " + applicationFiles.size() + " files for analysis.");

            // 3. Generate content hashes
            List<ApplicationFile> appFileList = HashGenerator.hashFiles(applicationFiles);
            LoggerUtil.logInfo("🔐 Generated SHA-256 hashes for all files.");

            // 4. Detect duplicates
            Map<String, List<ApplicationFile>> duplicates = DuplicateDetector.findDuplicates(appFileList);
            LoggerUtil.printDuplicates(duplicates);

            // 5. Ask user which duplicates to remove
            DuplicateRemover.handleUserRemoval(duplicates);

            // 6. Categorize files using Drools
            Categorizer.applyCategorization(appFileList, rulesFilePath);
            LoggerUtil.logInfo("📦 Categorization complete. Files moved to category folders.");

            LoggerUtil.logInfo("✅ Application completed successfully.");

        } catch (Exception e) {
            LoggerUtil.logError("❌ An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}