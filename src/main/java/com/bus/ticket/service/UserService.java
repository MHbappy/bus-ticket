package com.bus.ticket.service;


import javax.servlet.http.HttpServletRequest;

import com.bus.ticket.exception.CustomException;
import com.bus.ticket.repository.UserRepository;
import com.bus.ticket.model.Users;
import com.bus.ticket.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  public String signin(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

      Users appUser = userRepository.findByEmail(email);

      return jwtTokenProvider.createToken(email, appUser);
    } catch (AuthenticationException e) {
      e.printStackTrace();
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signup(Users appUser) {
    if (!userRepository.existsByEmail(appUser.getEmail())) {
      appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
      userRepository.save(appUser);
      return jwtTokenProvider.createToken(appUser.getEmail(), appUser);
    } else {
      throw new CustomException("Email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String email) {
    userRepository.deleteByEmail(email);
  }

  public Users search(String email) {
    Users appUser = userRepository.findByEmail(email);
    if (appUser == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return appUser;
  }

  public Users whoami(HttpServletRequest req) {
    return userRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  public boolean changePassword(Users users, String oldPassword, String newPassword){
    if (passwordEncoder.matches(oldPassword, users.getPassword())){
      users.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(users);
    }else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your password is wrong");
    }
    return true;
  }

  public List<Users> usersList(){
    return userRepository.findAll();
  }

  public String refresh(String email) {
    return jwtTokenProvider.createToken(email, userRepository.findByEmail(email));
  }

}
