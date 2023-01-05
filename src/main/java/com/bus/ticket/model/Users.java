package com.bus.ticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Table(name = "users")
@Entity
@Data
@NoArgsConstructor
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstName;

  private String lastName;

  LocalDate dateOfBirth;

  @Column(unique = true, nullable = false)
  private String email;

  @Size(min = 8, message = "Minimum password length: 8 characters")
  @JsonIgnore
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_role",
          joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
          inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
  private Set<Roles> roles = new HashSet<>();

}
