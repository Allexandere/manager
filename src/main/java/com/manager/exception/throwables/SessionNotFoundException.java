package com.manager.exception.throwables;


import com.manager.exception.HttpErrorCode;
import com.manager.exception.ManagerException;

@HttpErrorCode(404)
public class SessionNotFoundException extends ManagerException {
    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(Throwable cause) {
        super(cause);
    }

    public SessionNotFoundException() {
        super();
    }
}
