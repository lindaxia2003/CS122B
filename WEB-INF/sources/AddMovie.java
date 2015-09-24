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

public class AddMovie extends HttpServlet {

	public String getServletInfo() {
		return "Add movie into Fabflix";
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// Forward to addMovie.jsp
		RequestDispatcher view = request.getRequestDispatcher("/html/addMovie.jsp");
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
			// get parameters 
			String title = request.getParameter("title");
			Integer year = 0;
			String director = request.getParameter("director");
			String genre = request.getParameter("genre");
			String fn = request.getParameter("fn");
			String ln = request.getParameter("ln");

			title = Database.cleanSQL(title);
			director = Database.cleanSQL(director);
			genre = Database.cleanSQL(genre);
			fn = Database.cleanSQL(fn);
			ln = Database.cleanSQL(ln);

			// get error info
			ArrayList<String> errors = getErrInfo(title, year, director, genre, fn, ln, request, mySession);
			// if exists error, doGet to go to addMovie.jsp
			if (!errors.isEmpty()) {
				doGet(request, response);
			}

			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();
			// excute add_movie procedure
			addMovieProcedure(title, year, director, genre, fn, ln, dbcon);

			// go to added movie page
			showAddedMovie(title, year, director, dbcon, response);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<String> getErrInfo(String title, Integer year, String director, String genre, String fn, String ln, HttpServletRequest request, HttpSession mySession){
		
		// get error info
		ArrayList<String> errors = new ArrayList<String>();

		if (title == null || title.isEmpty()){
			errors.add("Must provide a title.");
			mySession.setAttribute("addMovie_err", errors);
		}

		try{
			year = Integer.valueOf(request.getParameter("year"));
		}catch(Exception e){
			errors.add("Provided year is invalid.");
			mySession.setAttribute("addMovie_err", errors);
		}

		if ( year == 0 ){
			errors.remove("Provided year is invalid.");
			errors.add("Must provide a year.");
			mySession.setAttribute("addMovie_err", errors);
		} 

		if (director == null || director.isEmpty() ){
			errors.add("Must provide a director.");
			mySession.setAttribute("addMovie_err", errors);
		} 

		if (genre == null || genre.isEmpty() ){
			errors.add("Must provide a genre.");
			mySession.setAttribute("addMovie_err", errors);
		}

		if(fn == null || fn.isEmpty() ){
			errors.add("Must provide star's first name.");
			mySession.setAttribute("addMovie_err", errors);
		}	

		if(ln == null || ln.isEmpty()) {
			errors.add("Must provide Star's last name");
			mySession.setAttribute("addMovie_err", errors);
		} 

		return errors;
	}

	public void addMovieProcedure(String title, Integer year, String director, String genre, String fn, String ln, Connection dbcon)throws IOException, ServletException {
		
		try{		
			CallableStatement cst = null;
			dbcon = Database.openConnection();
			cst = dbcon.prepareCall("{call add_movie(?, ?, ?, ?, ?, ?)}");
			cst.setString(1, title);
			cst.setInt(2, year);
			cst.setString(3, director);
			cst.setString(4, fn);
			cst.setString(5, ln);
			cst.setString(6, genre);
			cst.execute();	
			cst.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}

	public void showAddedMovie(String title, Integer year, String director, Connection dbcon, HttpServletResponse response)throws IOException, ServletException{
		
		try{
			Statement statement = dbcon.createStatement();
			String query = "SELECT * FROM movies WHERE title='" + title + "' AND year='"+year+"' AND director='"+ director +"' ";
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				Integer movieId = rs.getInt("id");
				rs.close();
				statement.close();
				// cst.close();
				dbcon.close();
				response.sendRedirect("/project2_8/servlet/singleMovie?movieId=" + movieId);
				return;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}
}
