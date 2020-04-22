package org.loose.fis.project.proofing.tool.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

public class FpptFileUtils {

    public static String replaceTemplatesInFile(Map<String, String> templateValues, Path filePath) throws IOException {
        String fileContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        return replaceTemplatesInFile(templateValues, fileContent);
    }

    public static String replaceTemplatesInFile(Map<String, String> templateValues, String fileContent) {
        for (Map.Entry<String, String> entry : templateValues.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String keyTemplate = String.format("%%{{%s}}", key);
            fileContent = fileContent.replace(keyTemplate, value);
        }

        return fileContent;
    }


    public static String encodeBase64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }
}
