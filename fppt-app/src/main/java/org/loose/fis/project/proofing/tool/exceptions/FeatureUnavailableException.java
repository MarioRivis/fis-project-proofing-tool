package org.loose.fis.project.proofing.tool.exceptions;

public class FeatureUnavailableException extends RuntimeException {
    public FeatureUnavailableException() {
        super("The feature you are trying to access is currently unavailable. Try using a newer version, or contact the app maintainers.");
    }
}
