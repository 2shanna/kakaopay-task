package com.kakaopay.greentour.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakaopay.greentour.domain.ApplicationUser;
import com.kakaopay.greentour.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.kakaopay.greentour.security.SecurityConstants.EXPIRATION_TIME;
import static com.kakaopay.greentour.security.SecurityConstants.SECRET;
import static com.kakaopay.greentour.security.SecurityConstants.TOKEN_PREFIX;
import static java.util.Collections.emptyList;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public boolean save(ApplicationUser applicationUser) {
        if (applicationUserRepository.findByUsername(applicationUser.getUsername()).isPresent()) {
            return false;
        }
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> applicationUserOpt = applicationUserRepository.findByUsername(username);

        if (applicationUserOpt.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        ApplicationUser applicationUser= applicationUserOpt.get();

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }

    public String getUsernameFromToken(String authorization) throws JWTVerificationException {
        String currentToken = authorization.replace(TOKEN_PREFIX, "");

        Algorithm algorithm = Algorithm.HMAC512(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decodedJwt = verifier.verify(currentToken);

        return decodedJwt.getSubject();
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }

}
