package com.manager.exception.throwables;


import com.manager.exception.HttpErrorCode;
import com.manager.exception.ManagerException;

@HttpErrorCode(404)
public class OwnerNotFoundException extends ManagerException {
    public OwnerNotFoundException(String message) {
        super(message);
    }

    public OwnerNotFoundException(Throwable cause) {
        super(cause);
    }

    public OwnerNotFoundException() {
        super();
    }
}
