package org.loose.fis.project.proofing.tool.actions;

import lombok.extern.slf4j.Slf4j;
import org.loose.fis.project.proofing.tool.GitClient;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;

import java.util.List;

@Slf4j
public class CloneAllReposCommand {

    public static void cloneAllRepos() {
        List<StudentResponse> students = StudentsRegistry.getInstance().getAllStudents();

        students.forEach(student -> {
            System.out.printf("Cloning for %s...........", student.getAllNames());
            try {
                GitClient.cloneRepositoryForStudent(student);
                System.out.println("SUCCESS");
            } catch (Exception e) {
                System.out.println("FAILED");
                log.error("Could not clone for student: " + student, e);
            }
        });
    }
}
