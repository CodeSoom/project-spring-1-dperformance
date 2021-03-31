package com.dyson.school.dto;

import com.dyson.school.domain.Student;
import com.github.dozermapper.core.Mapping;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @NotBlank
    private String group;

    public Student toEntity() {
        return Student.builder()
                .name(this.name)
                .gender(this.gender)
                .group(this.group)
                .build();
    }

    @Builder
    public StudentUpdateDto(@NotBlank String name, @NotBlank String gender, @NotBlank String group) {
        this.name = name;
        this.gender = gender;
        this.group = group;
    }
}
