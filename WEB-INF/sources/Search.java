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

public class Search extends HttpServlet {

	public String getServletInfo() {
		return "Search in fabflix_movies table";
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
		responseWriteChannel.println("Search: doPost");
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
			String searchInputVar = request.getParameter("searchInput");
        	if(searchInputVar == null){
        		searchInputVar = "";
        	}
        	searchInputVar = Database.cleanSQL(searchInputVar);

        	// get Search result
			List<Movie> movieList = getListOfMovies(searchInputVar, dbcon);
			
			// set session movie_list
			mySession.setAttribute("movie_list", movieList);

			// forward to main.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/main.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public List<Movie> getListOfMovies(String searchInputVar, Connection dbcon) {

		List<Movie> movieList = new ArrayList<>();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();

			// construct the query sentence
			String query;
			if(searchInputVar.equals("")){
				query = "SELECT * from movies";
			}else{
				query = "SELECT * from movies where title like '%" + searchInputVar + "%'";
			}
			// Perform the query
			ResultSet rs = statement.executeQuery(query);
			
			// get movielist
			movieList = searchedMovieList(rs, dbcon);

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

	public static List<Movie> searchedMovieList(ResultSet rs, Connection dbcon){

		List<Movie> movieList = new ArrayList<>();
		try{
			// Iterate through each row of rs
			while (rs.next()) {
				Movie m = new Movie();
				m.id = rs.getInt("id");
				m.year = rs.getInt("year");
				m.title = rs.getString("title");
				m.director = rs.getString("director");
				m.banner_url = rs.getString("banner_url");
				m.trailer_url = rs.getString("trailer_url");

				// get stars in this movie
				Statement statement_star = dbcon.createStatement();
				String query_starsInMovie = "SELECT DISTINCT * FROM stars s LEFT OUTER JOIN stars_in_movies sm ON sm.star_id=s.id where sm.movie_id = '" + m.id + "'";
				ResultSet rs_stars = statement_star.executeQuery(query_starsInMovie);
				// Iterate through each row of rs_stars to get star info
				while(rs_stars.next()){
					Star s = new Star();
					s.id = rs_stars.getInt("id");
					s.fn = rs_stars.getString("first_name");
					s.ln = rs_stars.getString("last_name");
					s.dob = rs_stars.getString("dob");
					s.photo_url = rs_stars.getString("photo_url");
					m.starsInMovie.add(s);	
				}
				rs_stars.close();
				statement_star.close();

				// get genres of this movie
				Statement statement_genres = dbcon.createStatement();
				String query_genresOfThisMovie = "SELECT DISTINCT * FROM genres g LEFT OUTER JOIN genres_in_movies gm ON gm.genre_id=g.id where gm.movie_id = '" + m.id + "'";
				ResultSet rs_genres = statement_genres.executeQuery(query_genresOfThisMovie);
				// Iterate through each row of rs_genres to get genres info
				while(rs_genres.next()){
					String g = rs_genres.getString("name");
					m.genresInMovie.add(g);
				}
				rs_genres.close();
				statement_genres.close();
			
				// add movie to movieList
				movieList.add(m);
			}

			return movieList;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return movieList;
	}		
}
