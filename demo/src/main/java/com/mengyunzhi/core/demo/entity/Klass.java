package com.mengyunzhi.core.demo.entity;

import com.mengyunzhi.core.annotation.query.EqualTo;
import com.mengyunzhi.core.annotation.query.Ignore;
import com.mengyunzhi.core.annotation.query.IsNotNull;
import com.mengyunzhi.core.annotation.query.IsNull;
import com.mengyunzhi.core.entity.YunzhiEntity;

import javax.persistence.*;

/**
 * @author panjie
 */
@Entity
public class Klass implements YunzhiEntity<Long> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final Boolean deleted = false;

    @Column(nullable = false)
    private final Long deleteAt = 0L;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 学生总数
     */
    private Short totalStudentCount;

    /**
     * 整形测试
     */
    private Integer integerTest;

    /**
     * 长整形测试
     */
    private Long longTest;

    /**
     * 手动equal测试字段
     */
    @EqualTo
    private String address;

    /**
     * 空字段
     * 查询时本字段的值与address值进行比对
     */
    private String nullField;
    @EqualTo(name = "address")
    @Transient
    private String queryAddress;

    @ManyToOne
    private Teacher teacher;

    /**
     * 忽略本查询条件
     */
    @ManyToOne
    @Ignore
    private Teacher ignoreTeacher;

    @IsNull(name = "longTest")
    @Transient
    private Boolean longTestIsNull;

    @IsNotNull(name = "nullField")
    @Transient
    private Boolean notNullFiled;

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public long getDeleteAt() {
        return this.deleteAt;
    }

    @Deprecated
    private static void setDeleted(final Boolean deleted) {
        throw new RuntimeException("禁止手动对deleteAt字段赋值");
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(final Teacher teacher) {
        this.teacher = teacher;
    }

    public Short getTotalStudentCount() {
        return this.totalStudentCount;
    }

    public void setTotalStudentCount(final Short totalStudentCount) {
        this.totalStudentCount = totalStudentCount;
    }

    public Integer getIntegerTest() {
        return this.integerTest;
    }

    public void setIntegerTest(final Integer integerTest) {
        this.integerTest = integerTest;
    }

    public Long getLongTest() {
        return this.longTest;
    }

    public void setLongTest(final Long longTest) {
        this.longTest = longTest;
    }

    public Teacher getIgnoreTeacher() {
        return this.ignoreTeacher;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getQueryAddress() {
        return this.queryAddress;
    }

    public void setQueryAddress(final String queryAddress) {
        this.queryAddress = queryAddress;
    }

    public Boolean getLongTestIsNull() {
        return this.longTestIsNull;
    }

    public void setLongTestIsNull(final Boolean longTestIsNull) {
        this.longTestIsNull = longTestIsNull;
    }

    public String getNullField() {
        return this.nullField;
    }

    public void setNullField(final String nullField) {
        this.nullField = nullField;
    }

    public Boolean getNotNullFiled() {
        return this.notNullFiled;
    }

    public void setNotNullFiled(final Boolean notNullFiled) {
        this.notNullFiled = notNullFiled;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setIgnoreTeacher(final Teacher ignoreTeacher) {
        this.ignoreTeacher = ignoreTeacher;
    }
}
