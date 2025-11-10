package ru.netology.diplom.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String minioUrl;

    @Value("${minio.secret-key}")
    private String minioSecretKey;
    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioSecretKey).build();
    }
}
