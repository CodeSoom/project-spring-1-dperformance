// TODO
// id
// name
// password
// 학년반 - group
// 생년월일 - birthday
// 성별 - gender
// 핸드폰번호 - phone
// 주소 - address
// image

package com.dyson.school.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Student {

    private Long id;

    private String name;

    private String gender;

    private String group;

    @Builder
    public Student(Long id, String name, String gender, String group) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.group = group;
    }

    public void changeWith(Student source) {
        this.name = source.getName();
        this.gender = source.getGender();
        this.group = source.getGroup();
    }
}
