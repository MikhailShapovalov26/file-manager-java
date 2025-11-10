package ru.netology.diplom.service.minio;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.exceptions.DocumentStorageException;

import java.io.File;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3FileService {

    private final MinioClient minioClient;
    private final String bucketName;

    public S3FileService(MinioClient minioClient, @Value("${minio.bucket.name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public void uploadFileMinio(final MultipartFile file) throws DocumentStorageException {
        try {
            List<String> filesListBucket = getFilesFromMinioBucket();
            boolean fileExists = filesListBucket.stream().toList().contains(file.getOriginalFilename());
            String objectName = "admin/" + file.getOriginalFilename();

            if (!fileExists) {
                String fileName = file.getOriginalFilename();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()

                );
            }
            throw new DocumentStorageException("Error uploading file");
        } catch (Exception e) {
            throw new DocumentStorageException("Error uploading file");
        }
    }

    public List<String> getFilesFromMinioBucket() {
        List<String> fileNames = new ArrayList<>();
        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build()
        );
        items.forEach(itemResult -> {
            try {
                Item item = itemResult.get();
                fileNames.add(item.objectName());
            } catch (Exception e) {
                throw new DocumentStorageException("Error uploading file");
            }
        });
        System.out.println(fileNames);
        return fileNames;
    }

    public MediaType getMediaType(final String filename) {
        try {
            File file = new File(filename);
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String mimeType = fileNameMap.getContentTypeFor(file.getName());

            return MediaType.parseMediaType(mimeType);
        }catch (Exception e) {
            throw new DocumentStorageException("Error uploading file");
        }

    }

    public Resource download(String fileName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build()
            );
            return new InputStreamResource(inputStream);
        } catch (Exception e){
            throw new DocumentStorageException("Error uploading file");
        }
    }
}
