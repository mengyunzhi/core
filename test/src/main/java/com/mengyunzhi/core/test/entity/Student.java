package com.mengyunzhi.core.test.entity;

import com.mengyunzhi.core.entity.YunzhiEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author panjie
 */
@Entity
public class Student implements YunzhiEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean deleted = false;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
