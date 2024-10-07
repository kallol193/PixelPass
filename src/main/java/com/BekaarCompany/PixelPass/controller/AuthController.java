package com.BekaarCompany.PixelPass.controller;

import com.BekaarCompany.PixelPass.entity.AuthRequest;
import com.BekaarCompany.PixelPass.entity.AuthResponse;
import com.BekaarCompany.PixelPass.entity.User;
import com.BekaarCompany.PixelPass.jwt.JWTUtils;
import com.BekaarCompany.PixelPass.service.AuthService;
import com.BekaarCompany.PixelPass.service.securityservice.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtil;

    @Autowired
    private AuthService authService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//            return ResponseEntity.ok("Authentication successful");
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        authService.registerUser(user);
        return ResponseEntity.ok(String.format("User Registered sucessfully : %s", user));
    }
}