package com.dyson.school.application;

import com.dyson.school.domain.Student;
import com.dyson.school.domain.StudentRepository;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.errors.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StudentServiceTest {

    private StudentService studentService;
    private final StudentRepository studentRepository = mock(StudentRepository.class);

    private static final Long EXISTED_ID = 1L;
    private static final String SETUP_NAME = "코돌쓰";
    private static final String SETUP_GENDER = "남";
    private static final String SETUP_GROUP = "1학년1반";

    private static final Long CREATE_ID = 2L;
    private static final String CREATE_NAME = "해나쓰";
    private static final String CREATE_GENDER = "여";
    private static final String CREATE_GROUP = "담임";

    private static final Long NOT_EXISTED_ID = 1000L;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);

        Student student = Student.builder()
                .id(EXISTED_ID)
                .name(SETUP_NAME)
                .gender(SETUP_GENDER)
                .group(SETUP_GROUP)
                .build();

        given(studentRepository.findAll()).willReturn(List.of(student));

        given(studentRepository.findById(EXISTED_ID)).willReturn(Optional.of(student));

        given(studentRepository.save(any(Student.class))).will(invocation -> {
            Student source = invocation.getArgument(0);

            return Student.builder()
                    .id(CREATE_ID)
                    .name(source.getName())
                    .gender(source.getGender())
                    .group(source.getGroup())
                    .build();
        });
    }

    @Test
    @DisplayName("비어있는 학생목록을 확인합니다.")
    void getStudentsWithNoStudent() {
        given(studentRepository.findAll()).willReturn(List.of());

        assertThat(studentService.getStudents()).isEmpty();
    }

    @Test
    @DisplayName("모든 학생목록을 확인합니다.")
    void getStudents() {
        assertThat(studentService.getStudents()).isNotEmpty();

        List<Student> students = studentService.getStudents();

        Student student = students.get(0);

        assertThat(student.getId()).isEqualTo(EXISTED_ID);
        assertThat(student.getName()).isEqualTo(SETUP_NAME);
        assertThat(student.getGender()).isEqualTo(SETUP_GENDER);
        assertThat(student.getGroup()).isEqualTo(SETUP_GROUP);
    }

    @Test
    @DisplayName("동록된 id를 가진 학생을 조회하고, 정보를 확인합니다.")
    void getStudentWithExistedStudent() {
        assertThat(studentService.getStudent(EXISTED_ID)).isNotNull();

        Student student = studentService.getStudent(EXISTED_ID);

        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(EXISTED_ID);
        assertThat(student.getName()).isEqualTo(SETUP_NAME);
        assertThat(student.getGender()).isEqualTo(SETUP_GENDER);
        assertThat(student.getGroup()).isEqualTo(SETUP_GROUP);
    }

    @Test
    @DisplayName("존재하지 않는 학생정보 요청시, NotFound Exception 을 발생시킵니다.")
    void getStudentWithNotExistedId() {
        assertThatThrownBy(() -> studentService.getStudent(NOT_EXISTED_ID))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    @DisplayName("학생을 등록하고, 등록된 정보를 확인합니다.")
    void createStudent() {
        StudentCreateDto studentCreateDto = StudentCreateDto.builder()
                .name(CREATE_NAME)
                .gender(CREATE_GENDER)
                .group(CREATE_GROUP)
                .build();

        StudentResponseDto studentResponseDto =
                studentService.createStudent(studentCreateDto);

        assertThat(studentResponseDto.getId()).isEqualTo(CREATE_ID);
        assertThat(studentResponseDto.getName()).isEqualTo(CREATE_NAME);
        assertThat(studentResponseDto.getGender()).isEqualTo(CREATE_GENDER);
        assertThat(studentResponseDto.getGroup()).isEqualTo(CREATE_GROUP);

        verify(studentRepository).save(any(Student.class));
    }

}
