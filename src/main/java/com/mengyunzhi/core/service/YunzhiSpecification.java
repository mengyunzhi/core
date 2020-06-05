package com.mengyunzhi.core.service;

import com.mengyunzhi.core.annotation.query.*;
import com.mengyunzhi.core.entity.YunzhiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author panjie
 * 综合查询构造器
 */
public class YunzhiSpecification<O> implements Specification<O> {
    private final static Logger logger = LoggerFactory.getLogger(YunzhiSpecification.class);
    private static final long serialVersionUID = 3132543250940999651L;
    private Predicate predicate = null;
    private CriteriaBuilder criteriaBuilder;
    private final YunzhiEntity entity;       // 实体

    public YunzhiSpecification(final YunzhiEntity entity) {
        this.entity = entity;
    }

    /**
     * 设置and谓语.注意，这里只能设置and关系的谓语，如果谓语为OR，则需要手动设置
     *
     * @param predicate 查询谓语
     */
    private void andPredicate(final Predicate predicate) {
        if (null != predicate) {
            if (null == this.predicate) {
                this.predicate = predicate;
            } else {
                this.predicate = this.criteriaBuilder.and(this.predicate, predicate);
            }
        }
    }

    private void generatePredicate(final Object entity, final From<O, ?> root) {
        if (entity instanceof YunzhiEntity) {
            logger.debug("继承了YunzhiEntity实体，则先判断是否传入了ID。传入ID，则直接对ID及deleted字段进行查询");
            final YunzhiEntity yunzhiEntity = (YunzhiEntity) entity;
            if (yunzhiEntity.getId() != null) {
                this.andPredicate(this.criteriaBuilder.equal(root.get("id"), yunzhiEntity.getId()));
                if (yunzhiEntity.getDeleted() != null) {
                    this.andPredicate(this.criteriaBuilder.equal(root.get("deleted"), yunzhiEntity.getDeleted()));
                }

                return;
            }
        }

        logger.debug("反射字段，按字段类型，进行综合查询");
        final List<Field> fields = CommonService.getAllModelFields(entity.getClass());
        try {
            for (final Field field : fields) {
                logger.debug("设置字段可见，并获取实体值");
                field.setAccessible(true);
                final Object value = field.get(entity);

                if (value != null) {
                    logger.debug("检测final或static字段");
                    if (YunzhiSpecification.checkFinalOrStatic(field)) {
                        continue;
                    }

                    final String name = field.getName();
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

                    if (this.equalTo(root, field, value)) {
                        logger.debug("执行了equal to 注解查询");
                        continue;
                    }

                    if (this.isNull(root, field, value)) {
                        logger.debug("执行了isNull");
                        continue;
                    }

                    if (this.isNotNull(root, field, value)) {
                        logger.debug("执行了isNull");
                        continue;
                    }

                    if (field.getAnnotation(Transient.class) != null) {
                        logger.debug("该字段未持久化，跳过");
                        continue;
                    }

                    // 按字段类型进行查询
                    if (value instanceof Boolean) {
                        logger.debug("布尔值");
                        final Boolean booleanValue = ((Boolean) value);
                        this.andPredicate(this.criteriaBuilder.equal(root.get(name).as(Boolean.class), booleanValue));
                    } else if (value instanceof String) {
                        logger.debug("字符串则进行模糊查询");
                        final String stringValue = ((String) value);
                        if (!stringValue.isEmpty()) {
                            this.andPredicate(this.criteriaBuilder.like(root.get(name).as(String.class), "%" + stringValue + "%"));
                        }
                    } else if (value instanceof Number || value instanceof Calendar || value instanceof Date) {
                        logger.debug("如果为number，则进行精确或范围查询");
                        this.andPredicate(this.criteriaBuilder.equal(root.get(name).as(value.getClass()), value));
                    } else if (value instanceof YunzhiEntity) {
                        logger.debug("是实体类");
                        final YunzhiEntity yunZhiEntity = (YunzhiEntity) value;
                        if (yunZhiEntity.getId() != null) {
                            logger.debug("对应的ManyToOne，加入了id, 则按ID查询");
                            this.andPredicate(this.criteriaBuilder.equal(root.join(name).get("id"), yunZhiEntity.getId()));
                        } else {
                            logger.debug("未加入id, 则进行Join查询");
                            this.generatePredicate(value, root.join(name));
                        }

                    } else if (value instanceof Collection) {
                        final Collection<?> collectionValue = (Collection<?>) value;
                        if (collectionValue.size() > 0) {
                            logger.warn("暂不支持一对多，多对多查询");
                            // todo: 一对多，多对多查询
                        }
                    } else {
                        final Class<?> clazz = value.getClass();
                        if (clazz.isAnnotationPresent(Embeddable.class)) {
                            logger.debug("为内部Embeddable");
                            this.generatePredicate(value, root.join(name));
                        } else {
                            logger.error("综合查询暂不支持传入的数据类型:" + name + "->" + field.toString() + " " + field.getClass().getName());
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不为null
     *
     * @param root  根查询
     * @param field 字段
     * @param value 值
     * @return boolean 发现注解true
     */
    private boolean isNotNull(From<O, ?> root, Field field, Object value) {
        final IsNotNull isNotNull = field.getAnnotation(IsNotNull.class);
        if (isNotNull != null && value != null && !value.equals(false)) {
            String name = isNotNull.name();
            if ("".equals(name)) {
                name = field.getName();
            }
            this.andPredicate(this.criteriaBuilder.isNotNull(root.get(name)));
        }

        return isNotNull != null;
    }

    /**
     * 为null
     *
     * @param root  根查询
     * @param field 字段
     * @param value 值
     * @return boolean 发现注解true
     */
    private boolean isNull(From<O, ?> root, Field field, Object value) {
        final IsNull isNull = field.getAnnotation(IsNull.class);
        if (isNull != null && value != null && !value.equals(false)) {
            String name = isNull.name();
            if ("".equals(name)) {
                name = field.getName();
            }
            this.andPredicate(this.criteriaBuilder.isNull(root.get(name)));
        }

        return isNull != null;
    }

    /**
     * 完全等于注解，主要用于一些需要进行精确查询的字符串类型
     * 使用其注解了。
     *
     * @param root  查询根
     * @param field 字段
     * @param value 值
     * @return boolean 发现注解true
     */
    private boolean equalTo(From<O, ?> root, Field field, Object value) {
        // 查找开始与结束的注解
        final EqualTo beginQueryParam = field.getAnnotation(EqualTo.class);

        // 有注解，且值不为null，也不为 空 时，进行查询
        if (beginQueryParam != null && value != null && !"".equals(value)) {
            String name = beginQueryParam.name();
            if ("".equals(name)) {
                name = field.getName();
            }

            this.andPredicate(this.criteriaBuilder.equal(root.get(name), value));
        }

        return beginQueryParam != null;
    }

    private static boolean checkFinalOrStatic(final Field field) {
        if (Modifier.isFinal(field.getModifiers())) {
            logger.debug("字段类型为final");
            return true;
        }

        if (Modifier.isStatic(field.getModifiers())) {
            logger.debug("字段类型为static");
            return true;
        }
        return false;
    }

    /**
     * in 查询
     *
     * @param root  根查询实体
     * @param field 字段
     * @param value 值
     * @return 进行了in直询，true;未进行in查询,false.
     */
    private Boolean inQuery(final From<O, ?> root, final Field field, final Object value) {
        // 获取in注解
        final In inQueryParam = field.getAnnotation(In.class);
        if (inQueryParam != null) {
            logger.debug("存在@InQueryParam注解");
            final String inQueryName = inQueryParam.name();
            /*
             * In查询相关代码
             * https://blog.csdn.net/jiangyu1013/article/details/79056231
             * Expression：查询表达式
             * 源码中支持isNull，isNotNull，in，返回值为根据表达式生成的谓语
             */
            logger.debug("执行In查询");
            try {
                final Collection<?> collectionValue = (Collection<?>) value;
                final Expression<Object> expression = root.get(inQueryName);
                this.andPredicate(expression.in(collectionValue));
            } catch (final ClassCastException e) {
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
     *
     * @param root  基实体
     * @param value 值
     * @param field 字段
     * @return Boolean 是否执行了范围查询
     */
    private Boolean beginOrEndQuery(final From<O, ?> root, final Object value, final Field field) {
        String name = field.getName();
        // 初始化两个界限的变量
        Boolean isBegin = false;
        Boolean isEnd = false;

        // 查找开始与结束的注解
        final GreaterThanOrEqualTo beginQueryParam = field.getAnnotation(GreaterThanOrEqualTo.class);
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
                    final Byte byteValue = (Byte) value;
                    if (isBegin) {
                        this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Byte.class), byteValue));
                    } else {
                        this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Byte.class), byteValue));
                    }
                } else if (value instanceof Short) {
                    final Short shortValue = (Short) value;
                    if (isBegin) {
                        this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Short.class), shortValue));
                    } else {
                        this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Short.class), shortValue));
                    }
                } else if (value instanceof Integer) {
                    final Integer integerValue = (Integer) value;
                    if (isBegin) {
                        this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Integer.class), integerValue));
                    } else {
                        this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Integer.class), integerValue));
                    }
                } else if (value instanceof Long) {
                    final Long longValue = (Long) value;
                    if (isBegin) {
                        this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Long.class), longValue));
                    } else {
                        this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Long.class), longValue));
                    }
                } else {
                    logger.error("大于等于、小于等于中的Number类型，暂时只支持到Byte, Short,Integer,Long");
                }
            } else if (value instanceof Calendar) {
                final Calendar calendarValue = (Calendar) value;
                if (isBegin) {
                    this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Calendar.class), calendarValue));
                } else {
                    this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Calendar.class), calendarValue));
                }
            } else if (value instanceof Date) {
                logger.debug("java.util.date类型");
                final Date dateValue = (Date) value;
                if (isBegin) {
                    this.andPredicate(this.criteriaBuilder.greaterThanOrEqualTo(root.get(name).as(Date.class), dateValue));
                } else {
                    this.andPredicate(this.criteriaBuilder.lessThanOrEqualTo(root.get(name).as(Date.class), dateValue));
                }
            }
        }

        return isBegin || isEnd;
    }

    @Override
    public Predicate toPredicate(final Root<O> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
        // 设置CriteriaBuilder，用于合并谓语
        this.criteriaBuilder = criteriaBuilder;
        this.generatePredicate(this.entity, root);
        return this.predicate;
    }
}
