package com.dyson.school.controller;

import com.dyson.school.application.StudentService;
import com.dyson.school.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        Student student = Student.builder()
                                    .id(1L)
                                    .name("코돌쓰")
                                    .password("qwer1234")
                                    .group("1학년2반")
                                    .gender("남")
                                    .phone("010-11111-2222")
                                    .address("백석동 동문 굿모닝힐 2차")
                                    .build();

        given(studentService.getStudents()).willReturn(List.of(student));
    }

    @Test
    void list() throws Exception{
        mockMvc.perform(
                get("/students")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
        .andExpect(content().string(containsString("코돌쓰")));
    }
}
