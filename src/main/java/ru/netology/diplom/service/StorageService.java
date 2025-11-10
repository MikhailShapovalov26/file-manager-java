package ru.netology.diplom.service;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.exceptions.DocumentStorageException;
import ru.netology.diplom.exceptions.ErrorDeleteFile;
import ru.netology.diplom.exceptions.UnauthorizedStorage;
import ru.netology.diplom.model.Storage;
import ru.netology.diplom.model.User;
import ru.netology.diplom.repostory.ActiveTokenRepository;
import ru.netology.diplom.repostory.StorageRepository;
import ru.netology.diplom.storage.StorageProperties;

import javax.management.modelmbean.XMLParseException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StorageService {
    private final Path rootLocation;

    private final StorageRepository storageRepository;
    private final ActiveTokenRepository activeTokenRepository;

    public StorageService(StorageProperties properties,
                          StorageRepository storageRepository,
                          ActiveTokenRepository activeTokenRepository) {
        this.rootLocation = Paths.get(properties.getRootLocation());
        this.storageRepository = storageRepository;
        this.activeTokenRepository = activeTokenRepository;
    }



    public void save(MultipartFile file, String token) throws DocumentStorageException {
        User userUploadFile = searchUserActiveSession(token);

        Storage storageEntity = new Storage();

        if (storageRepository.findByUserAndFileName(userUploadFile, file.getOriginalFilename()).isPresent()) {
            throw new DocumentStorageException("File already exists for this user");
        }

        if (file.isEmpty()) {
            throw new DocumentStorageException("Error input data");
        }
        Path destinationFile = this.rootLocation.resolve(
                        Paths.get(
                                file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new DocumentStorageException("Error input data");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
            storageEntity.setUser(userUploadFile);
            storageEntity.setFileName(file.getOriginalFilename());
            storageEntity.setUploadDir(rootLocation.toString());
            storageEntity.setSizeToByte((int) file.getSize());
            storageRepository.save(storageEntity);
        } catch (IOException e) {
            throw new DocumentStorageException("Error input data");
        }

    }

    // нужна чтоб при запуске удалять все старые файлы
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    // Создание директории при старте приложения
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление файла по имени и удаление записи из базы данных
    public boolean delete(String fileName, String token) throws IOException {
        User userDeleteFile = searchUserActiveSession(token);
        if (storageRepository.findByUserAndFileName(userDeleteFile, fileName).isEmpty()) {
            throw new DocumentStorageException("Error input data");
        } else {
            Path file = rootLocation.resolve(fileName);
            try {
                Files.deleteIfExists(file);
                storageRepository.delete(storageRepository.findByUserAndFileName(userDeleteFile, fileName).orElseThrow());
                return true;
            } catch (Exception e) {
                throw new ErrorDeleteFile("Error delete file");
            }
        }
    }

    // Поиск, что юзер активировал сессию
    private User searchUserActiveSession(String token) {
        User user;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (activeTokenRepository.existsById(token)) {
            user = activeTokenRepository.findById(token).get().getUser();
        } else {
            throw new UnauthorizedStorage("Unauthorized error");
        }
        return user;
    }

    // Отправка раннее загруженного файла пользователю
    public InputStream downloadFileUser(String fileName, String token) throws IOException {
        User userUploadFile = searchUserActiveSession(token);
        Path file = rootLocation.resolve(fileName);
        if (!Files.exists(file)) {
            throw new DocumentStorageException("Error input data");
        }
        try {
            if (storageRepository.findByUserAndFileName(userUploadFile, fileName).isPresent()) {
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file.toFile()));
                return inputStream;
            }
        } catch (Exception e) {
            throw new DocumentStorageException("Error input data");
        }
        return null;
    }

    // Изменение имени файла на новый
    public void changeFileName(String fileName, String name, String token) {
        User userChangeFileName = searchUserActiveSession(token);
        Path fileOldName = rootLocation.resolve(fileName);
        Path fileNewName = rootLocation.resolve(name);
        if (!Files.exists(fileOldName)) {
            throw new DocumentStorageException("Error input data");
        }
        try {
            if (storageRepository.findByUserAndFileName(userChangeFileName, fileName).isPresent()) {
                Files.move(fileOldName, fileNewName);
                Storage storageEntity = storageRepository.findByUserAndFileName(userChangeFileName, fileName).get();
                storageEntity.setFileName(name);
                storageRepository.save(storageEntity);
            } else {
                throw new DocumentStorageException("Error input data");
            }

        } catch (IOException e) {
            throw new ErrorDeleteFile("Error upload file");
        }
    }


}
