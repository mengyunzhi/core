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
public class YunzhiServiceImpl<O> implements YunzhiService<O> {

    @Override
    public Page<O> page(final JpaSpecificationExecutor jpaSpecificationExecutor, final YunzhiEntity entity, final Pageable pageable) {
        final Specification<O> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification, pageable);
    }

    @Override
    public List<O> findAll(final JpaSpecificationExecutor jpaSpecificationExecutor, final YunzhiEntity entity) {
        final Specification<O> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification);
    }


    private Specification<O> getSpecificationByEntity(final YunzhiEntity entity) {
        final Specification<O> specification = new YunzhiSpecification<>(entity);
        return specification;
    }
}
