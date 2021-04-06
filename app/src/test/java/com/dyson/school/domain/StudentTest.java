package com.dyson.school.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    @Test
    void creationWithBuilder() {
        Student student = Student.builder()
                .id(1L)
                .name("코돌쓰")
                .gender("남")
                .group("1학년1반")
                .build();

        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getName()).isEqualTo("코돌쓰");
        assertThat(student.getGender()).isEqualTo("남");
        assertThat(student.getGroup()).isEqualTo("1학년1반");
    }

    @Test
    void changeWith() {
        Student student = Student.builder()
                        .id(1L)
                        .name("코돌쓰")
                        .gender("남")
                        .group("1학년1반")
                        .build();

        student.changeWith(Student.builder()
                                    .name("나영쓰")
                                    .gender("여")
                                    .build());

        assertThat(student.getName()).isEqualTo("나영쓰");
        assertThat(student.getGender()).isEqualTo("여");
    }
}
