package com.bus.ticket.dto;

import java.time.LocalDate;
import java.util.List;

import com.bus.ticket.model.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataDTO {
  private String firstName;
  private String lastName;
  LocalDate dateOfBirth;
  private String username;
  private String email;
  private String password;
  List<Roles> appUserRoles;

}
