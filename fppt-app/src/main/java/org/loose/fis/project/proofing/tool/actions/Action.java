package org.loose.fis.project.proofing.tool.actions;

public interface Action<T> {
    T perform(String... args);
}
