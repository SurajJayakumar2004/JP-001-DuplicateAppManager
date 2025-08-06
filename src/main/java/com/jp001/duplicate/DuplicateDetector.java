package com.jp001.duplicate;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateDetector {

    /**
     * Groups ApplicationFile objects by their content hash to identify duplicates.
     *
     * @param files List of ApplicationFile objects
     * @return Map of hash -> list of duplicate files
     */
    public static Map<String, List<ApplicationFile>> findDuplicates(List<ApplicationFile> files) {
        Map<String, List<ApplicationFile>> hashToFileMap = new HashMap<>();

        for (ApplicationFile file : files) {
            String hash = file.getHash();
            hashToFileMap.putIfAbsent(hash, new ArrayList<>());
            hashToFileMap.get(hash).add(file);
        }

        // Filter out non-duplicates (only keep entries with > 1 file)
        Map<String, List<ApplicationFile>> duplicates = new HashMap<>();
        for (Map.Entry<String, List<ApplicationFile>> entry : hashToFileMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
                LoggerUtil.logInfo("Duplicate group detected (hash: " + entry.getKey() + "): " + entry.getValue().size() + " files");
            }
        }

        return duplicates;
    }
}