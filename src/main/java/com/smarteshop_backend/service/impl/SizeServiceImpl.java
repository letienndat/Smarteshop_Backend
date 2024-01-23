package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Size;
import com.smarteshop_backend.repository.SizeRepository;
import com.smarteshop_backend.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public boolean checkSize(Double size) {
        return sizeRepository.existsBySize(size);
    }

    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public Size findBySize(Double size) {
        return sizeRepository.findBySize(size);
    }
}
