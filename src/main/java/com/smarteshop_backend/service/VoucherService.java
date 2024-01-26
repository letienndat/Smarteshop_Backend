package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Voucher;
import org.springframework.stereotype.Service;

@Service
public interface VoucherService {
    boolean existsByCode(String code);
    Voucher findByCode(String code) throws Exception;
    Voucher save(Voucher voucher);
    int checkVoucher(Long idUser, String code) throws Exception;
}
