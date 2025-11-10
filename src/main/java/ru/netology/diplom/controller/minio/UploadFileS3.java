package ru.netology.diplom.controller.minio;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.service.minio.S3FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class UploadFileS3 {
    private final S3FileService s3FileService;

    public UploadFileS3(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try{
            s3FileService.uploadFileMinio(file);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getFileList")
    public ResponseEntity<List<String>> getFiles() {
        return ResponseEntity.ok(s3FileService.getFilesFromMinioBucket());
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource>  downloadFile(@PathVariable String fileName) throws IOException {
        Resource file = s3FileService.download(fileName);
        return ResponseEntity.ok()
                .headers(h ->{
                    h.setContentType(s3FileService.getMediaType(fileName));
                    h.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
                })
                .body(file);
    }
}
