package com.jp001.categorizer;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Categorizer {

    /**
     * Applies Drools rules to categorize application files and moves them into folders.
     *
     * @param files      List of ApplicationFile objects
     * @param rulesPath  Path to the .drl rules file
     */
    public static void applyCategorization(List<ApplicationFile> files, String rulesPath) {
        try {
            // Load Drools rules from .drl file
            String drl = Files.readString(Path.of(rulesPath));

            KieHelper kieHelper = new KieHelper();
            kieHelper.addContent(drl, Resource.ResourceType.DRL);

            KieSession kieSession = kieHelper.build().newKieSession();

            // Insert facts and fire rules
            for (ApplicationFile file : files) {
                kieSession.insert(file);
            }

            kieSession.fireAllRules();
            kieSession.dispose();

            // Move files to categorized folders
            for (ApplicationFile file : files) {
                String category = file.getCategory();
                if (category != null && !category.equalsIgnoreCase("Uncategorized")) {
                    Path sourcePath = Path.of(file.getPath());
                    Path targetDir = Path.of("categorized", category);
                    Files.createDirectories(targetDir);

                    Path targetPath = targetDir.resolve(sourcePath.getFileName());
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    LoggerUtil.logInfo("Moved to " + category + ": " + sourcePath.getFileName());
                }
            }

        } catch (Exception e) {
            LoggerUtil.logError("Failed to categorize applications: " + e.getMessage());
        }
    }
}