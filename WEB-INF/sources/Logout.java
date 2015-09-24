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

public class Logout extends HttpServlet {
	public String getServletInfo() {
		return "Servlet Logout";
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession mySession = request.getSession();
		User thisUser = (User) mySession.getAttribute("user_object");
		Admin thisAdmin = (Admin) mySession.getAttribute("admin_object");
		if ((thisUser == null) && (thisAdmin == null)) {
			// already logged out
		} else {
			// invalidate all sessions
			mySession.invalidate();
		}

		// redirect to /servlet/index
		response.sendRedirect("/project2_8/servlet/index");
	}
}