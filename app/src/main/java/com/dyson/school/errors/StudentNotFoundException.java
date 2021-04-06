package com.dyson.school.errors;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found : " + id);
    }
}
