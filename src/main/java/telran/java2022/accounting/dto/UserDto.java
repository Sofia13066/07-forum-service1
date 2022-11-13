package telran.java2022.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.java2022.accounting.utils.Role;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  String login;
  String firstName;
  String lastName;
  Iterable<Role> roles;
}
