// ✅ Categorizer.java (robust file moving + folder creation)
package com.jp001.categorizer;

import com.jp001.model.ApplicationFile;
import com.jp001.utils.LoggerUtil;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Categorizer {

    public static void applyCategorization(List<ApplicationFile> files, String rulesPath) {
        try {
            String drl = Files.readString(Path.of(rulesPath));

            KieHelper kieHelper = new KieHelper();
            kieHelper.addContent(drl, ResourceType.DRL);
            KieSession kieSession = kieHelper.build().newKieSession();

            for (ApplicationFile file : files) {
                kieSession.insert(file);
            }

            kieSession.fireAllRules();
            kieSession.dispose();

            Files.createDirectories(Path.of("categorized"));

            for (ApplicationFile file : files) {
                String category = file.getCategory();
                if (category != null && !category.equalsIgnoreCase("Uncategorized")) {
                    Path sourcePath = Path.of(file.getPath());
                    if (!Files.exists(sourcePath)) {
                        LoggerUtil.logWarning("File already moved or missing: " + sourcePath);
                        continue;
                    }

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
