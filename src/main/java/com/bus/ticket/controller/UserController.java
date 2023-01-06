package com.bus.ticket.controller;

import com.bus.ticket.dto.UserDataDTO;
import com.bus.ticket.model.Users;
import com.bus.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final ModelMapper modelMapper;

  @PostMapping("/signin")
  public String login(
       @RequestParam String username,
       @RequestParam String password) {
    return userService.signin(username, password);
  }

  @PostMapping("/signup")
  public String signup(@RequestBody UserDataDTO user) {
    return userService.signup(modelMapper.map(user, Users.class));
  }

  @DeleteMapping(value = "/{username}")
  public String delete(@PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/me")
  public Users getOwnUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    return userService.search(currentPrincipalName);
  }

  @GetMapping(value = "/{username}")
  public Users search(@PathVariable String username) {
    return userService.search(username);
  }

  @PostMapping(value = "/change-password")
  public Boolean changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    Users users = userService.search(currentPrincipalName);
    return userService.changePassword(users, oldPassword, newPassword);
  }

  @GetMapping(value = "/all-users")
  public List<Users> allUserList() {
   return userService.usersList();
  }
}
