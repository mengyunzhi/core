package com.mengyunzhi.core.service;

import com.mengyunzhi.core.entity.YunzhiEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author panjie
 */
public interface YunzhiService<T> {
    Page<YunzhiEntity<T>> page(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity<T> entity, Pageable pageable);

    List<YunzhiEntity<T>> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity<T> entity);
}
