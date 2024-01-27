package com.smarteshop_backend.controller;

import com.smarteshop_backend.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Get image local
     *
     * @param subfolder
     * @param fileName
     * @return
     */
    @GetMapping("/{subfolder}/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String subfolder, @PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName, subfolder);

        String contentType = null;
        try {
            contentType = Files.probeContentType(Path.of(resource.getURI()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}