package com.mengyunzhi.core.service;


import com.mengyunzhi.core.annotation.query.GreaterThanOrEqualTo;
import com.mengyunzhi.core.annotation.query.Ignore;
import com.mengyunzhi.core.annotation.query.In;
import com.mengyunzhi.core.annotation.query.LessThanOrEqualTo;
import com.mengyunzhi.core.entity.YunzhiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.Embeddable;
import javax.persistence.criteria.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @author panjie
 * 团队综合查询实现类
 */
public class YunzhiServiceImpl<T> implements YunzhiService<Object> {
    private final static Logger logger = LoggerFactory.getLogger(YunzhiServiceImpl.class);

    /**
     * 分页查询
     * @param jpaSpecificationExecutor JPA综合查询SPEC
     * @param entity 实体
     * @param pageable 分页
     * @return
     */
    @Override
    public Page<Object> page(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity entity, Pageable pageable) {
        Specification<Object> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification, pageable);
    }

    @Override
    public List<Object> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, YunzhiEntity entity) {
        Specification<Object> specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification);
    }

    private Specification<Object> getSpecificationByEntity(Object entity) {
        Specification<Object> specification;
        specification = new Specification<Object>() {
            private Predicate predicate = null;
            private CriteriaBuilder criteriaBuilder;

            // 设置and谓语.注意，这里只能设置and关系的谓语，如果谓语为OR，则需要手动设置
            private void andPredicate(Predicate predicate) {
                if (null != predicate) {
                    if (null == this.predicate) {
                        this.predicate = predicate;
                    } else {
                        this.predicate = this.criteriaBuilder.and(this.predicate, predicate);
                    }
                }
            }

            private void generatePredicate(Object entity, From<Object, ?> root) {
                logger.debug("反射字段，按字段类型，进行综合查询");
                Field[] fields = entity.getClass().getDeclaredFields();
                try {
                    for (Field field : fields) {
                        logger.debug("设置字段可见，并获取实体值");
                        field.setAccessible(true);
                        Object value = field.get(entity);

                        if (value != null) {
                            if (Modifier.isFinal(field.getModifiers())) {
                                logger.debug("字段类型为final");
                                continue;
                            }

                            if (Modifier.isStatic(field.getModifiers())) {
                                logger.debug("字段类型为static");
                                continue;
                            }

                            String name = field.getName();
                            if (name.equals("serialVersionUID")) {
                                logger.debug("字段名为serialVersionUID");
                                continue;
                            }

                            if (field.getAnnotation(Ignore.class) != null) {
                                logger.debug("存在@Ignore注解, 跳出");
                                continue;
                            }

                            if (this.beginOrEndQuery(root, value, field)) {
                                logger.debug("执行了大于等于或是小于等于查询");
                                continue;
                            }

                            if (this.inQuery(root, field, value)) {
                                logger.debug("执行了范围查询");
                                continue;
                            }

                            // 按字段类型进行查询
                            if (value instanceof Boolean) {
                                logger.debug("布尔值");
                                Boolean booleanValue = ((Boolean) value);
                                this.andPredicate(criteriaBuilder.equal(root.get(name).as(Boolean.class), booleanValue));
                            } else if (value instanceof String) {
                                logger.debug("字符串则进行模糊查询");
                                String stringValue = ((String) value);
                                if (!stringValue.isEmpty()) {
                                    this.andPredicate(criteriaBuilder.like(root.get(name).as(String.class), "%" + stringValue + "%"));
                                }
                            } else if (value instanceof Number || value instanceof Calendar || value instanceof Date) {
                                logger.debug("如果为number，则进行精确或范围查询");
                                this.andPredicate(criteriaBuilder.equal(root.get(name).as(value.getClass()), value));
                            } else if (value instanceof YunzhiEntity) {
                                logger.debug("是实体类");
                                YunzhiEntity yunZhiEntity = (YunzhiEntity) value;
                                if (yunZhiEntity.getId() != null) {
                                    logger.debug("对应的ManyToOne，加入了id, 则按ID查询");
                                    this.andPredicate(criteriaBuilder.equal(root.join(name).get("id"), yunZhiEntity.getId()));
                                } else {
                                    logger.debug("未加入id, 则进行Join查询");
                                    this.generatePredicate(value, root.join(name));
                                }

                            } else if (value instanceof Collection) {
                                Collection<?> collectionValue = (Collection<?>) value;
                                if (collectionValue.size() > 0) {
                                    logger.warn("暂不支持一对多，多对多查询");
                                    // todo: 一对多，多对多查询
                                }
                            } else {
                                Class<?> clazz = value.getClass();
                                if (clazz.isAnnotationPresent(Embeddable.class)) {
                                    logger.debug("为内部Embeddable");
                                    this.generatePredicate(value, root.join(name));
                                } else {
                                    logger.error("综合查询暂不支持传入的数据类型:" + name + "->" + field.toString() + " " + field.getClass().getName());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * in 查询
             * @param root 根查询实体
             * @param field 字段
             * @param value 值
             * @return 进行了in直询，true;未进行in查询,false.
             */
            private Boolean inQuery(From<Object, ?> root, Field field, Object value) {
                // 获取in注解
                In inQueryParam = field.getAnnotation(In.class);
                if (inQueryParam != null) {
                    logger.debug("存在@InQueryParam注解");
                    String inQueryName = inQueryParam.name();
                    /*
                     * In查询相关代码
                     * https://blog.csdn.net/jiangyu1013/article/details/79056231
                     * Expression：查询表达式
                     * 源码中支持isNull，isNotNull，in，返回值为根据表达式生成的谓语
                     */
                    logger.debug("执行In查询");
                    try {
                        Collection<?> collectionValue = (Collection<?>) value;
                        Expression<Object> expression = root.get(inQueryName);
                        this.andPredicate(expression.in(collectionValue));
                    } catch (ClassCastException e) {
                        String message = "将对象转换为Collention时发生错误。注意：in查询只能做用到Collention中";
                        if (value != null) {
                            message += "->" + value.getClass().getName();
                        }
                        logger.warn(message);
                        e.printStackTrace();
                    }
                }
                return inQueryParam != null;
            }

            /**
             * 范围查询 查询大于等于或是小于等于
             * @param root 基实体
             * @param value 值
             * @param field 字段
             * @return Boolean 是否执行了范围查询
             *
             */
            private Boolean beginOrEndQuery(From<Object, ?> root, Object value, Field field) {
                String name = field.getName();
                // 初始化两个界限的变量
                Boolean isBegin = false;
                Boolean isEnd = false;

                // 查找开始与结束的注解
                GreaterThanOrEqualTo beginQueryParam = field.getAnnotation(GreaterThanOrEqualTo.class);
                if (beginQueryParam != null) {
                    logger.debug("存在@BeginQueryParam注解, 进行大于等于查询");
                    isBegin = true;
                    name = beginQueryParam.name();
                } else if (field.getAnnotation(LessThanOrEqualTo.class) != null) {
                    logger.debug("存在@EndQueryParam注解");
                    isEnd = true;
                    name = field.getAnnotation(LessThanOrEqualTo.class).name();
                }

                if (isEnd || isBegin) {
                    if (value instanceof Number) {
                        logger.debug("传入类型为number");
                        if (value instanceof Byte) {
                            Byte byteValue = (Byte) value;
                            if (isBegin) {
                                this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Byte.class), byteValue));
                            } else {
                                this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Byte.class), byteValue));
                            }
                        } else if (value instanceof Short) {
                            Short shortValue = (Short) value;
                            if (isBegin) {
                                this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Short.class), shortValue));
                            } else {
                                this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Short.class), shortValue));
                            }
                        } else if (value instanceof Integer) {
                            Integer integerValue = (Integer) value;
                            if (isBegin) {
                                this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Integer.class), integerValue));
                            } else {
                                this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Integer.class), integerValue));
                            }
                        } else if (value instanceof Long) {
                            Long longValue = (Long) value;
                            if (isBegin) {
                                this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Long.class), longValue));
                            } else {
                                this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Long.class), longValue));
                            }
                        } else {
                            logger.error("大于等于、小于等于中的Number类型，暂时只支持到Byte, Short,Integer,Long");
                        }
                    } else if (value instanceof Calendar) {
                        Calendar calendarValue = (Calendar) value;
                        if (isBegin) {
                            this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Calendar.class), calendarValue));
                        } else {
                            this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Calendar.class), calendarValue));
                        }
                    } else if (value instanceof Date) {
                        logger.debug("Sql.Date类型");
                        Date dateValue = (Date) value;
                        if (isBegin) {
                            this.andPredicate(criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Date.class), dateValue));
                        } else {
                            this.andPredicate(criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Date.class), dateValue));
                        }
                    }
                }

                return isBegin || isEnd;
            }

            @Override
            public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // 设置CriteriaBuilder，用于合并谓语
                this.criteriaBuilder = criteriaBuilder;
                this.generatePredicate(entity, root);
                return this.predicate;
            }
        };

        return specification;
    }

}
