package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MinIoTest {
    public static void main(String[] args) {
        FileInputStream fileInputStream=null;

        try {
            fileInputStream = new FileInputStream("D:\\program\\java\\heima-leadnews\\heima-leadnews-test\\minio-demo\\src\\main\\java\\com\\heima\\minio\\text\\list.html");
            //1.创建minio 链接 客户端
            MinioClient minioClient = MinioClient.builder().credentials("minioadmin", "minioadmin").endpoint("http://localhost:9000").build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("list.html") //文件名
                    .contentType("text/html")//文件类型
                    .bucket("leadnews")//桶名称，与minio服务创建的名称一致
                    .stream(fileInputStream,fileInputStream.available(),-1)//文件流
                    .build();
            minioClient.putObject(putObjectArgs);

            //访问路径
            System.out.println("http://localhost:9000/leadnews/list.html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
