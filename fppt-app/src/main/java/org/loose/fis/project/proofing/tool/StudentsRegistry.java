package org.loose.fis.project.proofing.tool;

import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.loose.fis.project.proofing.tool.utils.JsonMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentsRegistry {
    public static final File STUDENT_RESPONSE_FILE = Paths.get(".", Config.getStudentResponsesFilePath()).toFile();
    private static StudentsRegistry _instance = null;
    private final Map<Integer, StudentResponse> studentResponses;

    private List<String> currentStudents;

    @SneakyThrows
    private StudentsRegistry() {

        JsonMapper jsonMapper = new JsonMapper();
        studentResponses = ((List<StudentResponse>) jsonMapper.readJSONfromFile(STUDENT_RESPONSE_FILE, new TypeToken<List<StudentResponse>>() {
        }.getType())).stream()
                .collect(Collectors.toMap(StudentResponse::getId, Function.identity()));
    }

    public static StudentsRegistry getInstance() {
        if (_instance == null)
            _instance = new StudentsRegistry();

        return _instance;
    }

    public List<StudentResponse> getForTeacher() {
        loadCurrentStudents();
        return getAllForTeacher().stream()
                .filter(this::filterCurrentStudentsIfAny)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getAllForTeacher() {
        return studentResponses.values().stream()
                .filter(studentResponse -> studentResponse.getTeacher().toLowerCase().startsWith(Config.getTeacherName().toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean filterCurrentStudentsIfAny(StudentResponse studentResponse) {
        if (currentStudents.isEmpty())
            return true;
        return currentStudents.contains(studentResponse.getName());
    }

    private void loadCurrentStudents() {
        Path currentStudentPath = Config.getCurrentStudentPath();
        if (!Files.exists(currentStudentPath))
            currentStudents = Collections.emptyList();
        try {
            currentStudents = Files.readAllLines(currentStudentPath).stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            currentStudents = Collections.emptyList();
        }
    }

    public Optional<StudentResponse> getByGitRepo(String gitRepo) {
        List<StudentResponse> responsesWithThisRepo = getAllForTeacher().stream()
                .filter(studentResponse -> studentResponse.getRepoUrl().trim().startsWith(gitRepo))
                .collect(Collectors.toList());

        if (responsesWithThisRepo.isEmpty())
            return Optional.empty();

        if (responsesWithThisRepo.size() != 1) {
            System.err.println("Following responses have the same repository: " + responsesWithThisRepo);
        }

        StudentResponse studentResponse = responsesWithThisRepo.get(0);
        if (!studentResponse.getRepoUrl().equalsIgnoreCase(gitRepo)) {
            studentResponse.setRepoUrl(gitRepo);
        }

        return Optional.ofNullable(studentResponse);
    }

    @SneakyThrows
    public void persist() {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.writePrettyJSON(new FileWriter(STUDENT_RESPONSE_FILE), studentResponses.values());
    }

    public void setCurrentStudents(List<String> currentStudents) {
        this.currentStudents = currentStudents;
    }
}
