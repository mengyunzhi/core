package com.mengyunzhi.core.service;


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
    public Page<T> page(JpaSpecificationExecutor jpaSpecificationExecutor, T entity, Pageable pageable) {
        Specification<T> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification, pageable);
    }

    @Override
    public List<T> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, T entity) {
        Specification<T> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification);
    }


    private Specification<T> getSpecificationByEntity(T entity) {
        Specification<T> specification = new YunzhiSpecification<>(entity);
        return specification;
    }
}
