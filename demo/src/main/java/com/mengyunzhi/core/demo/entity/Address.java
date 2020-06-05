package com.mengyunzhi.core.demo.entity;

import com.mengyunzhi.core.entity.YunzhiEntity;

import javax.persistence.*;

/**
 * 住址
 * @author panjie
 */
@Entity
public class Address implements YunzhiEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final Boolean deleted = false;

    @Column(nullable = false)
    private final Long deleteAt = 0L;

    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;

    /**
     * 编号
     */
    private Integer num;

    public Address() {
    }

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public long getDeleteAt() {
        return this.deleteAt;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(final String county) {
        this.county = county;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(final Integer num) {
        this.num = num;
    }
    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Address setAllFieldsToNull() {
        this.id = null;
        this.city = null;
        this.county = null;
        this.num = null;
        return this;
    }
}
