package com.mengyunzhi.core.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mengyunzhi.core.annotation.query.GreaterThanOrEqualTo;
import com.mengyunzhi.core.annotation.query.LessThanOrEqualTo;
import com.mengyunzhi.core.entity.BaseYunzhiEntity;
import com.mengyunzhi.core.entity.YunzhiEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author panjie
 */
@Entity
public class Teacher extends BaseYunzhiEntity implements YunzhiEntity<Long> {
    private String name;
    /**
     * 住址
     */
    @ManyToOne
    private Address address;

    /**
     * 创建时间
     */
    @CreationTimestamp
    private Calendar createTime;

    @CreationTimestamp
    private Timestamp createTimestamp;

    /**
     * 用于查询的开始创建时间
     */
    @Transient
    @JsonIgnore
    @GreaterThanOrEqualTo(name = "createTime")
    private Calendar beginCreateTime;

    /**
     * 用于查询的结束创建时间
     */
    @Transient
    @JsonIgnore
    @LessThanOrEqualTo(name = "createTime")
    private Calendar endCreateTime;

    @Transient
    @JsonIgnore
    @GreaterThanOrEqualTo(name = "createTimestamp")
    private Timestamp beginCreateTimestamp;

    @Transient
    @JsonIgnore
    @LessThanOrEqualTo(name = "createTimestamp")
    private Timestamp endCreateTimestamp;


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Calendar getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(final Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getBeginCreateTime() {
        return this.beginCreateTime;
    }

    public void setBeginCreateTime(final Calendar beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public Calendar getEndCreateTime() {
        return this.endCreateTime;
    }

    public void setEndCreateTime(final Calendar endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Timestamp getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setCreateTimestamp(final Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Timestamp getBeginCreateTimestamp() {
        return this.beginCreateTimestamp;
    }

    public void setBeginCreateTimestamp(final Timestamp beginCreateTimestamp) {
        this.beginCreateTimestamp = beginCreateTimestamp;
    }

    public Timestamp getEndCreateTimestamp() {
        return this.endCreateTimestamp;
    }

    public void setEndCreateTimestamp(final Timestamp endCreateTimestamp) {
        this.endCreateTimestamp = endCreateTimestamp;
    }
}
