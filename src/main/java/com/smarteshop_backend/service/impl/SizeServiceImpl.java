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

    /**
     * Check exists size number
     *
     * @param size
     * @return
     */
    @Override
    public boolean checkSize(Double size) {
        return sizeRepository.existsByNumber(size);
    }

    /**
     * Save size
     *
     * @param size
     * @return
     */
    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    /**
     * Find size by number size
     *
     * @param size
     * @return
     * @throws Exception
     */
    @Override
    public Size findBySize(Double size) throws Exception {
        return sizeRepository.findByNumber(size).orElseThrow(() -> new Exception("Cannot find size with size = " + size));
    }
}
