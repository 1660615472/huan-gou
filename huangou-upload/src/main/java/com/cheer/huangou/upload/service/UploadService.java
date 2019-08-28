package com.cheer.huangou.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangjie
 * @title:
 * @data
 */


public interface UploadService {
    String upload(MultipartFile file);
}
