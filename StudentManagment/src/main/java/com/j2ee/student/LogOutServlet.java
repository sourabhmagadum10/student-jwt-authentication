package com.j2ee.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(false);
		
		if(session != null) {
			
			Integer userId=  (Integer) session.getAttribute("userId");
			
			String token= (String) session.getAttribute("loginToken");
			
			if(userId != null && token != null) {
				String sql = "update student set token = null from student where sid=? and token=?";
				
				try(Connection con = DatabaseConn.getConnection();
						PreparedStatement ps= con.prepareStatement(sql)){
					ps.setInt(1, userId);
					ps.setString(2, token);
					ps.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
			session.invalidate();
				
			}
			
			
			resp.sendRedirect(req.getContextPath()+ "/login.html");
			
		}
		
		
	}

