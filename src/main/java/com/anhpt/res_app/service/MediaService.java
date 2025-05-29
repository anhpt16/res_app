package com.anhpt.res_app.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface MediaService {
    ResponseEntity<Resource> loadMedia(String fileName);
    String getContentType(String fileName);
} 