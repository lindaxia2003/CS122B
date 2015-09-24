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

public class BrowseByGenres extends HttpServlet {

	public String getServletInfo() {
		return "Browse by Genres in Fabflix";
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
		responseWriteChannel.println("BrowseByGenres: doPost");
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

			// get parameters 
			Integer searchInputInt = Integer.valueOf(request.getParameter("genre"));

			// get browsed(by genre) movie list
			List<Movie> movieList = getListOfMovies(searchInputInt, dbcon);

			// set session "movie_list"
			mySession.setAttribute("movie_list", movieList);

			// forward to main page
			RequestDispatcher view = request.getRequestDispatcher("/html/main.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public List<Movie> getListOfMovies(Integer searchInputInt, Connection dbcon) {

		List<Movie> movieList = new ArrayList<>();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();

			// construct the query sentence
			String query = "SELECT DISTINCT * FROM movies m LEFT OUTER JOIN genres_in_movies gm ON gm.movie_id=m.id where gm.genre_id = " + searchInputInt + " ";

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

}
