package org.loose.fis.project.proofing.tool;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.utils.JsonMapper;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentsRegistry {
    public static final File STUDENT_RESPONSE_FILE = Paths.get(".", Config.getStudentResponsesFilePath()).toFile();
    private static StudentsRegistry _instance = null;
    private final Map<Integer, StudentResponse> studentResponses;

    @SneakyThrows
    private StudentsRegistry() {

        JsonMapper jsonMapper = new JsonMapper();
        studentResponses = jsonMapper.readJSONfromFile(STUDENT_RESPONSE_FILE, StudentResponseSheet.class).getResponses().stream()
                .collect(Collectors.toMap(StudentResponse::getId, Function.identity()));
    }

    public static StudentsRegistry getInstance() {
        if (_instance == null)
            _instance = new StudentsRegistry();

        return _instance;
    }

    public StudentResponse getById(int id) {
        return studentResponses.get(id);
    }

    public List<StudentResponse> getForTeacher() {
        return studentResponses.values().stream()
                .filter(studentResponse -> studentResponse.getTeacher().toLowerCase().startsWith(Config.getTeacherName().toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<StudentResponse> getByGitRepo(String gitRepo) {
        List<StudentResponse> responsesWithThisRepo = getForTeacher().stream()
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

        StudentResponseSheet out = new StudentResponseSheet();
        out.setResponses(studentResponses.values().stream().collect(Collectors.toList()));

        jsonMapper.writePrettyJSON(new FileWriter(STUDENT_RESPONSE_FILE), out);
    }
}
