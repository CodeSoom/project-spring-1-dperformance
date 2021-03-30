package com.dyson.school.controllers;

import com.dyson.school.application.StudentService;
import com.dyson.school.domain.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 학생과 관련된 HTTP 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> list() {
        return studentService.getStudents();
    }

    @GetMapping("/{id}")
    public Student detail(@PathVariable Long id) {
        return studentService.getStudent(id);
    }
}
