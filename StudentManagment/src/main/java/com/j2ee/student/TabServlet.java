package com.j2ee.student;

import java.io.IOException;

import com.mysql.cj.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/claim-tab")
public class TabServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("TabServlet called!");
		HttpSession session =  req.getSession(false);
		
		System.out.println("hiiiii");
		if(session == null || session.getAttribute("userId")== null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		 String tabId = req.getParameter("tabId");

	        if (tabId == null || tabId.isBlank()) {
	            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }
		
		synchronized (session) {
			
			String key= "activeTabId"+ session.getAttribute("userId");
			
			String existTable = (String) session.getAttribute("activeTabId");
			
			System.out.println("Student ID: " + session.getAttribute("userId"));
			System.out.println("Existing Tab ID: " + existTable);
			System.out.println("New Tab ID: " + tabId);
			if(existTable == null) {
				
				session.setAttribute(key, tabId);
//				session.setAttribute("activeTabId", tabId);
				resp.setStatus(HttpServletResponse.SC_OK);;
			}
			else if(existTable.equals(tabId)) {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				resp.sendError(HttpServletResponse.SC_CONFLICT, "This account is already active in another tab.");
			}
		}
		
	}
}
