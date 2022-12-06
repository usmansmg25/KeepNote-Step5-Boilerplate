package com.stackroute.keepnote.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	
private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/user" using HTTP POST method
	 */
	@PostMapping("/api/v1/user")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		log.info("registerUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {
			user.setUserAddedDate(new Date());
			User userCreated = userService.registerUser(user);
			log.info("userCreated: "+userCreated);
			if(userCreated!=null)
			{
				return new ResponseEntity<>(headers, HttpStatus.CREATED);
			}
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
		}
		log.info("registerUser : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the user updated successfully.
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
	 */
	@PutMapping("/api/v1/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") String id,@RequestBody User user) {
		
		log.info("updateUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {	
				if(userService.updateUser(id, user)!=null)
				{
					return new ResponseEntity<>(headers, HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
				}
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		log.info("updateUser : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/api/v1/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String id) 
	{
		log.info("deleteUser : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {
			if(userService.deleteUser(id))
			{
				return new ResponseEntity<>(headers, HttpStatus.OK);
			}
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("deleteUser : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
	/*
	 * Define a handler method which will show details of a specific user. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user found successfully. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
	 * replaced by a valid userId without {}
	 */
	
	@GetMapping("/api/v1/user/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
		log.info("getUserById : STARTED");
		HttpHeaders headers = new HttpHeaders();
		try {
				User user =userService.getUserById(id);
				if(user!=null)
				{
					return new ResponseEntity<>(headers, HttpStatus.OK);
					
				}
				
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
		}
		log.info("getUserById : ENDED");
		return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
	}
}
