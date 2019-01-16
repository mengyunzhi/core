package com.mengyunzhi.core.test.repository;

import com.mengyunzhi.core.test.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

/**
 * @author panjie
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
}