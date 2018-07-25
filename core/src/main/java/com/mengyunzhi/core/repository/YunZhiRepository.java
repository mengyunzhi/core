package com.mengyunzhi.core.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author panjie
 * 加入软删除
 */
@NoRepositoryBean
public interface YunZhiRepository<T, ID extends Serializable> extends CrudRepository<T, ID>, JpaSpecificationExecutor {
    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleted = false")
    T findOne(ID id);

    @Override
    @Transactional
    default boolean exists(ID var1) {
        return findOne(var1) != null;
    }

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.deleted = false")
    Iterable<T> findAll();

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id in ?1 and e.deleted = false")
    Iterable<T> findAll(Iterable<ID> var1);

    @Override
    @Transactional
    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long count();
}
