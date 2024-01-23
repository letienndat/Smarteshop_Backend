package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ImageDemo;
import com.smarteshop_backend.repository.ImageDemoRepository;
import com.smarteshop_backend.service.ImageDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageDemoServiceImpl implements ImageDemoService {
    @Autowired
    private ImageDemoRepository imageDemoRepository;

    @Override
    public ImageDemo save(ImageDemo imageDemo) {
        return imageDemoRepository.save(imageDemo);
    }
}
