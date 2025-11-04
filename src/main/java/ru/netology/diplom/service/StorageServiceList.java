package ru.netology.diplom.service;

import org.springframework.stereotype.Service;
import ru.netology.diplom.dto.ListAllFile;
import ru.netology.diplom.exceptions.UnauthorizedStorage;
import ru.netology.diplom.model.ActiveToken;
import ru.netology.diplom.model.User;
import ru.netology.diplom.repostory.ActiveTokenRepository;
import ru.netology.diplom.repostory.StorageRepository;
import ru.netology.diplom.repostory.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StorageServiceList {

    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final ActiveTokenRepository activeTokenRepository;

    public StorageServiceList(StorageRepository storageRepository,
                              UserRepository userRepository,
                              ActiveTokenRepository activeTokenRepository) {
        this.storageRepository = storageRepository;
        this.userRepository = userRepository;
        this.activeTokenRepository = activeTokenRepository;
    }

    public List<ListAllFile> list(String token, int limit) {
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Optional<ActiveToken> activeToken = activeTokenRepository.findById(token);
        if (activeToken.isEmpty()) {
            throw new UnauthorizedStorage("Token not valid");
        }
        Long userId = activeToken.get().getUser().getId();
        User user = userRepository.findById(userId).get();

        return  storageRepository.findAllByUser(user)
                .stream()
                .limit(limit)
                .toList();
    }
}
