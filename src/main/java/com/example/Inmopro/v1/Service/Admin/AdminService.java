package com.example.Inmopro.v1.Service.Admin;

import com.example.Inmopro.v1.Controller.Auth.AuthResponse;
import com.example.Inmopro.v1.Dto.Auth.RegisterRequest;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request){
        Users user = Users.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role_id(1)
                .build();

        usersRepository.save(user);

        return "User created";
    }
}
