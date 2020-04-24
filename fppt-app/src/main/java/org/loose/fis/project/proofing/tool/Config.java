package org.loose.fis.project.proofing.tool;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    public static final String FORM_NAME = "Form1";
    private static String STUDENT_RESPONSES = "studentResponses";
    private static String TEACHER_FIELD = "teacher";

    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key, defaultValue);
    }

    @SneakyThrows
    private static void loadProperties() {
        properties = new Properties();
        properties.load(new FileReader(Paths.get(".", "config", "fppt.properties").toFile()));
    }

    public static BasicAuthCredentials getGithubCredentials() {
        return new BasicAuthCredentials(getProperty("github.username"), getProperty("github.token"));
    }

    public static BasicAuthCredentials getJiraCredentials() {
        return new BasicAuthCredentials(getProperty("jira.username"), getProperty("jira.token"));
    }

    public static String getStudentResponsesFilePath() {
        return getProperty(STUDENT_RESPONSES, String.format("config/%s.json", STUDENT_RESPONSES));
    }

    public static String getTeacherName() {
        return getProperty(TEACHER_FIELD, "");
    }
}
