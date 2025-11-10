package ru.netology.diplom.controller.minio;

import io.minio.errors.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.diplom.model.User;
import ru.netology.diplom.service.minio.MinioCreateFolder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/auth")
public class UserGatewayService {

    private final MinioCreateFolder minioCreateFolder;


    public UserGatewayService(MinioCreateFolder minioCreateFolder) {
        this.minioCreateFolder = minioCreateFolder;
    }

    @PostMapping
    public void authorization(@RequestBody User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioCreateFolder.createFolder(user);
    }
}
