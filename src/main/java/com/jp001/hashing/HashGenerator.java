package com.jp001.hashing;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class HashGenerator {

    /**
     * Generates SHA-256 hashes for each application file and returns a list of ApplicationFile objects.
     *
     * @param files List of files to hash
     * @return List of ApplicationFile objects with hash and metadata
     */
    public static List<ApplicationFile> hashFiles(List<File> files) {
        List<ApplicationFile> hashedFiles = new ArrayList<>();

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file)) {
                String sha256 = DigestUtils.sha256Hex(fis);
                long size = FileUtils.sizeOf(file);

                ApplicationFile appFile = new ApplicationFile(file.getName(), file.getAbsolutePath(), sha256, size);
                hashedFiles.add(appFile);

                LoggerUtil.logDebug("Hashed: " + file.getName() + " -> " + sha256);
            } catch (Exception e) {
                LoggerUtil.logError("Failed to hash file: " + file.getAbsolutePath() + " | " + e.getMessage());
            }
        }

        return hashedFiles;
    }
}