package org.loose.fis.project.proofing.tool;

public interface Action<T> {
    T perform(String... args);
}
