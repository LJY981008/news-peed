package com.example.newspeed.controller;

import com.example.newspeed.dto.UpdateUserNameRequestDto;
import com.example.newspeed.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-peed")
public class UsersController {
    private final UsersService usersService;

    @PutMapping("/modify-name")
    public ResponseEntity<String> modifyName(@Valid @RequestBody UpdateUserNameRequestDto updateRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.updateUsersName(userId, updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-password")
    public ResponseEntity<String> modifyPassword(@Valid @RequestBody UpdatePasswordRequestDto updateRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.updatePassword(userId, updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-intro")
    public ResponseEntity<String> modifyIntro(@Valid @RequestBody UpdateIntroRequestDto updateRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.updateIntro(userId, updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-image")
    public ResponseEntity<String> modiyImage(@Valid @RequestBody UpdateImageRequestDto updateRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.updateImage(userId, updateRequest);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> quitUser(@Valid @RequestBody DeleteUsersRequest deleteRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.deleteUser(userId, deleteRequest);
        return ResponseEntity.ok("success");
    }

    private Long extractUserIdFromCookie(HttpServletRequest request){
        if(request.getCookies() == null) throw new RuntimeException("there's no cookie");
        for(Cookie cookie : request.getCookies()){
            if("userId".equals(cookie.getName())) return Long.valueOf(cookie.getValue());
        }
        throw new RuntimeException("there's no cookie");
    }
}
