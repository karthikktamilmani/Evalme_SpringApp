package com.group18.asdc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.group18.asdc.entities.Course;
import com.group18.asdc.entities.User;
import com.group18.asdc.util.GroupFormationToolUtil;

@Repository
public class CourseDetailsDaoImpl implements CourseDetailsDao {

	@Autowired
	private DataSource dataSource;

	@Override
	public List<Course> getAllCourses() {

		Connection con = null;
		Statement getCourses = null;
		PreparedStatement getCourseRoles = null;
		ResultSet resultSetAllCourses = null;
		ResultSet resultSetAllCourseRoles = null;
		List<Course> allCourses = new ArrayList<Course>();
		try {
			con = dataSource.getConnection();
			getCourses = con.createStatement();
			resultSetAllCourses = getCourses.executeQuery(GroupFormationToolUtil.getAllCourses);
			getCourseRoles = con.prepareStatement(GroupFormationToolUtil.getCourseDetails);
			Course course = null;
			while (resultSetAllCourses.next()) {
				course = new Course();
				List<User> students = new ArrayList<User>();
				List<User> taList = new ArrayList<User>();
				course.setCourseId(resultSetAllCourses.getInt("courseid"));
				course.setCourseName(resultSetAllCourses.getString("coursename"));
				getCourseRoles.setInt(1, resultSetAllCourses.getInt("courseid"));
				resultSetAllCourseRoles = getCourseRoles.executeQuery();
				while (resultSetAllCourseRoles.next()) {

					String role = resultSetAllCourseRoles.getString("rolename");
					String bannerId = resultSetAllCourseRoles.getString("bannerid");
					if (role.equalsIgnoreCase("INSTRUCTOR")) {

						course.setInstructorName(this.getUserById(bannerId));
					} else if (role.equalsIgnoreCase("STUDENT")) {
						
						students.add(this.getUserById(bannerId));
					} else if (role.equalsIgnoreCase("TA")) {
						
						taList.add(this.getUserById(bannerId));
					}
				}
				course.setTaList(taList);
				course.setStudentList(students);
				resultSetAllCourseRoles.close();
				allCourses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				getCourses.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return allCourses;
	}

	@Override
	public boolean allocateTa(String courseId, String bannerId) {

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection =dataSource.getConnection();
			statement = connection.prepareStatement(
					"insert into CSCI5308_18_DEVINT.courserole (roleid,courseid,bannerid) values (2,?,?);");
			statement.setInt(1, Integer.parseInt(courseId));
			statement.setString(2, bannerId);
			int taAllocated = statement.executeUpdate();
			if (taAllocated > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;

	}

	@Override
	public boolean isUserExists(String bannerId) {
		Connection connection = null;
		ResultSet resultSet = null;
		Statement checkUser = null;
		try {
			connection =dataSource.getConnection();
			String userSql = "select * from CSCI5308_18_DEVINT.user where bannerid='" + bannerId + "';";
			checkUser = connection.createStatement();
			resultSet = checkUser.executeQuery(userSql);
			if (resultSet.next()) {
				System.out.println("user exists");
				return true;
			} else {

				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;

	}

	@Override
	public User getUserById(String bannerId) {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement getUser = null;
		User user = new User();
		try {
			connection = dataSource.getConnection();
			String userSql = GroupFormationToolUtil.getUserById;
			getUser = connection.prepareStatement(userSql);
			getUser.setString(1, bannerId);
			resultSet = getUser.executeQuery();

			while (resultSet.next()) {

				user.setBannerId(resultSet.getString("bannerid"));
				user.setEmail(resultSet.getString("emailid"));
				user.setFirstName(resultSet.getString("firstname"));
				user.setLastName(resultSet.getString("lastname"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return user;
	}

	@Override
	public List<Course> getCourseWhereUserIsInstrcutor(String bannerid) {
		
		
		
		return null;
	}

	@Override
	public List<Course> getCourseWhereUserIsTA(String bannerid) {
		// TODO Auto-generated method stub
		return null;
	}

}