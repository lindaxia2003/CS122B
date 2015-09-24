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

public class Page extends HttpServlet{

	public String getServletInfo() {
		return "Pagination on servlet";
	}

	// Use http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// This is how you send information to the browser who issues
		// the original call in this sequence of resource execution.
		PrintWriter responseWriteChannel = response.getWriter();
		responseWriteChannel.println("Page: doPost");
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);

		// get parameters
		Integer index = (Integer) mySession.getAttribute("index");
		if (index == null) {
			index = 1;
		}
		String pageAction = request.getParameter("pageAction");
		if(pageAction == null){
			pageAction = "hold";
		}
		Integer resultPerPage = (Integer) mySession.getAttribute("result_Per_Page");
		if(resultPerPage == null){
			resultPerPage = 5;
		}
		
		// update parameters
		if(pageAction.equals("prev")){
			index -= 1;
		}else if(pageAction.equals("next")){
			index += 1;
		}else{
			Integer rpp = 5;
			try {
				rpp = Integer.valueOf(request.getParameter("rpp"));
			} catch (Exception e) {
				rpp = 5;
			}
		
			if(!resultPerPage.equals(rpp)){
				index = 1;
				resultPerPage = rpp;
			};		
		}

		// update session "result_Per_Page", "index"
		mySession.setAttribute("result_Per_Page", resultPerPage);
		mySession.setAttribute("index", index);

		// pagination
		pageMovieList(index, resultPerPage, mySession);

		// forward to page.jsp
		RequestDispatcher view = request.getRequestDispatcher("/html/page.jsp");
		view.forward(request, response);
	}

	public void pageMovieList(Integer index, Integer resultPerPage, HttpSession mySession){
		
		// get current movie list
		List<Movie> movieList = (List<Movie>)mySession.getAttribute("movie_list");
    	if(movieList == null){
        	movieList = new ArrayList<Movie>();
    	}

    	// initiate paged movie list
    	List<Movie> movieListPaged = new ArrayList<>();
    	
    	// get paged movie list
    	for(int i = (index - 1) * resultPerPage; i < Math.min(index * resultPerPage, movieList.size()); i++){
          Movie m = movieList.get(i);
          movieListPaged.add(m);
        }

        // set session "movie_list_paged"
		mySession.setAttribute("movie_list_paged", movieListPaged);
	}
}
