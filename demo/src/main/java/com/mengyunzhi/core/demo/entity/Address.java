package com.mengyunzhi.core.demo.entity;

import com.mengyunzhi.core.entity.YunzhiEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 住址
 * @author panjie
 */
@Entity
public class Address implements YunzhiEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean deleted = false;
    private String city;        // 市
    private String county;      // 县
    private Integer num;        // 号

    public Address() {
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setAllFieldsToNull() {
        this.id = null;
        this.city = null;
        this.county = null;
        this.num = null;
    }
}
