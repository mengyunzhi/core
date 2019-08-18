package com.mengyunzhi.core.demo.repository;

import com.mengyunzhi.core.demo.entity.Klass;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author panjie
 */
public interface KlassRepository extends CrudRepository<Klass, Long>, JpaSpecificationExecutor {
}
