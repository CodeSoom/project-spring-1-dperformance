package com.dyson.school.controllers;

import com.dyson.school.application.StudentService;
import com.dyson.school.domain.Student;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.dto.StudentUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto create(@RequestBody @Valid StudentCreateDto studentCreateDto) {
        return studentService.createStudent(studentCreateDto);
    }

    @PutMapping("{id}")
    public StudentResponseDto update(
            @PathVariable Long id,
            @RequestBody StudentUpdateDto studentUpdateDto
    ) {
        return studentService.updateStudent(id, studentUpdateDto);
    }


}
