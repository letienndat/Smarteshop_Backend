package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Size;
import org.springframework.stereotype.Service;

@Service
public interface SizeService {
    boolean checkSize(Double size);
    Size save(Size size);
    Size findBySize(Double size);
}
