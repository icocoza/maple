package com.ccz.modules.domain.dbhelper;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
	Connection connection;
	
	public DbConnection(Connection con) {
		this.connection = con;
	}
	
	public Connection getConn() {
		return connection;
	}
	
	public Connection getConn(boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
