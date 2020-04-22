package org.loose.fis.project.proofing.tool.utils;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;

import java.util.Properties;

public class TestUtils {

    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        properties = new Properties();
        properties.load(TestUtils.class.getClassLoader().getResourceAsStream("fppt.properties"));
    }

    public static BasicAuthCredentials getGithubCredentials() {
        return new BasicAuthCredentials(getProperty("github.username"), getProperty("github.token"));
    }

    public static BasicAuthCredentials getJiraCredentials() {
        return new BasicAuthCredentials(getProperty("jira.username"), getProperty("jira.token"));
    }
}
