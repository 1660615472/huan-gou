package com.cheer.huangou.upload.service.impl;

import com.cheer.huangou.upload.service.UploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
public class UploadServiceImpl implements UploadService {
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Override
    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                log.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File(System.getProperty("user.home") + "/upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String newFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // 2.2、保存图片
            file.transferTo(new File(dir, newFileName));

            // 2.3、拼接图片地址
            String url = "http://image.huangou.com/" + newFileName;

            return url;
        } catch (Exception e) {
            return null;
        }
    }
}
