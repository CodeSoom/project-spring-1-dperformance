package com.dyson.school.controllers;

import com.dyson.school.application.StudentService;
import com.dyson.school.domain.Student;
import com.dyson.school.dto.StudentCreateDto;
import com.dyson.school.dto.StudentResponseDto;
import com.dyson.school.dto.StudentUpdateDto;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private static final String UPDATE_NAME = "나영쓰";
    private static final String UPDATE_GENDER = "여";
    private static final String UPDATE_GROUP = "1학년2반";

    private static final String INVALID_CONTENT = "{\"id\":\"1\", \"name\":\"\", \"gender\":\"\"}";


    private Student setUpStudent;

    private Student updateStudent;

    private StudentResponseDto studentResponseDto;

    private StudentResponseDto updateStudentResponseDto;

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

        updateStudent = Student.builder()
                .id(EXISTED_ID)
                .name(UPDATE_NAME)
                .gender(UPDATE_GENDER)
                .group(UPDATE_GROUP)
                .build();

        // [GET] - /students
        given(studentService.getStudents()).willReturn(List.of(setUpStudent));

        // [GET] - /students/{id}, EXISTED_ID
        given(studentService.getStudent(EXISTED_ID)).willReturn(setUpStudent);

        // [GET] - /students/{id}, NOT_EXISTED_ID
        given(studentService.getStudent(NOT_EXISTED_ID))
                .willThrow(new StudentNotFoundException(NOT_EXISTED_ID));

        // [POST] - /students
        studentResponseDto = StudentResponseDto.of(setUpStudent);
        given(studentService.createStudent(any(StudentCreateDto.class)))
                .willReturn(studentResponseDto);

        // [PUT] - /students/{id}, EXISTED_ID
        updateStudentResponseDto = StudentResponseDto.of(updateStudent);
        given(studentService.updateStudent(
                eq(EXISTED_ID),
                any(StudentUpdateDto.class))
        )
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    StudentUpdateDto studentUpdateDto = invocation.getArgument(1);
                    return StudentResponseDto.builder()
                            .id(id)
                            .name(studentUpdateDto.getName())
                            .gender(studentUpdateDto.getGender())
                            .group(studentUpdateDto.getGroup())
                            .build();
                });

        // [PUT] - /students/{id}, NOT_EXISTED_ID
        given(studentService.updateStudent(
                eq(NOT_EXISTED_ID),
                any(StudentUpdateDto.class))
        )
                .willThrow(new StudentNotFoundException(NOT_EXISTED_ID));

        // [DELETE] - /students/{id}, NOT_EXISTED_ID
        given(studentService.deleteStudent(NOT_EXISTED_ID))
                .willThrow(new StudentNotFoundException(NOT_EXISTED_ID));

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
    @DisplayName("학생이 존재하지 않는경우 NOT_FOUND(404)를 반환합니다.")
    void detailWithNotExistedId() throws Exception {
        mockMvc.perform(get("/students/{id}", NOT_EXISTED_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("학생을 추가하고, 반환받은 정보를 확인합니다.")
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

        verify(studentService).createStudent(any(StudentCreateDto.class));
    }

    @Test
    @DisplayName("잘못된 정보로 생성요청시 BAD_REQUEST(400)를 반환합니다. ")
    void createWithInvalidAttribute() throws Exception {
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_CONTENT)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("학생 정보를 수정합니다.")
    void updateWithExistedStudent() throws Exception {
        mockMvc.perform(put("/students/{id}", EXISTED_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updateStudent))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("나영쓰")))
                .andExpect(jsonPath("id").value(updateStudentResponseDto.getId()))
                .andExpect(jsonPath("name").value(updateStudentResponseDto.getName()))
                .andExpect(jsonPath("gender").value(updateStudentResponseDto.getGender()))
                .andExpect(jsonPath("group").value(updateStudentResponseDto.getGroup()));

        verify(studentService).updateStudent(eq(EXISTED_ID), any(StudentUpdateDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 학생 수정 요청시 NOT_FOUND(404)를 반환합니다.")
    void updateWithNotExistedStudent() throws Exception {
        mockMvc.perform(put("/students/{id}", NOT_EXISTED_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateStudent))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("잘못된 형식으로 학생 수정 요청시 BAD_REQUEST(400)를 반환합니다.")
    void updateWithInvalidAttribute() throws Exception {
        mockMvc.perform(put("/students/{id}", EXISTED_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_CONTENT)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("학생을 삭제합니다.")
    void destroyWithExistedStudent() throws Exception {
        mockMvc.perform(
                delete("/students/{id}", EXISTED_ID)
        )
                .andExpect(status().isOk());
        verify(studentService).deleteStudent(EXISTED_ID);
    }

    @Test
    @DisplayName("존재하지 않는 학생 삭제 요청시 NOT_FOUND(404)를 반환합니다.")
    void destroyWithNotExistedStudent() throws Exception {
        mockMvc.perform(
                delete("/students/{id}", NOT_EXISTED_ID)
        )
                .andExpect(status().isNotFound());
        verify(studentService).deleteStudent(NOT_EXISTED_ID);
    }
}
