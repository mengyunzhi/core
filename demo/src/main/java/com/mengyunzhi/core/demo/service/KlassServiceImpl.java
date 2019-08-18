package com.mengyunzhi.core.demo.service;

import com.mengyunzhi.core.demo.repository.KlassRepository;
import com.mengyunzhi.core.service.CommonService;
import com.mengyunzhi.core.demo.entity.Klass;
import com.mengyunzhi.core.demo.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author panjie
 * 班级
 */
@Service
public class KlassServiceImpl implements KlassService {
    private final
    KlassRepository klassRepository;
    private final
    TeacherService teacherService;

    @Autowired
    public KlassServiceImpl(KlassRepository klassRepository, TeacherService teacherService) {
        this.klassRepository = klassRepository;
        this.teacherService = teacherService;
    }

    @Override
    public Klass getOneSavedKlass() {
        Teacher teacher = teacherService.getOneSavedTeacher();
        Klass klass = new Klass();
        klass.setName(CommonService.getRandomStringByLength(4) + "测试班级名称" + CommonService.getRandomStringByLength(4));
        klass.setLongTest(1000L);
        klass.setTotalStudentCount((short) 10);
        klass.setIntegerTest(100);
        klass.setTeacher(teacher);
        klass.setIgnoreTeacher(teacher);
        klass.setAddress(CommonService.getRandomStringByLength(4) + "测试地址");
        klassRepository.save(klass);
        return klass;
    }
}
