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

    @Test
    public void generatePredicate() {
        Klass originKlass = klassService.getOneSavedKlass();
        Klass klass = new Klass();
        PageRequest pageable = PageRequest.of(0, 2);

        klass.setId(-1L);
        Specification specification = KlassSpecification.base(klass);
        Page<Klass> klassPage = (Page<Klass>) this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(0);

        klass.setId(originKlass.getId());
        specification = KlassSpecification.base(klass);
        klassPage = (Page<Klass>) this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(1);

        klass.setId(originKlass.getId());
        originKlass.setName("修改班级名称");
        klassRepository.save(originKlass);
        specification = KlassSpecification.base(klass);
        klassPage = this.klassRepository.findAll(specification, pageable);
        Assertions.assertThat(klassPage.getTotalElements()).isEqualTo(0);
    }

    public static class KlassSpecification {

        public static Specification<Klass> base(Klass klass) {
            return (Specification<Klass>) (root, criteriaQuery, criteriaBuilder) -> {
                // 先获取YunzhiSpecification自动生成的查询条件
                YunzhiSpecification<Klass, Long> yunzhiSpecification = new YunzhiSpecification<>(klass);
                Predicate predicate = yunzhiSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);

                // 再加入其它的查询条件。比如加入NAME查询
                Predicate predicate1 = criteriaBuilder.like(root.get("name").as(String.class), "%测试班级名称%");
                predicate = criteriaBuilder.and(predicate, predicate1);
                return predicate;
            };
        }
    }
}