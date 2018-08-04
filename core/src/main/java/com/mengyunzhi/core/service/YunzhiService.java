package com.mengyunzhi.core.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author panjie
 */
public interface YunzhiService {
    /**
     * 通过传入的实体进行多条件查询
     * @param jpaSpecificationExecutor jpa多查询仓库
     * @param entity 实体
     * @param pageable 分页信息
     * @return 分页信息
     * panjie
     */
    Page<?> page(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity, Pageable pageable);

    /**
     * 通过传入的实体查询所有数据
     * @param jpaSpecificationExecutor jpa多查询仓库
     * @param entity 实体
     * @return
     * panjie
     */
    List<?> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity);
}
