package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ImageDemo;
import org.springframework.stereotype.Service;

@Service
public interface ImageDemoService {
    ImageDemo save(ImageDemo imageDemo);
}
