package com.huangou.upload;

import com.cheer.huangou.upload.HuangouUploadService;
import com.cheer.huangou.upload.service.UploadService;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HuangouUploadService.class)
public class FdfsTest {

    @Autowired  //注入storage客户端
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("C:\\Users\\HP\\upload\\1564818769308-hhhhcwu.png");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream (file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    //带缩略图的上传方法
    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("C:\\Users\\HP\\upload\\1564818769308-hhhhcwu.png");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}