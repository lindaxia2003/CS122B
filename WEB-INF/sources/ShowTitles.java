import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import MyClasses.*;

public class ShowTitles extends HttpServlet {

	public String getServletInfo() {
		return "Show titles of movies";
	}

	// Use http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// Input channels of data
		String parameterName = request.getParameter("parameterName");

		// This is how you send information to the browser who issues
		// the original call in this sequence of resource execution.
		PrintWriter responseWriteChannel = response.getWriter();
		responseWriteChannel.println("ShowTitles: doPost");
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		try{
			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();

			// get title list
			List<String> titleList = getListOfTitles(dbcon);
		
			// set session title_list
			mySession.setAttribute("title_list", titleList);

			// forward to titles.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/titles.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public List<String> getListOfTitles(Connection dbcon) {

		List<String> titleList = new ArrayList<>();
		// set title list
		String alphaNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
			
		for (int i = 0; i < alphaNum.length(); i++) {
			String t = String.valueOf(alphaNum.charAt(i));
			titleList.add(t);
		}
		return titleList;
	}
}
