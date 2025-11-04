package ru.netology.diplom.service;

import org.springframework.stereotype.Service;
import ru.netology.diplom.dto.AuthResponse;
import ru.netology.diplom.exceptions.UnauthorizedStorage;
import ru.netology.diplom.model.ActiveToken;
import ru.netology.diplom.model.User;
import ru.netology.diplom.repostory.ActiveTokenRepository;
import ru.netology.diplom.repostory.UserRepository;

import java.util.Optional;


@Service
public class AuthUserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtTokenProvider;
    private final ActiveTokenRepository activeTokenRepository;


    public AuthUserService(UserRepository userRepository, JwtUtil jwtTokenProvider, ActiveTokenRepository activeTokenRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.activeTokenRepository = activeTokenRepository;
    }

    public AuthResponse authenticate(String login, String password) {
        User user = userRepository.findByLogin(login);

        if (user == null) throw  new UnauthorizedStorage("Unauthorized error");

        if (!user.getPassword().equals(password)) throw  new UnauthorizedStorage("Unauthorized error");;

        Optional<ActiveToken> existing = activeTokenRepository.findByUser(user);
        if (existing.isPresent()) {
            return new AuthResponse(existing.get().getToken());
        }
        String token = jwtTokenProvider.generateToken(login);
        activeTokenRepository.save(new ActiveToken(token, user));
        return new AuthResponse(token);
    }


    public void logout(String token) {
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(activeTokenRepository.existsById(token)) {
            activeTokenRepository.deleteById(token);
        }else{
            throw  new UnauthorizedStorage("Unauthorized error");
        }
    }

}
