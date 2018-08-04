package com.mengyunzhi.coretest.repository;

import com.mengyunzhi.coretest.entity.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long>, JpaSpecificationExecutor {
}
