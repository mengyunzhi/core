package com.mengyunzhi.core.service;


import com.mengyunzhi.core.entity.YunzhiEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author panjie
 */
public class YunzhiServiceImpl<T> implements YunzhiService<T> {

    @Override
    public Page<YunzhiEntity<T>> page(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity<T> entity, Pageable pageable) {
        Specification<T> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification, pageable);
    }

    @Override
    public List<YunzhiEntity<T>> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity entity) {
        Specification<T> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification);
    }


    private Specification<T> getSpecificationByEntity(YunzhiEntity<T> entity) {
        Specification<T> specification = new YunzhiSpecification<>(entity);
        return specification;
    }
}
