package com.mengyunzhi.core.demo.entity;

import com.mengyunzhi.core.entity.YunzhiEntity;

import javax.persistence.*;

/**
 * @author panjie
 */
@Entity
public class Student implements YunzhiEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private final Boolean deleted = false;

    @Column(nullable = false)
    private final Long deleteAt = 0L;

    @Override
    public Integer getId() {
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

    public void setId(final Integer id) {
        this.id = id;
    }
}
