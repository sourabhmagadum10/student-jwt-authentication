package com.j2ee.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/heartbeat")
public class HeartbeatServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session= req.getSession(false);
		
		System.out.println("Heartbeat servlet called!");
		
		if(session == null ||
				session.getAttribute("userId") == null ||
				session.getAttribute("loginToken")== null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		int userId= (int) session.getAttribute("userId");
		
		String loginToken= (String) session.getAttribute("loginToken");
		
		String sql = "update student set last_seen  = current_timestamp where sid= ? and token =?";
		
		try(Connection con= DatabaseConn.getConnection();
				PreparedStatement ps= con.prepareStatement(sql)){
			ps.setInt(1, userId);
			ps.setString(2, loginToken);
			
			int update = ps.executeUpdate();
			System.out.println("Heartbeat updated rows: " + update);
			
			if(update == 1) {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				session.invalidate();
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			
		}
		catch(Exception e) {
//			e.printStackTrace();
			throw new ServletException("Heartbeat FAil!!! ",e);
		}
	}

}
