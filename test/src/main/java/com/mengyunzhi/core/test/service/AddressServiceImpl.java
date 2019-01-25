package com.mengyunzhi.core.test.service;

import com.mengyunzhi.core.test.entity.Address;
import com.mengyunzhi.core.test.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 地址
 * @author panjie
 */
@Service
public class AddressServiceImpl implements AddressService {
    private final
    AddressRepository addressRepository;    // 地址

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getOneSavedAddress() {
        Address address = new Address();
        address.setCity(CommonService.getRandomStringByLength(4) + "测试城市" + CommonService.getRandomStringByLength(4));
        address.setCounty(CommonService.getRandomStringByLength(4) + "测试县" + CommonService.getRandomStringByLength(4));
        address.setNum(10);
        addressRepository.save(address);
        return address;
    }
}
