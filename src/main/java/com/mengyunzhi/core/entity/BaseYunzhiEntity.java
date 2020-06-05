package com.mengyunzhi.core.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 基类
 *
 * @author panjie
 */
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
