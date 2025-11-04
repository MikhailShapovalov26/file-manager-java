package ru.netology.diplom.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.diplom.dto.ListAllFile;
import ru.netology.diplom.service.StorageServiceList;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {
    private final StorageServiceList storageServiceList;

    public ListController(StorageServiceList storageServiceList) {
        this.storageServiceList = storageServiceList;
    }


    @GetMapping
    @CrossOrigin
    public List<ListAllFile> listAllFile(@RequestHeader("auth-token") String token,
                                         @RequestParam("limit") int limit) throws IOException {
        return storageServiceList.list(token, limit);
    }
}
