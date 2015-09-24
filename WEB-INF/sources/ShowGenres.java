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

public class ShowGenres extends HttpServlet {

	public String getServletInfo() {
		return "Show genres of movies";
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
		responseWriteChannel.println("ShowGenres: doPost");
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
			
			// get genre list
			List<Genre> genreList = getListOfGenres(dbcon);
			// set session genre_list
			mySession.setAttribute("genre_list", genreList);

			// forward to genres.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/genres.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public List<Genre> getListOfGenres(Connection dbcon) {

		List<Genre> genreList = new ArrayList<>();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();

			// construct the query sentence
			String query = "SELECT * from genres";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			// Iterate through each row of rs
			while (rs.next()) {
				Genre g = new Genre();
				g.id = rs.getInt("id");
				g.name = rs.getString("name");
				genreList.add(g);
			}
			rs.close();
			statement.close();
			dbcon.close();

			return genreList;
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

		catch (java.lang.Exception ex) {
			System.out.println("Exception");
			return genreList;
		}
		return genreList;
	}
}
