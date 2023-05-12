package com.manager.exception;

public class ManagerException extends Exception {
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }

    public ManagerException() {
        super();
    }
}
