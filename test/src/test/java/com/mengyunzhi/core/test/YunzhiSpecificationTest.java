package com.mengyunzhi.core.test;

import com.mengyunzhi.core.service.YunzhiSpecification;
import com.mengyunzhi.core.test.entity.Klass;
import com.mengyunzhi.core.test.repository.KlassRepository;
import com.mengyunzhi.core.test.service.KlassService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;

/**
 * 使用生成的Specification，再加入其它条件进行查询。
 * 使用情景：
 * 1. 用户需要3个查询条件进行查询。比如：名称（模糊查询），班级ID（精确查询），专业LIST（IN 查询）
 * 2. YunzhiService只支持名称、班级ID查询，不支持IN查询。
 *
 * 使用步骤：
 * 1. 实现原生的 Specification
 * 2. 将名称（模糊查询），班级ID（精确查询）加入查询条件
 * 3. 调用yunzhiSpecification.toPredicate生成查询条件
 * 4. 加入IN查询条件
 * 5. 返回供综合查询
 * @author panjie
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class YunzhiSpecificationTest {
    @Autowired
    KlassService klassService;
    @Autowired
    KlassRepository klassRepository;


    /**
     * 测试生成的 谓语
     */
    @Test
    public void generatePredicate() {
        // 持久化一个班级，并初始化名称
        final Klass originKlass = this.klassService.getOneSavedKlass();
        originKlass.setName("xxx测试班级名称xxx");
        this.klassRepository.save(originKlass);
        final Klass klass = new Klass();
        final PageRequest pageable = PageRequest.of(0, 2);

        // 同时按 ID（-1） 及 班级名称查询, 断言0条记录
        klass.setId(-1L);
        Specification specification = KlassSpecification.base(klass);
        Page<Klass> klassPage = (Page<Klass>) this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(0);

        // 按正确的ID及班级名称查询，断言1条记
        klass.setId(originKlass.getId());
        specification = KlassSpecification.base(klass);
        klassPage = (Page<Klass>) this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(1);

        // 修改班级名称后，再次查询，断言0条记录
        klass.setId(originKlass.getId());
        originKlass.setName("修改班级名称");
        this.klassRepository.save(originKlass);
        specification = KlassSpecification.base(klass);
        klassPage = this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(0);
    }

    // 班级综合查询
    public static class KlassSpecification {

        /**
         * 当YunzhiService中的查询方法不符合我们的实际需要时
         * 需要手动创建该方法，并调用以生成综合查询规范
         * @param klass 实体
         * @return
         */
        public static Specification<Klass> base(final Klass klass) {
            /**
             * 直接return，则表示该方法为Specification接口中唯一需要必须实现的方法
             * 具体到此接口中，为：Predicate toPredicate(Root<T> var1, CriteriaQuery<?> var2, CriteriaBuilder var3);
             */
            return (Specification<Klass>) (root, criteriaQuery, criteriaBuilder) -> {
                // 先获取YunzhiSpecification按传入的klass自动生成的查询条件
                final YunzhiSpecification<Klass> yunzhiSpecification = new YunzhiSpecification<>(klass);
                Predicate predicate = yunzhiSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);

                // 再加入NAME其它的查询条件。
                final Predicate predicate1 = criteriaBuilder.like(root.get("name").as(String.class), "%测试班级名称%");
                predicate = criteriaBuilder.and(predicate, predicate1);
                return predicate;
            };
        }
    }
}