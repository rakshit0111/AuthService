package org.example.services;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repositories.RefreshTokenRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service

public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createToken(String username)
    {
        UserInfo userInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder().userInfo(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000000)).build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken)
    {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0)
        {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken()+"Refresh token is expired.Login again.");
        }

        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token)
    {
        return refreshTokenRepository.findByToken(token);
    }
}
