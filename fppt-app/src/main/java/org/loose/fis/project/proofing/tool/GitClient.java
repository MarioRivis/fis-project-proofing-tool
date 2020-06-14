package org.loose.fis.project.proofing.tool;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.loose.fis.project.proofing.tool.http.BasicAuthenticationProvider;

import java.io.File;
import java.util.Collections;

@Slf4j
public class GitClient {

    public static final String MASTER = "master";

    private static void initializeReposPath() {
        File repo_file_path = FileSystemService.APPLICATION_HOME_PATH.toFile();
        if (!repo_file_path.exists()) {
            repo_file_path.mkdirs();
        }
    }


    public static void cloneRepositoryForStudent(StudentResponse studentResponse) throws GitAPIException {
        String repoUrl = studentResponse.getRepoUrl();

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(FileSystemService.getPathToStudentFile(studentResponse, studentResponse.getRepo()).toFile())
                .setBranchesToClone(Collections.singletonList("refs/heads/master"))
                .setBranch(MASTER);

        BasicAuthenticationProvider githubCredentialsForTeacher = Config.getGithubCredentialsForTeacher(studentResponse.getTeacher());

        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                githubCredentialsForTeacher.getUsername(), githubCredentialsForTeacher.getPassword()));
        cloneCommand.call();
    }
}
