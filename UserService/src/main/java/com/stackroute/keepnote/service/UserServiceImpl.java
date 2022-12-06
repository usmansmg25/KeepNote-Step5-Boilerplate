package com.stackroute.keepnote.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the UserRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	private Log log = LogFactory.getLog(getClass());
	
	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(User user) throws UserAlreadyExistsException {

		User userCreated =  userRepository.insert(user);
		
		if(userCreated!=null)
		{
			log.debug("userId: "+userCreated.getUserId());
			log.debug("userName: "+userCreated.getUserName());
			log.debug("pwd: "+userCreated.getUserPassword());
			log.debug("mobile: "+userCreated.getUserMobile());
			log.debug("added Date: "+userCreated.getUserAddedDate());
			return userCreated;
		}
		throw new UserAlreadyExistsException("UserAlreadyExistsException");
	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(String userId,User user) throws UserNotFoundException {

		userRepository.save(user);
		return userRepository.findById(userId).get();
	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(String userId) throws UserNotFoundException {

		try 
		{
			userRepository.deleteById(userId);
			return true;
		} catch (Exception e) 
		 {
			throw new UserNotFoundException("UserNotFoundException");
		}
	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(String userId) throws UserNotFoundException {

		User user = null;
		try {
			user = userRepository.findById(userId).get();
			if (user != null) {
				return user;
			} else {
				throw new UserNotFoundException("UserNotFoundException");
			}
		} catch (Exception e) {
			throw new UserNotFoundException("UserNotFoundException");
		}
	}

}
