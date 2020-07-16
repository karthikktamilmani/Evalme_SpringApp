package com.group18.asdc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group18.asdc.database.ConnectionManager;
import com.group18.asdc.database.SQLMethods;
import com.group18.asdc.database.SQLQueries;

public class PasswordPolicyDB implements IPasswordPolicyDB {

	private Logger logger = Logger.getLogger(IPasswordPolicyDB.class.getName());

	@Override
	public ArrayList loadBasePoliciesFromDB() {
		logger.log(Level.INFO, "Loading base password policies from DB");
		SQLMethods sqlImplementation = null;
		ArrayList policiesList = new ArrayList<>();
		try {
			Connection connection = ConnectionManager.getInstance().getDBConnection();
			sqlImplementation = new SQLMethods(connection);
			policiesList = sqlImplementation.selectQuery(SQLQueries.GET_BASEPASSWORD_POLICIES.toString(),
					new ArrayList<>());
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "SQL Exception while fetching base password policies", e);
		} finally {
			/*
			 * Had a discussion with Professor Rob and this cannot be avoided without
			 * complicating the code
			 */
			if (sqlImplementation != null) {
				sqlImplementation.cleanup();
			}
		}
		return policiesList;
	}

	@Override
	public ArrayList loadPoliciesFromDB() {
		logger.log(Level.INFO, "Loading history password policies from DB");
		SQLMethods sqlImplementation = null;
		ArrayList policiesList = new ArrayList<>();
		try {
			Connection connection = ConnectionManager.getInstance().getDBConnection();
			sqlImplementation = new SQLMethods(connection);
			policiesList = sqlImplementation.selectQuery(SQLQueries.GET_HISTORYPASSWORD_POLICIES.toString(),
					new ArrayList<>());
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "SQL Exception while loading history password policies", e);
		} finally {
			/*
			 * Had a discussion with Professor Rob and this cannot be avoided without
			 * complicating the code
			 */
			if (sqlImplementation != null) {
				sqlImplementation.cleanup();
			}
		}
		return policiesList;
	}
}