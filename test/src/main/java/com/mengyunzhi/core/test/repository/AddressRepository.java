package com.mengyunzhi.core.test.repository;


import com.mengyunzhi.core.test.entity.Address;
import org.springframework.data.repository.CrudRepository;

/**
 * 地址
 * @author panjie
 */
public interface AddressRepository extends CrudRepository<Address, Long> {
}
