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

public class Sort extends HttpServlet{

	public String getServletInfo() {
		return "Sorting on servlet";
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
		responseWriteChannel.println("Sort: doPost");
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// get parameters
		String order = request.getParameter("order");
		order = Database.cleanSQL(order);

		// get current movie list
		List<Movie> movieList = (List<Movie>)mySession.getAttribute("movie_list");
    	if(movieList == null){
        	movieList = new ArrayList<Movie>();
    	}

    	// sorting
    	movieList = sort(movieList, order);

		// set session movie_list
		mySession.setAttribute("movie_list", movieList);

		// forward to main.jsp
		RequestDispatcher view = request.getRequestDispatcher("/html/main.jsp");
		view.forward(request, response);
	}

	public List<Movie> sort(List<Movie> movieList, String order){
		
		// Sorting
		if(order.equals("titleA")){
			Collections.sort(movieList, new Comparator<Movie>() {
            	//@Override
            	public int compare(Movie m1, Movie m2) {
                	return m1.title.compareToIgnoreCase(m2.title);
  		    	}
  			});
		}else if(order.equals("titleD")){
			Collections.sort(movieList, new Comparator<Movie>() {
            	//@Override
            	public int compare(Movie m1, Movie m2) {
                	return m2.title.compareToIgnoreCase(m1.title);
  		    	}
  			});
		}else if(order.equals("yearA")){
			Collections.sort(movieList, new Comparator<Movie>() {
            	//@Override
            	public int compare(Movie m1, Movie m2) {
            		if(m1.year > m2.year){
            			return 1;
            		}else if(m1.year < m2.year){
            			return -1;
            		}else{
            			return 0;
            		}
  		    	}
  			});
		}else{
			Collections.sort(movieList, new Comparator<Movie>() {
            	//@Override
            	public int compare(Movie m1, Movie m2) {
                	if(m2.year > m1.year){
            			return 1;
            		}else if(m2.year < m1.year){
            			return -1;
            		}else{
            			return 0;
            		}
  		    	}
  			});
		}

		return movieList;
	}
}
