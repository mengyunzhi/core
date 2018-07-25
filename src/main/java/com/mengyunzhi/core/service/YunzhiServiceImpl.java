package com.mengyunzhi.core.service;

import com.mengyunzhi.core.annotation.query.GreaterThanOrEqualToQuery;
import com.mengyunzhi.core.annotation.query.IgnoreQuery;
import com.mengyunzhi.core.annotation.query.InQueryParam;
import com.mengyunzhi.core.annotation.query.LessThanOrEqualTo;
import com.mengyunzhi.core.entity.YunZhiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * 多条件综合查询
 *
 * @author panjie
 */
@Service
public class YunzhiServiceImpl implements YunzhiService {
    private static final Logger logger = LoggerFactory.getLogger(YunzhiServiceImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public Page<Object> page(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity, Pageable pageable) {
        Specification specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification, pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity) {
        Specification specification = this.getSpecificationByEntity(entity);
        return jpaSpecificationExecutor.findAll(specification);
    }

    private Specification getSpecificationByEntity(Object entity) {
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

                            if (field.getAnnotation(IgnoreQuery.class) != null) {
                                logger.debug("存在@IgnoreQueryParam注解, 跳出");
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
                            if (value instanceof String) {
                                logger.debug("字符串则进行模糊查询");
                                String stringValue = ((String) value);
                                if (!stringValue.isEmpty()) {
                                    this.andPredicate(criteriaBuilder.like(root.get(name).as(String.class), "%" + stringValue + "%"));
                                }
                            } else if (value instanceof Number) {
                                logger.debug("如果为number，则进行精确或范围查询");
                                if (value instanceof Short) {
                                    Short shortValue = (Short) value;
                                    this.andPredicate(criteriaBuilder.equal(root.get(name).as(Short.class), shortValue));
                                } else if (value instanceof Integer) {
                                    Integer integerValue = (Integer) value;
                                    this.andPredicate(criteriaBuilder.equal(root.get(name).as(Integer.class), integerValue));
                                } else if (value instanceof Long) {
                                    Long longValue = (Long) value;
                                    this.andPredicate(criteriaBuilder.equal(root.get(name).as(Long.class), longValue));
                                } else {
                                    logger.error("综合查询Number类型，暂时只支持到Short,Integer,Long");
                                }
                            } else if (value instanceof Calendar) {
                                logger.debug("Calendar类型");
                                Calendar calendarValue = (Calendar) value;
                                this.andPredicate(criteriaBuilder.equal(root.get(name).as(Calendar.class), calendarValue));
                            } else if (value instanceof Date) {
                                logger.debug("Sql.Date类型");
                                Date dateValue = (Date) value;
                                this.andPredicate(criteriaBuilder.equal(root.get(name).as(Date.class), dateValue));
                            } else if (value instanceof YunZhiEntity) {
                                logger.debug("是实体类");
                                YunZhiEntity yunZhiEntity = (YunZhiEntity) value;
                                if (yunZhiEntity.getId() != null) {
                                    logger.debug("对应的ManyToOne，加入了id, 则按ID查询");
                                    this.andPredicate(criteriaBuilder.equal(root.join(name).get("id").as(Long.class), yunZhiEntity.getId()));
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
                                logger.error("综合查询暂不支持传入的数据类型:" + name.toString() + field.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * in 查询
             * @param root
             * @param field
             * @param value
             * @return
             */
            private Boolean inQuery(From<Object, ?> root, Field field, Object value) {
                // 获取in注解
                InQueryParam inQueryParam = field.getAnnotation(InQueryParam.class);
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
                        logger.error("将对象转换为Collention时发生错误。注意：in查询只能做用到Collention中");
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
                GreaterThanOrEqualToQuery beginQueryParam = field.getAnnotation(GreaterThanOrEqualToQuery.class);
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
                        if (value instanceof Short) {
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
                            logger.error("大于等于、小于等于中的Number类型，暂时只支持到Short,Integer,Long");
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