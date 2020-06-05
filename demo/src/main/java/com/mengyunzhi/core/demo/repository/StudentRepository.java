package com.mengyunzhi.core.demo.repository;

import com.mengyunzhi.core.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author panjie
 */
public interface StudentRepository extends CrudRepository<Student, Integer>, JpaSpecificationExecutor {
}
