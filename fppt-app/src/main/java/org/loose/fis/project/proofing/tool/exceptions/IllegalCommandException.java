package org.loose.fis.project.proofing.tool.exceptions;

import org.loose.fis.project.proofing.tool.actions.Command;

public class IllegalCommandException extends IllegalArgumentException {
    public static final String COMMAND_ERROR_STRING = "Please use only one of the following commands: " + Command.names();
    private String command;

    public IllegalCommandException(String command) {
        super(String.format("Command %s not found. %s", command, COMMAND_ERROR_STRING));
        this.command = command;
    }

    public IllegalCommandException() {
        super(COMMAND_ERROR_STRING);
    }
}
