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
		HttpSession session =  req.getSession(false);
		
		if(session == null || session.getAttribute("username")== null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		 String tabId = req.getParameter("tabId");

	        if (tabId == null || tabId.isBlank()) {
	            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }
		
		synchronized (session) {
			String existTable = (String) session.getAttribute("activeTabId");
			
			if(existTable == null) {
				session.setAttribute("activeTabId", tabId);
			}else if(existTable.equals(tabId)) {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				resp.sendError(HttpServletResponse.SC_CONFLICT, "This account is already active in another tab.");
			}
		}
		
	}
}
