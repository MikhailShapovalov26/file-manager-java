package ru.netology.diplom.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.diplom.dto.AuthRequest;
import ru.netology.diplom.dto.AuthResponse;
import ru.netology.diplom.model.User;
import ru.netology.diplom.service.AuthUserService;
import ru.netology.diplom.service.JwtUtil;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping()
public class AuthController {
    private final AuthUserService authUserService;

    public AuthController(AuthUserService authUserService, JwtUtil jwtUtil) {
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    @CrossOrigin
    public Map<String, Object> login(@RequestBody AuthRequest req) {
        AuthResponse response = authUserService.authenticate(req.getLogin(), req.getPassword());
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad credentials");
        }
        return Collections.singletonMap("auth-token", response.getToken());
    }

    @PostMapping("/logout")
    @CrossOrigin
    public void logout(@RequestHeader("auth-token") String token) {
        authUserService.logout(token);

    }

}
