package com.jp001.remover;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DuplicateRemover {

    /**
     * Interactively prompts the user to choose which duplicates to remove.
     *
     * @param duplicates Map of hash -> list of duplicate ApplicationFile objects
     */
    public static void handleUserRemoval(Map<String, List<ApplicationFile>> duplicates) {
        Scanner scanner = new Scanner(System.in);

        for (Map.Entry<String, List<ApplicationFile>> entry : duplicates.entrySet()) {
            List<ApplicationFile> files = entry.getValue();

            System.out.println("\n🔁 Duplicate Group (Hash: " + entry.getKey() + ")");
            for (int i = 0; i < files.size(); i++) {
                System.out.println("  [" + i + "] " + files.get(i).getPath());
            }

            System.out.print("Enter indices of files to delete (comma-separated, or press Enter to skip): ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                String[] indices = input.split(",");
                for (String indexStr : indices) {
                    try {
                        int idx = Integer.parseInt(indexStr.trim());
                        if (idx >= 0 && idx < files.size()) {
                            File file = new File(files.get(idx).getPath());
                            if (Files.deleteIfExists(file.toPath())) {
                                LoggerUtil.logInfo("🗑 Deleted: " + file.getAbsolutePath());
                            } else {
                                LoggerUtil.logWarning("⚠️ Could not delete: " + file.getAbsolutePath());
                            }
                        }
                    } catch (Exception e) {
                        LoggerUtil.logError("Invalid index or deletion error: " + indexStr + " | " + e.getMessage());
                    }
                }
            } else {
                LoggerUtil.logInfo("Skipped deletion for this group.");
            }
        }

        scanner.close();
    }
}