package com.mengyunzhi.core.demo.repository;


import com.mengyunzhi.core.demo.entity.Address;
import org.springframework.data.repository.CrudRepository;

/**
 * 地址
 * @author panjie
 */
public interface AddressRepository extends CrudRepository<Address, Long> {
}
