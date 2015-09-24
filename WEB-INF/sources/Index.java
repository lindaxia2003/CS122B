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

public class Index extends HttpServlet {
	public String getServletInfo() {
		return "Servlet Login connects to moviedb database and checks username and password in moviedb_customers table";
	}

	// Use http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/html"); // Response mime type

		// access the session object.
		HttpSession mySession = request.getSession();
		
		// get parameters 
		String usernameVar = request.getParameter("username");
		String passwordVar = request.getParameter("password");
		usernameVar = Database.cleanSQL(usernameVar);
		passwordVar = Database.cleanSQL(passwordVar);

		try {
			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();
			
			// check if is admin
			adminCheck(usernameVar, passwordVar, dbcon, response, mySession);
			
			// check if is customer
			userCheck(usernameVar, passwordVar, dbcon, request, response, mySession);
			
			// close mySQL pooling
			dbcon.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession mySession = request.getSession();
		User thisUser = (User) mySession.getAttribute("user_object");
		Admin thisAdmin = (Admin) mySession.getAttribute("admin_object");
		if ((thisUser != null) || (thisAdmin != null)){
			response.sendRedirect("/project2_8/servlet/search");
			return;
		}
		// User/Admin is in the system, we can continue safely.

		// Forward to index.jsp
		RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
		view.forward(request, response);
	}

	public static void LoginCheck(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
		
		HttpSession mySession = request.getSession();
		User thisUser = (User) mySession.getAttribute("user_object");
		Admin thisAdmin = (Admin) mySession.getAttribute("admin_object");
		if ((thisUser == null) && (thisAdmin == null)) {
			response.sendRedirect("/project2_8/servlet/index");
			return;
		}
		// User/Admin is in the system, we can continue safely.
	}

	public void adminCheck(String usernameVar, String passwordVar, Connection dbcon, HttpServletResponse response, HttpSession mySession)throws IOException, ServletException {
		
		// check if user is admin
		try{
			Statement statement_admin = dbcon.createStatement();
			String query_admin = "SELECT * from employees where email = '"
					+ usernameVar + "'";
			ResultSet rs_admin = statement_admin.executeQuery(query_admin);
			
			Admin thisAdmin = new Admin();
			if(rs_admin.next()){
				thisAdmin.email = rs_admin.getString("email");
				thisAdmin.password = rs_admin.getString("password");
				thisAdmin.fullname = rs_admin.getString("fullname");
			}
			rs_admin.close();
			statement_admin.close();
			
			if(passwordVar.equals(thisAdmin.password)){
				mySession.setAttribute("admin_object", thisAdmin);
				response.sendRedirect("/project2_8/servlet/search");
				dbcon.close();
				return;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void userCheck(String usernameVar, String passwordVar, Connection dbcon, HttpServletRequest request, HttpServletResponse response, HttpSession mySession) throws IOException, ServletException {
		
		// check if user is customer
		try{		
			Statement statement = dbcon.createStatement();
			String query = "SELECT * from customers where email = '"
					+ usernameVar + "'";
			ResultSet rs = statement.executeQuery(query);

			User thisUser = new User();

			if (!rs.next()) { // invalid email
				loginFail(request, response, mySession);
			}

			// correct email
			thisUser.id = rs.getInt("id");
			thisUser.name = rs.getString("first_name");
			thisUser.username = rs.getString("email");
			thisUser.password = rs.getString("password");

			if (!passwordVar.equals(thisUser.password)) { //invalid password
				loginFail(request, response, mySession);
			}

			// set session "user_object"
			mySession.setAttribute("user_object", thisUser);

			// redirect to /servlet/search
			response.sendRedirect("/project2_8/servlet/search");

			rs.close();
			statement.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void loginFail(HttpServletRequest request,
			HttpServletResponse response, HttpSession mySession)
			throws IOException, ServletException {
		// set error login message
		String loginErr = "Unvalid username or password.";
		// set session "error_message"
		mySession.setAttribute("login_Err", loginErr);

		doGet(request, response);
	}
}