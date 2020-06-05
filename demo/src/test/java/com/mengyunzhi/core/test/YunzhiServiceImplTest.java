package com.mengyunzhi.core.test;

import com.mengyunzhi.core.demo.DemoApplication;
import com.mengyunzhi.core.demo.entity.Klass;
import com.mengyunzhi.core.demo.entity.Teacher;
import com.mengyunzhi.core.demo.repository.KlassRepository;
import com.mengyunzhi.core.demo.service.KlassService;
import com.mengyunzhi.core.service.YunzhiService;
import com.mengyunzhi.core.service.YunzhiServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class YunzhiServiceImplTest {
    @Autowired
    private KlassService klassService;
    @Autowired
    private KlassRepository klassRepository;

    @Test
    public void test() {
        YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        long count = klassRepository.count();

        Klass originKlass = klassService.getOneSavedKlass();

        Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 加入日期对比
        Teacher teacher = new Teacher();
        teacher.setAllFieldsToNull();
        klass.setTeacher(teacher);

        // 起始时间大于存储时间,断言0
        Timestamp timestamp = originKlass.getTeacher().getCreateTimestamp();
        Timestamp beginTimestamp = new Timestamp(timestamp.getTime() + 1);

        teacher.setBeginCreateTimestamp(beginTimestamp);
        List<Klass> klassPage = yunzhiService.findAll(klassRepository, klass);

        Assertions.assertThat(klassPage.size()).isEqualTo(count);

        // 起始时间小于存储时间，断言1
        teacher.setBeginCreateTimestamp(new Timestamp(timestamp.getTime() - 1));
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        // 结束时间小于当前时间
        teacher.setEndCreateTimestamp(new Timestamp(timestamp.getTime() - 1));
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count);

        // 结束时间大于当前时间
        teacher.setEndCreateTimestamp(new Timestamp(timestamp.getTime() + 1));
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);
    }

    @Test
    public void equalTo() {
        YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        long count = klassRepository.count();

        Klass originKlass = klassService.getOneSavedKlass();

        Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 名称少一个字符，进行精确查询，未找到
        klass.setAddress(originKlass.getAddress().substring(0, originKlass.getAddress().length() - 2));
        List<Klass> klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count);

        // 查询名称与实体存的相同，进行精确查询，能找到
        klass.setAddress(originKlass.getAddress());
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        // 设置另一个注解的字段
        klass.setQueryAddress(originKlass.getAddress().substring(0, originKlass.getAddress().length() - 2));
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count);

        // 查询名称与实体存的相同，进行精确查询，能找到
        klass.setQueryAddress(originKlass.getAddress());
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);
    }

    @Test
    public void isNullTest() {
        YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        long count = klassRepository.count();

        klassService.getOneSavedKlass();

        Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 名称少一个字符，进行精确查询，未找到
        List<Klass> klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        klass.setLongTestIsNull(true);
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count);
    }

    @Test
    public void isNotNullTest() {
        YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        long count = klassRepository.count();


        klassService.getOneSavedKlass();

        Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 名称少一个字符，进行精确查询，未找到
        List<Klass> klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        klass.setNotNullFiled(true);
        klassPage = yunzhiService.findAll(klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count);
    }
}
