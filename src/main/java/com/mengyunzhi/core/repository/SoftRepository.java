package com.mengyunzhi.core.repository;

import com.mengyunzhi.core.entity.YunzhiEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author panjie
 * 加入软删除
 */
@NoRepositoryBean
public interface SoftRepository<T extends YunzhiEntity<ID>, ID extends Serializable> extends CrudRepository<T, ID>, JpaSpecificationExecutor {
    /**
     * 通过主键查找数据
     *
     * @param id 关键字
     * @return 实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleted = false")
    Optional<T> findById(ID id);

    /**
     * 判断主键记录是否否在
     *
     * @param id 主键
     * @return 存在：true; 不存在 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    default boolean existsById(final ID id) {
        return this.findById(id).isPresent();
    }

    /**
     * 获取所有数据
     *
     * @return 数据迭代器
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Query("select e from #{#entityName} e where e.deleted = false")
    Iterable<T> findAll();

    /**
     * <p>Examples:
     * ids = [1, 2]，则查找出id = 1，id = 2的数据
     * 查找位于关键字数据组中的所有数据
     *
     * @param ids 关键字组成的迭代器
     * @return 实体数组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Query("select e from #{#entityName} e where e.id in ?1 and e.deleted = false")
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * 计算数据总条数（不包含已被软删除的数据）
     *
     * @return 数据条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long count();

    /**
     * 软删除
     * 在数据表中加入对应的字段，不实际执行删除操作
     *
     * @param id 实体ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update #{#entityName} e set e.deleteAt = UNIX_TIMESTAMP() and e.deleted = true where e.id = ?1")
    void softDelete(Long id);

    /**
     * 注意事务传播级别 挂起存在的事务 创建新事务
     * 如果使用默认的 REQUIRE 传播级别 使用已存在的事务
     * 当内层代码 hardDelete 发生异常时 会进行标记
     * 引发错误 Transaction silently rolled back because it has been marked as rollback-only
     * 内层创建新事务避免该问题
     * 事务管理参考：https://segmentfault.com/a/1190000022531982
     *
     * @param id 关键字
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {})
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from User user where user.id = ?1")
    void hardDelete(Long id);

    /**
     * 删除某条记录
     * 先尝试硬删除
     * 硬删除不成功，则进行软删除
     *
     * @param id 关键字
     */
    @Transactional(rollbackFor = Exception.class)
    default void deleteById(final Long id) {
        try {
            this.hardDelete(id);
        } catch (final Exception e) {
            this.softDelete(id);
        }
    }

    /**
     * 删除实体
     *
     * @param var1 实体
     */
    @Override
    @Deprecated
    default void delete(final T var1) {
        throw new RuntimeException("请使用deleteById或hardDelete方法");
    }

    /**
     * 删除所有
     *
     * @param var1 要删除实体列表
     */
    @Override
    @Deprecated
    default void deleteAll(final Iterable<? extends T> var1) {
        throw new RuntimeException("方法尚未实现，请使用deleteById或hardDelete方法");
    }

    /**
     * 清空数据表
     */
    @Override
    @Deprecated
    default void deleteAll() {
        throw new RuntimeException("方法尚未实现，请使用deleteById或hardDelete方法");
    }

}
