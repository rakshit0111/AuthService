package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entities.RefreshToken;
import org.example.model.UserInfoDto;
import org.example.response.JwtResponseDto;
import org.example.services.JwtService;
import org.example.services.RefreshTokenService;
import org.example.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private final JwtService jwtService;

    @PostMapping("/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto)
    {
        try
        {
            Boolean isSignUped = userDetailsServiceImp.signUpUser(userInfoDto);

            if(Boolean.FALSE.equals(isSignUped))
            {
                return  new ResponseEntity<>("User already exists",HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(),HttpStatus.OK);
        }
        catch (Exception e)
        {
            return  new ResponseEntity<>("Exception in AuthController", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
