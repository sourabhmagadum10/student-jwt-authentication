package com.j2ee.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		
		
		HttpServletRequest requ = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String path = requ.getRequestURI().substring(requ.getContextPath().length());
		HttpSession session= requ.getSession(false);
		
		boolean loggedIn= session != null && session.getAttribute("userId") != null
				&& session.getAttribute("loginToken") != null; 
		
		
		//mention pages that we can access the without any credtional
		if(path.equals("/login.html")
				|| path.equals("/login")) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!loggedIn) {
			resp.sendRedirect(requ.getContextPath()+"/login.html");
			return;
		}
		
		Integer userId= (Integer) session.getAttribute("userId");
		String token= (String) session.getAttribute("loginToken");
		
		String sql= "select token from student whre sid=?";
		
		try(Connection con= DatabaseConn.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
				ps.setInt(1, userId);
				
				try(ResultSet re = ps.executeQuery()){
					
					if(re.next()&& token.equals(re.getString("token"))) {
						chain.doFilter(request, response);
					}
					else {
						session.invalidate();
						resp.sendRedirect(requ.getContextPath()+"/login.html?error=sessionexpire");
					}
				}
		}
		catch(Exception e) {
			throw new ServletException("Session validation failed", e);			
		}
		
//		if(session != null && session.getAttribute("userId")!= null) {
//			chain.doFilter(request, response);
//			
//		}
//		else {
//			resp.sendRedirect(requ.getContextPath()+ "/login.html");
//		}
//		
	}

}
