package com.jwt.implementation.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.implementation.model.Role;
import com.jwt.implementation.model.User;
import com.jwt.implementation.model.UserDTO;
import com.jwt.implementation.repository.RoleRepository;
import com.jwt.implementation.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userRepo.findByUserName(username);
	     return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
	}
	
	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserDTO userRegisteredDTO) {
		/*Role user = new Role(1,"ROLE_USER" );
		Role admin = new Role(2,"ROLE_ADMIN");
		
		if(roleRepo.findByRole("ROLE_USER" )!= null) {
			
			roleRepo.saveAll(List.of(user,admin));
		}*/
        
		//roleService.saveAllRoles(List.of(staff, manager, admin, superAdmin));
		
		Role role = new Role();
		  
		
		
		if(userRegisteredDTO.getRole().equals("USER"))
		  role = roleRepo.findByRole("ROLE_USER");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
		 role = roleRepo.findByRole("ROLE_ADMIN");
		User user1 = new User();
		user1.setEmail(userRegisteredDTO.getEmail());
		user1.setUserName(userRegisteredDTO.getUserName());
		user1.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user1.setRole(role);
		System.out.println(userRegisteredDTO);
		return userRepo.save(user1);
	}
}
