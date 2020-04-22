package org.loose.fis.project.proofing.tool.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FpptFileUtilsTest {

    @Test
    void replaceTemplatesInFileForMaven() throws IOException {
        String fileContent = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("BuildTemplate.yml").getFile()), StandardCharsets.UTF_8.toString());
        String expectedContent = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("BuildResultMaven.yml").getFile()), StandardCharsets.UTF_8.toString());

        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("buildCommand", "mvn clean install")
                .put("artifactPath", "target")
                .put("buildTool", "maven")
                .put("buildToolCachePath", "~/.m2/repository")
                .put("buildToolConfigFilePattern", "pom.xml")
                .build();

        String actualContent = FpptFileUtils.replaceTemplatesInFile(map, fileContent);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    void replaceTemplatesInFileForGradle() throws IOException {
        String fileContent = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("BuildTemplate.yml").getFile()), StandardCharsets.UTF_8.toString());
        String expectedContent = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("BuildResultGradle.yml").getFile()), StandardCharsets.UTF_8.toString());

        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("buildCommand", "gradle clean build")
                .put("artifactPath", "build/libs")
                .put("buildTool", "gradle")
                .put("buildToolCachePath", "~/.gradle/caches")
                .put("buildToolConfigFilePattern", "*.gradle*")
                .build();

        String actualContent = FpptFileUtils.replaceTemplatesInFile(map, fileContent);

        assertEquals(expectedContent, actualContent);
    }
}