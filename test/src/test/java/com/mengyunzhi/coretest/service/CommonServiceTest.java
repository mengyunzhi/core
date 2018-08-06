package com.mengyunzhi.coretest.service;

import com.mengyunzhi.coretest.entity.Student;
import com.mengyunzhi.core.service.CommonService;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CommonServiceTest extends ServiceTest {
    @Test
    public void setAllFieldsToNull() {
        CommonService.getNullFieldsObject(Student.class);
    }

    @Test
    public void getNullFieldsObject() {
        Student student = (Student) CommonService.getNullFieldsObject(Student.class);
        Assertions.assertThat(student.getSex()).isNull();
        Assertions.assertThat(student.getId()).isNull();
        Assertions.assertThat(student.getAddress()).isNull();
    }
}
