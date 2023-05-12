package com.manager.exception.throwables;


import com.manager.exception.HttpErrorCode;
import com.manager.exception.ManagerException;

@HttpErrorCode(400)
public class FolderAlreadyExistsException extends ManagerException {
    public FolderAlreadyExistsException(String message) {
        super(message);
    }

    public FolderAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public FolderAlreadyExistsException() {
        super();
    }
}
