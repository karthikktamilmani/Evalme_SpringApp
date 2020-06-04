package com.group18.asdc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;
import java.util.HashMap;

import com.group18.asdc.dao.UserDao;
import com.group18.asdc.database.SQLMethods;
import com.group18.asdc.database.SQLQueries;
import com.group18.asdc.entities.User;
import com.group18.asdc.util.GroupFormationToolUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SQLMethods sqlImplementation;
	@Autowired
	private DataSource dataSource;
	
	private Logger log=Logger.getLogger(UserDaoImpl.class.getName());

	@Override
	public Boolean authenticateByEmailAndPassword(ArrayList<Object> valuesList) throws SQLException {
		return sqlImplementation.selectQuery(SQLQueries.USER_AUTH_BY_EMAIL_PASSWORD.toString(), valuesList).size() == 1;
	}

	@Override
	public boolean isUserExists(User user) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement checkUser = null;
		try {
			connection = dataSource.getConnection();
			checkUser = connection.prepareStatement(GroupFormationToolUtil.isUserExists);
			checkUser.setString(1, user.getBannerId());
			resultSet = checkUser.executeQuery();
			log.info("In User Dao to check if user exists or not");
			if (resultSet.next()) {
				return true;
			} else {

				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (checkUser != null) {
					checkUser.close();
				}
				log.info("closing connection after having a check if user exists or not");
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
		User user = null;
		try {
			connection = dataSource.getConnection();
			String userSql = GroupFormationToolUtil.getUserById;
			getUser = connection.prepareStatement(userSql);
			getUser.setString(1, bannerId);
			log.info("In User Dao to get the user for given banner id");
			resultSet = getUser.executeQuery();

			while (resultSet.next()) {

				user = new User();
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (getUser != null) {
					getUser.close();
				}
				log.info("closing the connection after getting user by banner id");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return user;
	}

	@Override
	public List<User> filterEligibleUsersForCourse(List<User> studentList, int courseId) {

		// Returns the list of eligible users to get enrolled in the course.

		List<User> eligibleStudents = new ArrayList<User>();
		List<User> existingStudentsOfCourse = this.getAllUsersByCourse(courseId);

		log.info("In User Dao to get filterEligible ");
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

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSetForStudentList = null;
		List<User> studentList = new ArrayList<User>();
		User user = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(GroupFormationToolUtil.getAlluserRelatedToCourse);
            log.info("In users dao getting all users based on course id");
			preparedStatement.setInt(1, courseId);
			resultSetForStudentList = preparedStatement.executeQuery();

			while (resultSetForStudentList.next()) {
				user = new User();
				user.setBannerId(resultSetForStudentList.getString("bannerid"));
				user.setEmail(resultSetForStudentList.getString("emailid"));
				user.setFirstName(resultSetForStudentList.getString("firstname"));
				user.setLastName(resultSetForStudentList.getString("lastname"));
				studentList.add(user);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (resultSetForStudentList != null) {
					resultSetForStudentList.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				log.info("Closing connections after getting users based on course");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return studentList;
	}

	@Override
	public User getInstructorForCourse(int courseId) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User instructor = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(GroupFormationToolUtil.getInstructorForCourse);
			preparedStatement.setInt(1, courseId);
			resultSet = preparedStatement.executeQuery();
			String bannerId = null;
			log.info("In user dao for getting Instructor for course id");
			while (resultSet.next()) {
				bannerId = resultSet.getString("bannerid");
			}
			if (bannerId != null) {
				instructor = this.getUserById(bannerId);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}

				if (preparedStatement != null) {
					preparedStatement.close();
				}

				if (resultSet != null) {
					preparedStatement.close();
				}
				log.info("closing connection after getting instructor for a course");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return instructor;
	}

    @Override
    public void loadUserWithBannerId(ArrayList<Object> valueList, User userObj) throws SQLException {
        ArrayList<HashMap<String,Object>> rowsList = sqlImplementation.selectQuery(SQLQueries.GET_USER_WITH_BANNER_ID.toString(), valueList);
        //
        if( rowsList.size() > 0)
        {
            HashMap<String,Object> valuesMap = rowsList.get(0);
            //
            userObj.setBannerId((String)valuesMap.get("bannerid"));
            userObj.setEmail((String)valuesMap.get("emailid"));
            userObj.setFirstName((String)valuesMap.get("firstname"));
            userObj.setLastName((String)valuesMap.get("lastname"));
            userObj.setPassword((String)valuesMap.get("password"));
        }
    }

    @Override
    public Boolean updatePassword(ArrayList<Object> criteriaList, ArrayList<Object> valueList) throws SQLException {
        Integer rowCount = sqlImplementation.updateQuery(SQLQueries.UPDATE_PASSWORD_FOR_USER.toString(), valueList, criteriaList);
        return rowCount > 0;
    }

}