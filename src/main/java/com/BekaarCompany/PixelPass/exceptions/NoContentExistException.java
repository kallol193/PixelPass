package com.BekaarCompany.PixelPass.exceptions;

import com.BekaarCompany.PixelPass.enums.Messages;

public class NoContentExistException extends RuntimeException{
    public NoContentExistException(String s) {
        super(Messages.NO_CONTENT_EXIST.getErrorMessage());
    }
}
