package com.group18.asdc.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.group18.asdc.entities.Course;
import com.group18.asdc.entities.User;
import com.group18.asdc.service.UserService;

@SpringBootTest
public class UserServiceImplTest {

	// test for isUserExists method
	@Test
	public void isUserExistsTestOne() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = null;
		boolean isExist = UserServiceImplMock.isUserExists(user);
		assertFalse(isExist);
	}

	@Test
	public void isUserExistsTestTwo() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = new User("Justin", "Langer", "B00123456", "justin@dal.ca");
		boolean isExist = UserServiceImplMock.isUserExists(user);
		assertTrue(isExist);
	}

	@Test
	public void isUserExistsTestThree() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = new User("Sachin", "Tendulkar", "B00999999", "sachin@dal.ca");
		boolean isExist = UserServiceImplMock.isUserExists(user);
		assertFalse(isExist);
	}

	/*
	 * First test is to send banner id which not exists and it should return null
	 * object
	 */

	@Test
	public void getUserByIdTestOne() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = UserServiceImplMock.getUserById("B00111111");
		assertNull(user);
	}

	/*
	 * Second Test is send the empty string as we should get a null object again
	 */
	@Test
	public void getUserByIdTestTwo() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = UserServiceImplMock.getUserById("");
		assertNull(user);
	}

	/*
	 * Third test is to send a valid banner id and we should get the valid user
	 * object
	 */
	@Test
	public void getUserByIdTestThree() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = UserServiceImplMock.getUserById("B00123456");
		assertNotNull(user);
	}

	/*
	 * Below test is done by sending valid course id and and we should get all 5
	 * users for course id 1
	 */

	@Test
	public void getAllUsersByCourseTestOne() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		List<User> allUsers = UserServiceImplMock.getAllUsersByCourse(1);
		assertEquals(5, allUsers.size());
	}

	/*
	 * second test is done by sending invalid course id and we get zero users for
	 * that
	 */
	@Test
	public void getAllUsersByCourseTestTwo() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		List<User> allUsers = UserServiceImplMock.getAllUsersByCourse(10);
		assertEquals(0, allUsers.size());
	}

	@Test
	public void getCurrentUserTest() {

		UserService UserServiceImplMock = new UserServiceImplMock();
		User user = UserServiceImplMock.getCurrentUser();
		assertNotNull(user);
	}

	public void isUserInstructorTest() {
		UserService theUserService = new UserServiceImplMock();
		assertTrue(theUserService.isUserInstructor(new Course()));
	}

}
