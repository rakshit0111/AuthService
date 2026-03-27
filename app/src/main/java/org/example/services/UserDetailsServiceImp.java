package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserInfo user = userRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Username not found in DB");


        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto)
    {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public boolean signUpUser(UserInfoDto userInfoDto)
    {
        //add validation logic to check whether userInfoDto fields like email and password are valid or not
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto)))
            return false;

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        userRepository.save(new UserInfo(UUID.randomUUID().toString(),userInfoDto.getUsername()
                ,userInfoDto.getPassword(),new HashSet<>()));
        return true;
    }
}
