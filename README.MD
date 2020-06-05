[english version](./readme.en.md)

[河北工业大学梦云智开发团队](http://www.mengyunzhi.com)核心库

# 版本号
1.5.3 *弃用*
1.5.3.1 *弃用*
2.1.2 *弃用*
3.0.0 *弃用*
3.0.1 发布中

## 已验证版本
spring-boot:2.0.5
spring-boot:2.1.2
spring-boot:2.1.7
# 使用方法
## 添加依赖
```xml
        <dependency>
            <groupId>com.mengyunzhi</groupId>
            <artifactId>core</artifactId>
            <version>2.1.7.RELEASE</version>
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
EqualTo | 完全等于
IsNull | 为null
IsNotNull | 不为null

## 示例代码
详见`demo`子模块。

# 起动环境
1. `mvn clean install`
2. 找到`pom.xml`，去除相关注释，维护功能。
3. 在`demo`子模块建立测试模块，并写单元测试。
4. 发布后，找到`pom.xml`加入相关的注释.

# 版本发布
https://segmentfault.com/n/1330000015884709

