package com.BekaarCompany.PixelPass.service;

import com.BekaarCompany.PixelPass.entity.User;
import com.BekaarCompany.PixelPass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void registerUser(User user) {
      userRepository.save(user);
    }
}
