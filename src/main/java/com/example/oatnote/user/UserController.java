package com.example.oatnote.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.oatnote.user.models.LoginUserResponse;
import com.example.oatnote.user.models.UserRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        userService.signUp(userRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        LoginUserResponse loginResponse = userService.login(userRequest);
        if (loginResponse != null) {
            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(401).body("로그인 실패");
        }
    }
}
