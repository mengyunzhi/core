package com.mengyunzhi.core.test.repository;

import com.mengyunzhi.core.test.entity.Klass;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author panjie
 */
public interface KlassRepository extends CrudRepository<Klass, Long>, JpaSpecificationExecutor {
}
