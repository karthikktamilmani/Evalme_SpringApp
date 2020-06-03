package com.group18.asdc.dao.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.group18.asdc.dao.UserDao;
import com.group18.asdc.entities.Course;
import com.group18.asdc.entities.User;

public class UserDaoImplMock implements UserDao {

	private static List<Course> coursesDetails = new ArrayList<Course>();
	private static List<User> userList = new ArrayList<User>();
	private CourseDaoImplMock courseDaoImplMock;

	public UserDaoImplMock() {
		super();

		courseDaoImplMock = new CourseDaoImplMock();
		// declaring the courses
		Course firstCourse = null;
		Course secondCourse = null;
		Course thirdCourse = null;
		Course fourthCourse = null;

		// declaring the users i.e students, ta and instructors.
		User instructorOne = new User("Justin", "Langer", "B00123456", "justin@dal.ca");
		userList.add(instructorOne);
		User instructorTwo = new User("Don", "Bradman", "B00741399", "don@dal.com");
		userList.add(instructorTwo);
		User instructorThree = new User("Michel", "Bevan", "B00675984", "bevan@dal.com");
		userList.add(instructorThree);
		User taOne = new User("Adam", "Gilichrist", "B00123789", "adam@dal.ca");
		userList.add(taOne);
		User taTwo = new User("Ricky", "Ponting", "B00951789", "ricky@dal.ca");
		userList.add(taTwo);
		User taThree = new User("Mike", "Hussey", "B946873", "Mile@dal.ca");
		userList.add(taThree);
		User studentOne = new User("David", "Warner", "B00789951", "david@dal.ca");
		userList.add(studentOne);
		User studentTwo = new User("Glenn", "Maxwell", "B00753159", "glenn@dal.ca");
		userList.add(studentTwo);
		User studentThree = new User("Brett", "Lee", "B00852693", "ricky@dal.ca");
		userList.add(studentThree);
		User studentFour = new User("Mathew", "Hayden", "B00496157", "haydos@dal.ca");
		userList.add(studentFour);
		User studentFive = new User("Shane", "Warne", "B00654194", "shane@dal.ca");
		userList.add(studentFive);

		// Adding users to the courses
		firstCourse = new Course(1, "Machine Learning", instructorOne, Arrays.asList(taOne, taThree),
				Arrays.asList(studentFive, studentOne));
		secondCourse = new Course(2, "Mobile Computing", instructorThree, Arrays.asList(taTwo, studentFour),
				Arrays.asList(studentOne));
		thirdCourse = new Course(3, "Cloud Computing", instructorThree, Arrays.asList(),
				Arrays.asList(studentTwo, studentThree));
		fourthCourse = new Course(4, "Software Comprehension", instructorTwo, Arrays.asList(taTwo, studentFive),
				Arrays.asList());

		coursesDetails.add(firstCourse);
		coursesDetails.add(secondCourse);
		coursesDetails.add(thirdCourse);
		coursesDetails.add(fourthCourse);
	}

	@Override
	public Boolean authenticateByEmailAndPassword(ArrayList<Object> valueList) throws SQLException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isUserExists(User user) {
		boolean isExists = false;

		if (user != null) {
			for (User theUser : UserDaoImplMock.userList) {
				if (theUser.getBannerId() == user.getBannerId()) {
					isExists = true;
					break;
				}
			}
		}

		return isExists;
	}

	@Override
	public User getUserById(String bannerId) {

		User user = null;
		for (User theUser : UserDaoImplMock.userList) {
			if (theUser.getBannerId() == bannerId) {
				user = theUser;
				break;
			}
		}

		return user;
	}

	@Override
	public List<User> filterEligibleUsersForCourse(List<User> studentList, int courseId) {

		// Returns the list of eligible users to get enrolled in the course.

		List<User> eligibleStudents = new ArrayList<User>();
		List<User> existingStudentsOfCourse = this.getAllUsersByCourse(courseId);

		for (User student : studentList) {

			boolean isExists = false;
			for (User existingStudent : existingStudentsOfCourse) {

				if (student.getBannerId().equalsIgnoreCase(existingStudent.getBannerId())) {
					isExists = true;
					break;
				}
			}
			if (!isExists) {
				eligibleStudents.add(student);
			}
		}

		return eligibleStudents;
	}

	@Override
	public List<User> getAllUsersByCourse(int courseId) {

		List<User> userList = new ArrayList<User>();

		Course course = courseDaoImplMock.getCourseById(courseId);
		if (course != null) {

			userList.add(course.getInstructorName());
			userList.addAll(course.getTaList());
			userList.addAll(course.getStudentList());

		}
		return userList;
	}

	@Override
	public User getInstructorForCourse(int courseId) {
		
		User instrUser=null;
		for(Course theCourse:UserDaoImplMock.coursesDetails) {
			
			if(theCourse.getCourseId()==courseId) {
				instrUser=theCourse.getInstructorName();
			}
			
		}
		return instrUser;
	}

	@Override
	public void loadUserWithBannerId(ArrayList<Object> valueList, User userObj) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean updatePassword(ArrayList<Object> criteriaList, ArrayList<Object> valuesList) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}