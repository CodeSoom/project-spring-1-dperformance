package com.dyson.school.utils;

public enum Gender {
    MAN("남"), WOMEN("여");

    private final String genderCode;

    Gender(String genderCode) {
        this.genderCode = genderCode;
    }

    public String type() {
        return this.genderCode;
    }
}
