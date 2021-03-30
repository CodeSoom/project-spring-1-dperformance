package com.dyson.school.dto;

import com.dyson.school.domain.Student;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentCreateDto {

    private String name;

    private String gender;

    private String group;

    public Student toEntity() {
        return Student.builder()
                .name(this.name)
                .gender(this.gender)
                .group(this.group)
                .build();
    }

    @Builder
    public StudentCreateDto(String name, String gender, String group) {
        this.name = name;
        this.gender = gender;
        this.group = group;
    }
}
