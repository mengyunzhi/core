package com.mengyunzhi.coretest.entity;

import javax.persistence.MappedSuperclass;

/**
 * 人类
 */
@MappedSuperclass
public abstract class Human {
    private Boolean sex;

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
