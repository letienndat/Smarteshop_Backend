package com.smarteshop_backend.util.impl;

import com.smarteshop_backend.util.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${app.upload-dir}")
    private String uploadDir;

    /**
     * Save file (image)
     *
     * @param file
     * @param subfolder
     * @return
     * @throws IOException
     */
    @Override
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

    /**
     * Load file as resource object
     *
     * @param fileName
     * @param subfolder
     * @return
     */
    @Override
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

    /**
     * Load file as path object
     *
     * @param fileName
     * @param subfolder
     * @return
     */
    @Override
    public Path loadFileAsPath(String fileName, String subfolder) {
        return Paths.get(uploadDir, subfolder, StringUtils.cleanPath(fileName)).normalize();
    }

    /**
     * Generate unique filename with UUID
     *
     * @param originalFileName
     * @return
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }
}