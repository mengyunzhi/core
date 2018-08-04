package com.mengyunzhi.coretest.service;

import com.mengyunzhi.core.service.CommonService;
import com.mengyunzhi.coretest.entity.Student;
import com.mengyunzhi.coretest.repository.StudentRepository;
import com.mengyunzhi.core.service.YunzhiService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YunzhiServiceImplTest extends ServiceTest {
    @Autowired
    YunzhiService yunzhiService;
    @Autowired
    StudentRepository studentRepository;
    @Autowired StudentService studentService;

    @Test
    @SuppressWarnings("unchecked")
    public void all() {
        Student student = studentService.getOneSavedStudent();
        Student student1 = (Student) CommonService.getNullFieldsObject(Student.class);
        student1.setAddress(student.getAddress());
        List<Student> studentList = (List<Student>) yunzhiService.findAll(studentRepository, student1);
        Assertions.assertThat(studentList.size()).isEqualTo(1);

        student1.getAddress().setNubmer("xsdfsdfsdf");
        studentList = (List<Student>) yunzhiService.findAll(studentRepository, student1);
        Assertions.assertThat(studentList.size()).isEqualTo(0);
    }

}
