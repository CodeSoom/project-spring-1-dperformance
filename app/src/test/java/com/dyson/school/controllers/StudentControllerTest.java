package com.dyson.school.controllers;

import com.dyson.school.application.StudentService;
import com.dyson.school.domain.Student;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.errors.StudentNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private static final Long EXISTED_ID = 1L;
    private static final Long NOT_EXISTED_ID = 1000L;

    private static final String SETUP_NAME = "코돌쓰";
    private static final String SETUP_GENDER = "남";
    private static final String SETUP_GROUP = "1학년1반";

    private Student setUpStudent;

    private StudentResponseDto studentResponseDto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        setUpStudent = Student.builder()
                .id(EXISTED_ID)
                .name(SETUP_NAME)
                .gender(SETUP_GENDER)
                .group(SETUP_GROUP)
                .build();

        // [GET] /students
        given(studentService.getStudents()).willReturn(List.of(setUpStudent));

        // [GET] /students/{id}, EXISTED_ID
        given(studentService.getStudent(EXISTED_ID)).willReturn(setUpStudent);

        // [GET] /students/{id}, NOT_EXISTED_ID
        given(studentService.getStudent(NOT_EXISTED_ID))
                .willThrow(new StudentNotFoundException(NOT_EXISTED_ID));

        // [POST]
        studentResponseDto = StudentResponseDto.of(setUpStudent);
        given(studentService.createStudent(any(StudentCreateDto.class)))
                .willReturn(studentResponseDto);
    }

    @Test
    @DisplayName("등록된 모든 학생 정보를 확인합니다.")
    void list() throws Exception {
        mockMvc.perform(
                get("/students")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(SETUP_NAME)))
                .andExpect(content().string(containsString(SETUP_GENDER)))
                .andExpect(content().string(containsString(SETUP_GROUP)));
    }

    @Test
    @DisplayName("등록된 id를 가진 학생을 확인합니다.")
    void detailWithExistedId() throws Exception {
        mockMvc.perform(get("/students/{id}", EXISTED_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(setUpStudent.getId()))
                .andExpect(jsonPath("name").value(setUpStudent.getName()))
                .andExpect(jsonPath("gender").value(setUpStudent.getGender()))
                .andExpect(jsonPath("group").value(setUpStudent.getGroup()));
    }

    @Test
    @DisplayName("조회하고자 하는 학생이 존재하지 않는 경우 NOT_FOUND")
    void detailWithNotExistedId() throws Exception {
        mockMvc.perform(get("/students/{id}", NOT_EXISTED_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("d")
    void createWithValidAttribute() throws Exception {
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(setUpStudent))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(studentResponseDto.getId()))
                .andExpect(jsonPath("name").value(studentResponseDto.getName()))
                .andExpect(jsonPath("gender").value(studentResponseDto.getGender()))
                .andExpect(jsonPath("group").value(studentResponseDto.getGroup()));
    }
}
