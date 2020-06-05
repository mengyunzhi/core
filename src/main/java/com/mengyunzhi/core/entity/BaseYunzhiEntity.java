package com.mengyunzhi.core.entity;

import javax.persistence.*;

/**
 * 基类
 * 添加MappedSuperclass防止继承于此类的实体报 No identifier specified for entity 错误
 *
 * @author panjie
 */
@MappedSuperclass
public abstract class BaseYunzhiEntity implements YunzhiEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected final Boolean deleted = false;

    @Column(nullable = false)
    protected final Long deleteAt = 0L;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public long getDeleteAt() {
        return this.deleteAt;
    }
}
