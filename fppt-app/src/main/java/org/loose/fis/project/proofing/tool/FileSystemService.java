package org.loose.fis.project.proofing.tool;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemService {
    private static final String APPLICATION_FOLDER = ".fppt";
    private static final String USER_FOLDER = System.getProperty("user.home");
    public static final Path APPLICATION_HOME_PATH = Paths.get(USER_FOLDER, APPLICATION_FOLDER);

    public static Path getPathToFile(String... path) {
        return APPLICATION_HOME_PATH.resolve(Paths.get(".", path));
    }

    public static Path getPathToStudentFile(StudentResponse studentResponse, String... path) {
        return getStudentPath(studentResponse).resolve(Paths.get(".", path));
    }

    public static Path getStudentPath(StudentResponse studentResponse) {
        return APPLICATION_HOME_PATH.resolve(Paths.get("repos", studentResponse.getAllNames()));
    }

}
