package com.klapeks.sql;

import java.sql.SQLException;

public class RuntimeSQLException extends RuntimeException {
	
	public RuntimeSQLException(SQLException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8004536832926862430L;

}
