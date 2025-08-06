// ✅ DuplicateRemover.java (optional refresh after deletion)
package com.jp001.remover;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DuplicateRemover {

    public static void promptAndRemove(Map<String, List<ApplicationFile>> duplicates) {
        Scanner scanner = new Scanner(System.in);

        for (Map.Entry<String, List<ApplicationFile>> entry : duplicates.entrySet()) {
            List<ApplicationFile> dupList = entry.getValue();
            System.out.println("\nDuplicate group (" + entry.getKey() + "):");
            for (int i = 0; i < dupList.size(); i++) {
                ApplicationFile file = dupList.get(i);
                System.out.printf("[%d] %s (%d bytes)%n", i + 1, file.getPath(), file.getSize());
            }

            System.out.print("Enter file numbers to delete (comma-separated), or press Enter to skip: ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                String[] tokens = input.split(",");
                for (String token : tokens) {
                    try {
                        int index = Integer.parseInt(token.trim()) - 1;
                        if (index >= 0 && index < dupList.size()) {
                            Path path = Path.of(dupList.get(index).getPath());
                            if (Files.exists(path)) {
                                Files.delete(path);
                                LoggerUtil.logInfo("Deleted: " + path);
                            } else {
                                LoggerUtil.logWarning("File already deleted or missing: " + path);
                            }
                        }
                    } catch (Exception e) {
                        LoggerUtil.logError("Invalid input or error deleting file: " + e.getMessage());
                    }
                }
            }
        }
    }
}
