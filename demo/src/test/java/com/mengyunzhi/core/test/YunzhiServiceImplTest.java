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
    public void LessThanOrGreaterThan() throws InterruptedException {
        final YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        Thread.sleep(1000);

        final Klass originKlass = this.klassService.getOneSavedKlass();

        final Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 加入日期对比
        final Teacher teacher = new Teacher();
        teacher.setAllFieldsToNull();
        klass.setTeacher(teacher);

        // 起始时间大于存储时间,断言0
        final Timestamp timestamp = originKlass.getTeacher().getCreateTimestamp();
        final Timestamp beginTimestamp = new Timestamp(timestamp.getTime() + 1);

        teacher.setBeginCreateTimestamp(beginTimestamp);
        List<Klass> klassPage = yunzhiService.findAll(this.klassRepository, klass);

        Assertions.assertThat(klassPage.size()).isEqualTo(0);

        // 起始时间小于存储时间，断言1
        teacher.setBeginCreateTimestamp(new Timestamp(timestamp.getTime() - 1));
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(1);

        // 结束时间小于当前时间
        teacher.setEndCreateTimestamp(new Timestamp(timestamp.getTime() - 1));
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(0);

        // 结束时间大于当前时间
        teacher.setEndCreateTimestamp(new Timestamp(timestamp.getTime() + 1));
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(1);
    }

    @Test
    public void equalTo() {
        final YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        final Klass originKlass = this.klassService.getOneSavedKlass();

        final Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 名称少一个字符，进行精确查询，未找到
        klass.setAddress(originKlass.getAddress().substring(0, originKlass.getAddress().length() - 2));
        List<Klass> klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(0);

        // 查询名称与实体存的相同，进行精确查询，能找到
        klass.setAddress(originKlass.getAddress());
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(1);

        // 设置另一个注解的字段
        klass.setQueryAddress(originKlass.getAddress().substring(0, originKlass.getAddress().length() - 2));
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(0);

        // 查询名称与实体存的相同，进行精确查询，能找到
        klass.setQueryAddress(originKlass.getAddress());
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(1);
    }

    @Test
    public void isNullTest() {
        final YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        final long count = this.klassRepository.count();

        this.klassService.getOneSavedKlass();

        final Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 查找所有
        List<Klass> klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        // 查找longTest为null的
        klass.setLongTestIsNull(true);
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(0);
    }

    @Test
    public void isNotNullTest() {
        final YunzhiService<Klass> yunzhiService = new YunzhiServiceImpl();
        final long count = this.klassRepository.count();


        this.klassService.getOneSavedKlass();

        final Klass klass = new Klass();
        klass.setAllFieldsToNull();

        // 名称少一个字符，进行精确查询，未找到
        List<Klass> klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(count + 1);

        klass.setNotNullFiled(true);
        klassPage = yunzhiService.findAll(this.klassRepository, klass);
        Assertions.assertThat(klassPage.size()).isEqualTo(0);
    }
}
