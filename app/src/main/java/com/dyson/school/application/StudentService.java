package com.dyson.school.application;

import com.dyson.school.domain.Student;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.dto.StudentUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    public List<Student> getStudents() {
        return null;
    }

    public Student getStudent(Long id) {
        return null;
    }

    public StudentResponseDto createStudent(StudentCreateDto studentCreateDto) {
        return null;
    }

    public StudentResponseDto updateStudent(
            Long id,
            StudentUpdateDto studentUpdateDto
    ) {
        return null;
    }

    public Student deleteStudent(Long id) {
        return null;
    }
}
