package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.entity.Voucher;
import com.smarteshop_backend.repository.VoucherRepository;
import com.smarteshop_backend.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public boolean existsByCode(String code) {
        return voucherRepository.existsByCode(code);
    }

    @Override
    public Voucher findByCode(String code) throws Exception {
        return voucherRepository.findByCode(code).orElseThrow(() -> new Exception("Cannot find voucher with code = " + code));
    }

    @Override
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public int checkVoucher(Long idUser, String code) throws Exception {
        /**
         * Not exists voucher code
         */
        if (!existsByCode(code)) return 0;
        Voucher voucher = findByCode(code);
        List<User> users = voucher.getUsers();
        for (User user : users) {
            /**
             * Voucher code already exists in list voucher of user
             */
            if (user.getId().equals(idUser)) return 2;
        }

        /**
         * Already voucher but voucher not exists in list voucher of user
         */
        return 1;
    }
}
