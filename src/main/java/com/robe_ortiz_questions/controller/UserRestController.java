package com.robe_ortiz_questions.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robe_ortiz_questions.entity.user.User;
import com.robe_ortiz_questions.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUser(){
		List<User> users = userService.findAllUser();
		if(users.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		User user = userService.findByEmail(email);
		if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("{email}")
	public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
	    User user = userService.findByEmail(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
	    }
	    
	    userService.deleteByEmail(email);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}
	
    @PutMapping("{email}")
    public ResponseEntity<User> updateUserByEmail(@PathVariable String email, @RequestBody User updatedUser) {

        User user = userService.findByEmail(email);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }       
        
        user.setName(updatedUser.getName());       
        User savedUser = userService.save(user);       
        return ResponseEntity.ok(savedUser);
    }
	
}
