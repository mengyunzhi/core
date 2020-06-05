package com.mengyunzhi.core.demo.repository;

import com.mengyunzhi.core.demo.entity.Klass;
import com.mengyunzhi.core.repository.SoftRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author panjie
 */
public interface KlassRepository extends SoftRepository<Klass, Long>, JpaSpecificationExecutor {
}
