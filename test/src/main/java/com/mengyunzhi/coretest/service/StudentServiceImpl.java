package com.mengyunzhi.coretest.service;

import com.mengyunzhi.coretest.entity.Student;
import com.mengyunzhi.coretest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Override
    public Student getOneSavedStudent() {
        Student student = new Student();
        student.getAddress().setNubmer("number");
        student.getAddress().setStreet("street");
        student.setName("name");
        student.setSex(true);
        student.setAge((byte) 20);
        studentRepository.save(student);
        return student;
    }
}
