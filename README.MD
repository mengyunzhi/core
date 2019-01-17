[english version](./readme.en.md)

[河北工业大学梦云智开发团队](http://www.mengyunzhi.com)核心库

# 版本号
1.5.3 *弃用*
1.5.3.1 *弃用*
2.1.2 *弃用*
3.0.0

## 已验证版本
spring-boot:2.0.5
spring-boot:2.1.2

# 使用方法
## 添加依赖
```xml
        <dependency>
            <groupId>com.mengyunzhi</groupId>
            <artifactId>core</artifactId>
            <version>2.0.5.RELEASE</version>
        </dependency>
```

## 默认规则
类型 | 规则
--- | ---
number | 判等
date | 判等
calender | 判等
string | 模糊查询

## 注解
名称 | 作用
--- | ---
GreaterThanOrEqualTo | 大于等于某个字段的值
Ignore | 忽略查询注解的字段
In | 某个字段在该数组中
LessThanOrEqualTo | 小于等于某个字段的值

## 前提
查询的实体，需要实现`YunzhiEntity`接口。

## 示例代码
详见`test`子模块。

# 贡献代码
1. `cd core && mvn clean install`
2. 维护功能。
3. 在`test`子模块建立测试模块，并写单元测试。

