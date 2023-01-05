package com.bus.ticket.dto;
import java.time.LocalDate;
import java.util.List;

import com.bus.ticket.model.Users;
import lombok.Data;

@Data
public class UserResponseDTO {
  private Integer id;
  private String firstName;
  private String lastName;
  LocalDate dateOfBirth;
  private String username;
  private String email;
  List<Users> appUserRoles;
}
