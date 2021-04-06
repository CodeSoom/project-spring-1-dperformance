package com.dyson.school.domain;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Student save(Student student);
}
