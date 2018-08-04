package com.mengyunzhi.coretest.service;

import com.mengyunzhi.coretest.entity.Student;
import com.mengyunzhi.core.service.CommonService;
import org.junit.Test;

public class CommonServiceTest extends ServiceTest {
    @Test
    public void setAllFieldsToNull() {
        CommonService.getNullFieldsObject(Student.class);
    }
}
