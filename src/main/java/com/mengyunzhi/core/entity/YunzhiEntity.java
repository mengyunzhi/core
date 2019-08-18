package com.mengyunzhi.core.entity;

import com.mengyunzhi.core.service.CommonService;

public interface YunzhiEntity<ID> {
    ID getId();

    Boolean getDeleted();

    /**
     * 将所有的字段设置为null
     * 该方法使用反射，效率低。
     * 建议时间上有条件的重写该方法。
     */
    default void setAllFieldsToNull() {
         CommonService.setAllFieldsToNull(this);
    }
}