package com.dyson.school.application;

import com.dyson.school.domain.Student;
import com.dyson.school.domain.StudentRepository;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.dto.StudentUpdateDto;
import com.dyson.school.errors.StudentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 학생과 관련된 비즈니스 로직을 담당합니다.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * 등록된 모든 학생 목록을 반환합니다.
     */
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return findStudent(id);
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

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
}
