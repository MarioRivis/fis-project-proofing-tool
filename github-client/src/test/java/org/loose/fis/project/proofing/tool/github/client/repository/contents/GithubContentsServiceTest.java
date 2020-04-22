package org.loose.fis.project.proofing.tool.github.client.repository.contents;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents.CreateFileRequestBody;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubContentsServiceIT {

    private GithubContentsService contentsService = new GithubContentsService("gogmaster", "SimpleRegistrationExample", TestUtils.getGithubCredentials());

    @Test
    void addFileToRepo() throws IOException {

        String fileContent = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("javafx-maven-build.yml").getFile()), StandardCharsets.UTF_8.toString());


        CreateFileRequestBody body = CreateFileRequestBody.builder()
                .branch("actions")
                .content(Base64.getEncoder().encodeToString(fileContent.getBytes()))
                .message("Added Build.yml workflow to build the application on every push or pull request to Master branch")
                .build();

        assertTrue(contentsService.addFileToRepo(".github/workflows/Build.yml", body));
    }
}