package org.loose.fis.project.proofing.tool.github.client;

public interface GithubClientAction<T> {
    T perform(String... args);
}
