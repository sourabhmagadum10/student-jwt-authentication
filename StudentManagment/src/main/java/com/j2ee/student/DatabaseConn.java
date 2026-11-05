package com.j2ee.student;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConn {

	private static final String url="jdbc:mysql://localhost:3306/student";
	
	private static final String username = "root";;
	
	private static final String password = "root";
	
	
	
	public static Connection getConnection()throws SQLException, ClassNotFoundException{ 
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(url, username, password);
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection con= DatabaseConn.getConnection();
		try {
			
			System.out.println("Connnect");
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		finally {
			
				con.close();
			
		}
	}
	
}
