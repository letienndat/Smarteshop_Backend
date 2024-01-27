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
public class FileStorageService {
    @Value("${app.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subfolder) throws IOException {
        Path uploadPath = Paths.get(uploadDir, subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String newFileName = generateUniqueFileName(file.getOriginalFilename());
        Path destinationPath = uploadPath.resolve(StringUtils.cleanPath(newFileName));

        Files.copy(file.getInputStream(), destinationPath);

        return "/" + subfolder + "/" + newFileName;
    }

    public Resource loadFileAsResource(String fileName, String subfolder) {
        try {
            Path filePath = loadFileAsPath(fileName, subfolder);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load the file: " + fileName, e);
        }
    }

    public Path loadFileAsPath(String fileName, String subfolder) {
        return Paths.get(uploadDir, subfolder, StringUtils.cleanPath(fileName)).normalize();
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }
}