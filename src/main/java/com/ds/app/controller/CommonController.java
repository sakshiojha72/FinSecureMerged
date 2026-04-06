package com.ds.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
import com.ds.app.entity.AppUser;
import com.ds.app.jwtutil.JWTUtil;
import com.ds.app.service.AppUserService;
import com.ds.app.service.EmployeePhotoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/finsecure/public")
public class CommonController {
	
	@Autowired
	private AppUserService appUserService;
	
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<SignUpUserResponseDTO> registerUser(@RequestBody AppUser user){
		AppUser savedUser = appUserService.registerAppUser(user);
		SignUpUserResponseDTO dto = new SignUpUserResponseDTO(
				savedUser.getUserId(),
				savedUser.getUsername(),
				savedUser.getRole()
				);
		
		return new ResponseEntity<SignUpUserResponseDTO>(dto,HttpStatus.OK);
	} 
	

	@PostMapping("/login")
	public ResponseEntity<LogInResponseDTO> loginUser(@Valid @RequestBody LogInRequestDTO request) {
		//String username = appUserRequestBodyDto.getUsername();
		//String password = appUserRequestBodyDto.getPassword();
		try {	
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getUsername(), 
							request.getPassword()
							));
		 } catch (BadCredentialsException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new LogInResponseDTO(
	                            null,
	                            null,
	                            request.getUsername(),
	                            null,
	                            "Invalid username or password"
	                    ));
	        }

			
			AppUser user = appUserService.findByUsername(request.getUsername());
			
			String token = jwtUtil.generateToken(
				    user.getUsername(),
				    user.getUserId(),
				    user.getRole().name()
				);
			LogInResponseDTO response = new LogInResponseDTO(
					token,
					user.getUserId(),
					user.getUsername(),
					user.getRole().name(),
					"Login Successful"
					);
			
			return ResponseEntity.ok(response);
		}
		
}//end class
