package com.smarteshop_backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public interface FileStorageService {
    String storeFile(MultipartFile file, String subfolder) throws IOException;
    Resource loadFileAsResource(String fileName, String subfolder);
    Path loadFileAsPath(String fileName, String subfolder);
}