package com.BekaarCompany.PixelPass.enums;

public enum Messages {
    NO_CONTENT_EXIST("NO content exist");


    private  final String ErrorMessage;

    Messages(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}
