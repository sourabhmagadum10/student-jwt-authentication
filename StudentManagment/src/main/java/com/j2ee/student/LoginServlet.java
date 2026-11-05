package com.j2ee.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;
import com.mysql.cj.xdevapi.Result;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String username= req.getParameter("username");
		String password= req.getParameter("password");
		
		String sql= "select sid, sname, passW, token from student where sname = ?";
		
		try(Connection con = DatabaseConn.getConnection();
				PreparedStatement ps= con.prepareStatement(sql)){
			
			ps.setString(1, username);
			
			try(ResultSet  rs = ps.executeQuery()){
				if(!rs.next()) {
					resp.sendRedirect(req.getContextPath()+"/login.html?error=invalid");
					return;
				}
					
					String storeHash = rs.getString("passW");
					
					if(!PasswordUtil.verifyPassword(password, storeHash)) {
						
						 System.out.println("BCrypt password matched!");
						resp.sendRedirect(req.getContextPath()+"/login.html?error=invalid");
						return;
						
//						
//						resp.sendRedirect(req.getContextPath()+ "/dashboard.jsp");
					}
					
					String activeToken= rs.getString("token");
					if(activeToken != null) {
						resp.sendRedirect(req.getContextPath() + "/login.html?alreadyLogined");
						return;
					}
					
					String generatedToken= UUID.randomUUID().toString();
					
					String updateSql = "update student set token= ?" +" where sid = ? and token is NULL";
					
					
					try(PreparedStatement updateP = con.prepareStatement(updateSql)){
						updateP.setString(1, generatedToken);
						updateP.setInt(2, rs.getInt("sid"));
							
						
						
						int update = updateP.executeUpdate();
						
						if(update == 0) {
							resp.sendRedirect(req.getContextPath()+ "/login.html?error=alreadyloged");
							return;
						}
					}
					
					HttpSession session = req.getSession(true);
					session.setAttribute("userId", rs.getInt("sid"));
					
					session.setAttribute("username", rs.getString("sname") );
					session.setAttribute("loginToken", "token");
					resp.sendRedirect(req.getContextPath()+ "/dashboard.jsp");
					
//					else {
//						resp.sendRedirect(req.getContextPath()+"/login.html?error=invalid");
//					}
//				}
//				else {
//					resp.sendRedirect(req.getContextPath()+"/login.html?error=invalid");
//				}
					
			}
		}
		catch(Exception e) {
			throw new ServletException("login fail" + e);
		}
	}

}
