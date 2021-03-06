package com.mengyunzhi.core.demo.service;

import com.mengyunzhi.core.demo.repository.TeacherRepository;
import com.mengyunzhi.core.service.CommonService;
import com.mengyunzhi.core.demo.entity.Address;
import com.mengyunzhi.core.demo.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author panjie
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    private final
    TeacherRepository teacherRepository;
    private final
    AddressService addressService;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, AddressService addressService) {
        this.teacherRepository = teacherRepository;
        this.addressService = addressService;
    }

    @Override
    public Teacher getOneSavedTeacher() {
        Teacher teacher = new Teacher();
        Address address = addressService.getOneSavedAddress();
        teacher.setAddress(address);
        teacher.setName(CommonService.getRandomStringByLength(4) + "测试班级名称" + CommonService.getRandomStringByLength(4));
        teacherRepository.save(teacher);
        return teacher;
    }
}
