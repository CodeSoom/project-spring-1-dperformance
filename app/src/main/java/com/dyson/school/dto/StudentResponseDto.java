package com.dyson.school.dto;

import com.dyson.school.domain.Student;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentResponseDto {

    private Long id;

    private String name;

    private String gender;

    private String group;

    @Builder
    public StudentResponseDto(Long id, String name, String gender, String group) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.group = group;
    }

    public static StudentResponseDto of(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .name(student.getName())
                .gender(student.getGender())
                .group(student.getGroup())
                .build();
    }
}
