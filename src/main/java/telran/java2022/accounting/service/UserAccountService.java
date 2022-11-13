package telran.java2022.accounting.service;

import telran.java2022.accounting.dto.RolesResponseDto;
import telran.java2022.accounting.dto.UserAccountResponseDto;
import telran.java2022.accounting.dto.UserDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.UserUpdateDto;

public interface UserAccountService {
	UserAccountResponseDto addUser(UserRegisterDto userRegisterDto);
    
    UserDto loginUser(String[] credentials);
	
	UserAccountResponseDto getUser(String login);
	
	UserAccountResponseDto removeUser(String login);
	
	UserAccountResponseDto editUser(String login, UserUpdateDto updateDto);
	
	RolesResponseDto changeRolesList(String login, String role, boolean isAddRole);
	
	void changePassword(String login, String newPassword);
}
