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

public class SingleStar extends HttpServlet {

	public String getServletInfo() {
		return "Search and get the info for a star";
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
		responseWriteChannel.println("SingleStar: doPost");
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
			Integer searchInputInt = Integer.valueOf(request.getParameter("starId"));
			
			// get star info
			Star thisStar = getStarInfo(searchInputInt, dbcon);
		
			// set session this_Star
			mySession.setAttribute("this_Star", thisStar);

			// forward to star.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/star.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	public Star getStarInfo(Integer searchInputInt, Connection dbcon) {

		Star thisStar = new Star();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();

			// construct the query sentence
			String query = "SELECT * from stars where id = " + searchInputInt +" ";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);		
			
			// Iterate through each row of rs
			while(rs.next()){
				thisStar.id = rs.getInt("id");
				thisStar.fn = rs.getString("first_name");
				thisStar.ln = rs.getString("last_name");
				thisStar.dob = rs.getString("dob");
				thisStar.photo_url = rs.getString("photo_url");

				// get featured movies
				Statement statement_featuredMovies = dbcon.createStatement();
				String query_featuredMovies = "SELECT DISTINCT * FROM movies m LEFT OUTER JOIN stars_in_movies s ON movie_id=m.id WHERE star_id = "+thisStar.id+" ";
				ResultSet rs_featuredMovies = statement_featuredMovies.executeQuery(query_featuredMovies);
				// Iterate through each row of rs_featuredMovies
				while(rs_featuredMovies.next()){
					int featuredMovieId = rs_featuredMovies.getInt("id");
					String featuredMovieTitle = rs_featuredMovies.getString("title");
					thisStar.featuredMovies.add(featuredMovieId);
					thisStar.featuredMoviesTitle.add(featuredMovieTitle);
				}
				rs_featuredMovies.close();
				statement_featuredMovies.close();
			}
			rs.close();
			statement.close();
			dbcon.close();

			return thisStar;
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

		catch (java.lang.Exception ex) {
			System.out.println("Exception");
			return thisStar;
		}
		return thisStar;
	}
}
