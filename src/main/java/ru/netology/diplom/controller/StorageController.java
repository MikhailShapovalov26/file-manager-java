package ru.netology.diplom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.netology.diplom.dto.RenameFileName;
import ru.netology.diplom.service.StorageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class StorageController {
    private final StorageService storageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }


    @PostMapping
    @CrossOrigin
    public String file(@RequestParam("file") MultipartFile file,
                       @RequestHeader("auth-token") String token,
                       RedirectAttributes redirectAttributes) {
        storageService.save(file, token);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @DeleteMapping
    @CrossOrigin
    public void delete(@RequestParam("filename") String fileName,
                       @RequestHeader("auth-token") String token) throws IOException {
        if (storageService.delete(fileName, token)) {
            System.out.println("Файл удален " + fileName);
        }
    }

    @GetMapping
    @CrossOrigin
    public void getOutputStream(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam("filename") String fileName,
                                @RequestHeader("auth-token") String token
    ) throws IOException {
        FileCopyUtils.copy(storageService.downloadFileUser(fileName, token), response.getOutputStream());
    }

    @PutMapping
    @CrossOrigin
    public void putFileName(@RequestParam("filename") String fileName,
                            @RequestBody RenameFileName renameFileName,
                            @RequestHeader("auth-token") String token
    ) throws IOException {
        storageService.changeFileName(fileName, renameFileName.getFileName() , token);

    }

}
