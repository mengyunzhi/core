package com.mengyunzhi.core.demo.repository;

import com.mengyunzhi.core.demo.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

/**
 * @author panjie
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
}