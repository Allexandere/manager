package com.manager.exception.throwables;


import com.manager.exception.HttpErrorCode;
import com.manager.exception.ManagerException;

@HttpErrorCode(404)
public class SnapshotNotFoundException extends ManagerException {
    public SnapshotNotFoundException(String message) {
        super(message);
    }

    public SnapshotNotFoundException(Throwable cause) {
        super(cause);
    }

    public SnapshotNotFoundException() {
        super();
    }
}
