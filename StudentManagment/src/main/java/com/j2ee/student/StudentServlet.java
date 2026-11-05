package com.j2ee.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.cj.xdevapi.PreparableStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/sign")
public class StudentServlet extends HttpServlet{
	
		
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String name= req.getParameter("username");
		
//		HttpSession session= req.getSession();
//		session.setAttribute( "username",name);
		
//		String name1= (String)session.getAttribute(name);
		String password= req.getParameter("password");
		
		String hashedPassword= PasswordUtil.hashPassword(password);
		String email= req.getParameter("email");
		String course= req.getParameter("course");
		
		String sql= ("Insert into student(sname, passW, email, course) values(?, ?, ?, ?)");
		
		 resp.setContentType("text/html");
		
		try(Connection conection= DatabaseConn.getConnection();
				PreparedStatement ps= conection.prepareStatement(sql)){
			
			ps.setString(1, name);
			ps.setString(2, hashedPassword);
			ps.setString(3, email);
			ps.setString(4, course);
			
			int res= ps.executeUpdate();
			
			
			if(res > 0) {
				resp.getWriter().println("<h1>DOoone</h>");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resp.getWriter().println("<h1>Noooot Done</h1>");
		}
		
//		resp.setContentType("text/html");
//		PrintWriter print= resp.getWriter();
//		
//		print.println("<h1>registration done!</h1>");
//		print.println("<p>Name:- "+name+"</p>");
//		print.println("<p>pass:- "+password+ "</p>");
//		print.println("<p>email:- "+email+"</p>");
//		print.println("<p>course :- "+course+ "</p>");
		
//		resp.setContentType("text/html;charset=UTF-8");
//
//        resp.getWriter().println(
//            "<h1>POST Servlet is working!</h1>"
//        );
	}

}
