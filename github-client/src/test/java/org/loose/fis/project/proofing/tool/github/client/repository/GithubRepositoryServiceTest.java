package org.loose.fis.project.proofing.tool.github.client.repository;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GithubRepositoryServiceTest {

    GithubRepositoryService service = new GithubRepositoryService("mario", "simple-test");

    @Test
    void testGetApiPathWithAdditionalVariables() {
        Map<String, String> map = ImmutableMap.of("name", "newSecret");

        assertEquals("https://api.github.com/repos/mario/simple-test/actions/secret/newSecret", service.getApiPath(map, "actions", "secret", ":name"));
    }

    @Test
    void testGetApiPath() {
        assertEquals("https://api.github.com/repos/mario/simple-test/actions/secret", service.getApiPath("actions", "secret"));
    }
}