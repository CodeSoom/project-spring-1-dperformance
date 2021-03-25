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

    private String password;

    private String group;

    private String birthday;

    private String gender;

    private String phone;

    private String address;

    private String image;

    @Builder
    public Student(Long id, String name, String password, String group, String birthday, String gender, String phone, String address, String image) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.group = group;
        this.birthday = birthday;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }
}
