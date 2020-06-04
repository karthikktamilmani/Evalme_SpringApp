package com.group18.asdc.dao;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.group18.asdc.entities.CourseAdmin;
import com.group18.asdc.util.GroupFormationToolUtil;


public class AdminDaoImpl implements AdminDao{

	private static Connection connection;
	private static PreparedStatement pstatement;
	private static ResultSet resultset;
	
	@Autowired
	private DataSource dataSource;
	

	@Override
	public boolean checkCourseId(int courseid) {

		//check if courseId exists in the database
		//return true if it does
		

		int courseidps = courseid;
		boolean returnType=false;

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		String courseidrs = "";

		//get connection

		try {

			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to check if courseid exists in the database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseId);
			//set values in prepared statement 
			pstatement.setInt(1, courseidps);

			//execute query
			resultset = pstatement.executeQuery();

			//store courseid in String courseidrs
			while(resultset.next()) {
				courseidrs = resultset.getString("courseid");
			}


			//if courseidrs is null, courseid does not exist, return false
			if(courseidrs.equals(null) || courseidrs.equals("") || courseidrs.equals(" ")) {
				returnType=false;
			}
			//if courseidrs is not null, courseid exists, return true
			else {
				returnType=true;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
			
			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			
		}

		return returnType;
	}

	@Override
	public boolean checkCourseName(String coursename) {

		//check if courseId exists in the database
		//return true if it does

		String coursenameps = coursename;
		boolean returnType=false;
		String coursenamers = "";

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		//get connection

		try {

			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to check if coursename exists in the database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseName);
			//set values in prepared statement 
			pstatement.setString(1, coursenameps);

			//execute query
			resultset = pstatement.executeQuery();

			//store coursename in String coursenamers
			while(resultset.next()) {
				coursenamers = resultset.getString("coursename");
			}

			//if coursenamers is null, coursename does not exist, return false
			if(coursenamers.equals(null) || coursenamers.equals("") || coursenamers.equals(" ")) {
				returnType=false;

			}
			//if coursenamers is not null, coursename exists, return true
			else {
				returnType=true;

			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}

			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			
		}

		return returnType;

	}

	@Override
	public String checkInstructorId(String instructorid) {

		//check if instructor is registered as a user in the database
		//return true if they are

		String instructidps = instructorid;
		String returnType="";
		String instructidrs = "";
		String instructstudentidrs="";

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		//get connection

		try {

			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to check if instructorid exists in the database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkInstructorId);
			//set values in prepared statement 
			pstatement.setString(1, instructidps);

			//execute query
			resultset = pstatement.executeQuery();

			//store result in String instructidrs
			while(resultset.next()) {
				instructidrs = resultset.getString("bannerid");
			}

			//if instructidrs is null, instructidrs does not exist, return false
			if(instructidrs.equals(null) || instructidrs.equals("") || instructidrs.equals(" ")) {
				returnType="invalidinst";

			}

			//if instructidrs is not null, instructidrs exists
			//then, check if the user is registered as a student
			else {
				returnType="invalidinst";

				//statement to check if instructor is registered as a student or TA 
				pstatement = connection.prepareStatement(GroupFormationToolUtil.checkInstructorStudent);

				//set values in prepared statement
				pstatement.setString(1, instructidps);

				//execute query
				resultset = pstatement.executeQuery();

				//store resultset value - bannerid, in a string
				while(resultset.next()) {
					instructstudentidrs = resultset.getString("bannerid");
				}

				//if instructstudentidrs is not empty, instructor is also a student or TA
				if(!instructstudentidrs.equals(null)) {
					returnType="invalidinst";

				}
				//if instructstudentidrs is empty, instructor is not a student or TA
				else {
					returnType="success";
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}

			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return returnType;
	}

	@Override
	public boolean addCourse(int courseid, String coursename) {

		//insert new course in course table

		boolean returnType=true;

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		//get connection

		try {
			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to insert new course in the database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.createCourse1);
			//set values in prepared statement 
			pstatement.setInt(1, courseid);
			pstatement.setString(2, coursename);

			//execute statement to insert new course in the table course
			pstatement.execute();

			//check if the newly added course is in the course table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseId);
			pstatement.setInt(1,courseid);

			//execute query 
			resultset = pstatement.executeQuery();

			//if resultset is null, query is not executed, course is not added
			if(resultset.equals(null))
			{
				returnType = false;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}

			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return returnType;

	}

	@Override
	public boolean addCourse(CourseAdmin courseadmin) {

		int courseid = courseadmin.getCourseId();
		String coursename = courseadmin.getCourseName();
		String instructorid = courseadmin.getInstructorId();
		
		//insert new course in course table

		boolean returnType=true;

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		//get connection

		try {
			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to insert new course in the database - course table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.createCourse1);
			//set values in prepared statement 
			pstatement.setInt(1, courseid);
			pstatement.setString(2, coursename);

			//execute statement to insert new course in the table course
			pstatement.execute();

			//statement to insert new course in the database - courserole table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.createCourse2);
			//set values in prepared statement 
			pstatement.setInt(1, courseid);
			pstatement.setString(2, instructorid);

			//execute statement to insert new course in the table courserole
			pstatement.execute();
			
			//1.check if the newly added course is in the course table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseId);
			pstatement.setInt(1,courseid);

			//execute query 
			resultset = pstatement.executeQuery();

			//if resultset is null, course is not added
			if(resultset.equals(null))
			{
				returnType = false;
			}
			
			//2.check if the newly added course is in the courserole table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkcreateCourse2);
			pstatement.setInt(1,courseid);
			pstatement.setString(2, instructorid);
			
			//execute query 
			resultset = pstatement.executeQuery();

			//if resultset is null, course is not added
			if(resultset.equals(null))
			{
				returnType = false;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}

			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return returnType;

	}

	@Override
	public boolean deleteCourse(int courseid) {
		
		
		//delete course in course table

		boolean returnType=true;

		//set variables to null
		connection = null;
		pstatement = null;
		resultset = null;

		//get connection

		try {
			//open connection to database

			connection = dataSource.getConnection();

			//statement to use database
			pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
			pstatement.execute();

			//statement to delete course in the database - courserole table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.deleteCourse1);
			//set values in prepared statement 
			pstatement.setInt(1, courseid);
			

			//execute statement to delete course in the table courserole
			pstatement.execute();
			
			//statement to delete course in the database - course table 
			pstatement = connection.prepareStatement(GroupFormationToolUtil.deleteCourse2);
			//set values in prepared statement 
			pstatement.setInt(1, courseid);
			

			//execute statement to delete course in the table course
			pstatement.execute();

			//check if the deleted course is in the course table
			pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseId);
			pstatement.setInt(1,courseid);

			//execute query 
			resultset = pstatement.executeQuery();

			//if resultset is not null, course is not deleted
			if(!resultset.equals(null))
			{
				returnType = false;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if(pstatement!=null) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
			
			//close resultset if not null
			if(resultset!=null) {
				try {
					resultset.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			//close connection if not null
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return returnType;

	}

	@Override
	public boolean addInstructor(int courseid, String bannerid) {
		
		boolean returnType = true;
		
		//set variables to null
				connection = null;
				pstatement = null;
				resultset = null;

				//get connection

				try {
					//open connection to database

					connection = dataSource.getConnection();

					//statement to use database
					pstatement = connection.prepareStatement(GroupFormationToolUtil.useDatabase);
					pstatement.execute();

					//it is assumed that the course exists in the course table.
					//Values will only be added in course role table.
					
					//we delete any existing instructors from course role table
					
					pstatement = connection.prepareStatement(GroupFormationToolUtil.deletecreateCourse2);
					//set values in prepared statement 
					pstatement.setInt(1, courseid);

					//execute statement to delete existing instructor in the table courserole
					pstatement.execute();
					
					
					//statement to insert new instructor in the database - courserole table
					pstatement = connection.prepareStatement(GroupFormationToolUtil.createCourse2);
					//set values in prepared statement 
					pstatement.setInt(1, courseid);
					pstatement.setString(2, bannerid);

					//execute statement to insert new course in the table courserole
					pstatement.execute();
					
					//1.check if the newly added course is in the course table
					pstatement = connection.prepareStatement(GroupFormationToolUtil.checkCourseId);
					pstatement.setInt(1,courseid);

					//execute query 
					resultset = pstatement.executeQuery();

					//if resultset is null, course is not added
					if(resultset.equals(null))
					{
						returnType = false;
					}
					
					//2.check if the newly added course is in the courserole table
					pstatement = connection.prepareStatement(GroupFormationToolUtil.checkcreateCourse2);
					pstatement.setInt(1,courseid);
					pstatement.setString(2, bannerid);
					
					//execute query 
					resultset = pstatement.executeQuery();

					//if resultset is null, course is not added
					if(resultset.equals(null))
					{
						returnType = false;
					}


				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					
					if(pstatement!=null) {
						try {
							pstatement.close();
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
					}

					//close resultset if not null
					if(resultset!=null) {
						try {
							resultset.close();
						} catch (SQLException e) {

							e.printStackTrace();
						}
					}
					//close connection if not null
					if(connection!=null) {
						try {
							connection.close();
						} catch (SQLException e) {

							e.printStackTrace();
						}
					}
				}

		
		
		return returnType;
	}
	
}
