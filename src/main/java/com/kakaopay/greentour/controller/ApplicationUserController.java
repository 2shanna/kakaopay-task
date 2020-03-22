package com.kakaopay.greentour.controller;

import com.kakaopay.greentour.domain.ApplicationUser;
import com.kakaopay.greentour.repository.ApplicationUserRepository;
import com.kakaopay.greentour.service.ApplicationUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("users")
public class ApplicationUserController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @PostMapping("sign-up")
    public ResponseEntity signUp(@RequestBody ApplicationUser applicationUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String result = applicationUserDetailsService.save(applicationUser) ? "success" : "username already exists";
        return new ResponseEntity<>("{\"result\": \"" + result + "\"}\n", headers, HttpStatus.CREATED);
    }

    @PostMapping("refresh")
    public ResponseEntity refreshToken(@RequestHeader(value="authorization") String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String refreshedToken = applicationUserDetailsService.createRefreshToken(authorization);
        return new ResponseEntity<>("{\"refreshedToken\": \"" + refreshedToken + "\"}\n", headers, HttpStatus.CREATED);
    }
}
