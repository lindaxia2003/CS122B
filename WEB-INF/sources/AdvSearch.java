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

public class AdvSearch extends HttpServlet {

	public String getServletInfo() {
		return "Advanced search in Fabflix";
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// Forward to advsearch.jsp
		RequestDispatcher view = request.getRequestDispatcher("/html/advsearch.jsp");
		view.forward(request, response);
	}

	// Use http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		try {			
			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();

			// get parameters 
			String title = request.getParameter("title");
			Integer year = 0;
			try {
				year = Integer.valueOf(request.getParameter("year"));
			} catch (Exception e) {
				year = 0;
			}
			String director = request.getParameter("director");
			String fn = request.getParameter("fn");
			String ln = request.getParameter("ln");
			String sub = request.getParameter("sub");
			if (sub == null) {
				sub = "off";
			}
			title = Database.cleanSQL(title);
			director = Database.cleanSQL(director);
			fn = Database.cleanSQL(fn);
			ln = Database.cleanSQL(ln);

			// get advSearch result
			List<Movie> movieList = getListOfMovies(title, year, director, fn, ln,
					sub, dbcon);

			// set session "movie_list"
			mySession.setAttribute("movie_list", movieList);

			// forward to main page
			RequestDispatcher view = request.getRequestDispatcher("/html/main.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public List<Movie> getListOfMovies(String title, Integer year,
			String director, String fn, String ln, String sub, Connection dbcon)
			throws IOException, ServletException {

		List<Movie> movieList = new ArrayList<>();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();
			// construct query sentence
			String query = constructQuery(title, year, director, fn, ln, sub);
			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			// get movielist
			movieList = Search.searchedMovieList(rs, dbcon);

			rs.close();
			statement.close();
			dbcon.close();

			return movieList;

		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException
		
		catch (java.lang.Exception ex) {
			System.out.println("Exception");
			return movieList;
		}
		return movieList;
	}

	public String constructQuery(String title, Integer year, String director, String fn, String ln, String sub){

		String searchMsg = "";
		String query = "";
		Boolean firstCond = true;

		// construct the query sentence
		if (!(title == null || title.isEmpty())) {
			if (firstCond) {
				firstCond = false;
			} else {
				searchMsg += " AND ";
			}
			if (sub.equals("on")) {
				searchMsg += "title like '%" + title + "%' ";
			} else {
				searchMsg += "title = '" + title + "' ";
			}
		}

		if (year != 0) {
			if (firstCond) {
				firstCond = false;
			} else {
				searchMsg += " AND ";
			}
			searchMsg += "year = " + year + " ";
		}

		if (!(director == null || director.isEmpty())) {
			if (firstCond) {
				firstCond = false;
			} else {
				searchMsg += " AND ";
			}
			if (sub.equals("on")) {
				searchMsg += "director like '%" + director + "%' ";
			} else {
				searchMsg += "director = '" + director + "'";
			}
		}

		if (!(fn == null || fn.isEmpty())) {
			if (firstCond) {
				firstCond = false;
			} else {
				searchMsg += " AND ";
			}
			if (sub.equals("on")) {
				searchMsg += "first_name like '%" + fn + "%' ";
			} else {
				searchMsg += "first_name = '" + fn + "'";
			}
		}
		
		if (!(ln == null || ln.isEmpty())) {
			if (firstCond) {
				firstCond = false;
			} else {
				searchMsg += " AND ";
			}
			if (sub.equals("on")) {
				searchMsg += "last_name like '%" + ln + "%' ";
			} else {
				searchMsg += "last_name = '" + ln + "'";
			}
		}

		query = "SELECT DISTINCT m.id,title,year,director,banner_url,trailer_url FROM movies m LEFT OUTER JOIN stars_in_movies s ON movie_id=m.id LEFT OUTER JOIN stars s1 ON s.star_id=s1.id WHERE "
				+ searchMsg;

		return query;
	}
}
