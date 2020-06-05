package com.mengyunzhi.core.entity;

import com.mengyunzhi.core.service.CommonService;

/**
 * 云智实体
 *
 * @param <ID> 实体关键字类型
 * @author panjie
 */
public interface YunzhiEntity<ID> {
    /**
     * 获取关键字
     *
     * @return 关键字
     */
    ID getId();

    /**
     * 记录是否被删除
     *
     * @return 已删除 true; 未删除 false;
     */
    Boolean getDeleted();

    /**
     * 删除时间
     * 该字段仅用于协助形成 unique 索引。
     * 比如：用户管理中，使用 用户与删除时间 形成unique索引以规避已删除用户占用用户名的问题
     *
     * @return 已删除返回删除的时间戳，未删除返回0
     */
    long getDeleteAt();

    /**
     * 将所有的字段设置为null
     * 该方法使用反射，效率低。
     * 建议时间上有条件的重写该方法。
     *
     * @return 格式化后字段全部为null实体
     */
    default YunzhiEntity<ID> setAllFieldsToNull() {
        return (YunzhiEntity) CommonService.setAllFieldsToNull(this);
    }
}