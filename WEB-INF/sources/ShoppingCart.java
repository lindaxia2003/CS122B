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

public class ShoppingCart extends HttpServlet {

	public String getServletInfo() {
		return "manipulate fabflix_shoppingcart table";
	}

	// Use http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		User thisUser = (User) mySession.getAttribute("user_object");
		// login check
		Index.LoginCheck(request, response);

		try{
			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();

			// get parameters
			String update = request.getParameter("update");
			update = Database.cleanSQL(update);
			Integer movieId = Integer.valueOf(request.getParameter("movieId"));

			if (update.equals("yes")){ // this is an update operation

				updateShoppingCart(movieId, request, dbcon, mySession);
								
			}else{ // this is a remove operation
				
				removeRecordInShoppingCart(movieId, dbcon, mySession);
			}

			// get shopping cart info
			List<Cart> shoppingCart = getShoppingCart(thisUser.id, dbcon);
			
			// set session shopping_cart
			mySession.setAttribute("shopping_cart", shoppingCart);

			// forward to shoppingCart.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/shoppingCart.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// get session info
		HttpSession mySession = request.getSession();
		User thisUser = (User) mySession.getAttribute("user_object");
		// login check
		Index.LoginCheck(request, response);

		try{
			// Open context for mySQL pooling
			Connection dbcon = Database.openConnection();

			String addToCart = request.getParameter("addToCart");
			addToCart = Database.cleanSQL(addToCart);

			addToCart(addToCart, request, dbcon, mySession);

			// get shopping cart info
			List<Cart> shoppingCart = getShoppingCart(thisUser.id, dbcon);
			
			// set session shopping_cart
			mySession.setAttribute("shopping_cart", shoppingCart);

			// forward to shoppingCart.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/shoppingCart.jsp");
			view.forward(request, response);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void updateShoppingCart(Integer movieId, HttpServletRequest request, Connection dbcon, HttpSession mySession)throws IOException, ServletException{
		User thisUser = (User) mySession.getAttribute("user_object");
		
		//get parameter
		Integer quantity_update = Integer.valueOf(request.getParameter("quantity_update"));
		try{
			if(quantity_update > 0){ // only positive quantity allowed
				// Declare our statement
				Statement statement = dbcon.createStatement();
				// construct query sentence
				String query = "SELECT * from shoppingcart where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" ";
				// Perform the query
				ResultSet rs = statement.executeQuery(query);			
				 
				if(!rs.next()){ // if the record of this movie no longer exists
						;
				}else{ // if the record of this movie is in the table
					// get current quantity
					int quantity = rs.getInt("quantity");
					// update quantity
					quantity = quantity_update;
					// Declare our statement
					Statement statement_updateQuantity = dbcon.createStatement();
					// construct query_update sentence
					String query_updateQuantity = "UPDATE shoppingcart SET quantity = "+ quantity +" where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" ";
					// Perform the query_update
					statement_updateQuantity.executeUpdate(query_updateQuantity);
					statement_updateQuantity.close();
				}
				rs.close();
				statement.close();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}


	public void removeRecordInShoppingCart(Integer movieId, Connection dbcon, HttpSession mySession)throws IOException, ServletException{
		User thisUser = (User) mySession.getAttribute("user_object");
		
		try{
			// Declare our statement
			Statement statement = dbcon.createStatement();
			// construct query sentence
			String query = "SELECT * from shoppingcart where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" ";
			// Perform the query
			ResultSet rs = statement.executeQuery(query);
		 
			if(!rs.next()){ // if the record of this movie no longer exists
					;
			}else{ // if the record of this movie is in the table
				// Declare our statement
				Statement statement_remove = dbcon.createStatement();
				// construct query_remove sentence
				String query_remove = "delete from shoppingcart where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" ";
				// Perform the query_remove
				statement_remove.executeUpdate(query_remove);
				statement_remove.close();
			}
			rs.close();
			statement.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}


	public List<Cart> getShoppingCart(Integer userId, Connection dbcon) {

		List<Cart> shoppingCart = new ArrayList<>();

		try {
			// Declare our statement
			Statement statement = dbcon.createStatement();
			// construct the query sentence
			String query = "SELECT * from shoppingcart where customer_id = " + userId + " ";
			
			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			// Iterate through each row of rs
			while (rs.next()) {
				Cart c = new Cart();
				c.movieId = rs.getInt("movie_id");
				c.movieTitle = rs.getString("movie_title");
				c.quantity = rs.getInt("quantity");
				shoppingCart.add(c);
			}
			rs.close();
			statement.close();
			dbcon.close();

			return shoppingCart;
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

		catch (java.lang.Exception ex) {
			System.out.println("Exception");
			return shoppingCart;
		}
		return shoppingCart;
	}

	public void addToCart(String addToCart, HttpServletRequest request, Connection dbcon, HttpSession mySession)throws IOException, ServletException{

		User thisUser = (User) mySession.getAttribute("user_object");
		try{

			if(addToCart.equals("yes")){ // if this is an add operation
				// get parameters
				Integer movieId = Integer.valueOf(request.getParameter("movieId"));
				String movieTitle = request.getParameter("movieTitle");
				movieTitle = Database.cleanSQL(movieTitle);
			
				// Declare our statement
				Statement statement = dbcon.createStatement();
				// construct the query sentence
				String query = "SELECT * from shoppingcart where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" and movie_title = '"+ movieTitle +"'";
				// Perform the query
				ResultSet rs = statement.executeQuery(query);
			 
				if(!rs.next()){ // if no same movie in the table
					Statement statement_insert = dbcon.createStatement();
					String query_insert = "INSERT INTO shoppingcart VALUES("+ thisUser.id +", "+ movieId +", '"+ movieTitle +"', 1)";
					statement_insert.executeUpdate(query_insert);
					statement_insert.close();
				}else{ // if exists same movie
					int quantity = rs.getInt("quantity");
					// update quantity
					if(quantity > 0){
						quantity += 1;
						// Declare our statement
						Statement statement_updateQuantity = dbcon.createStatement();
						// construct the query_update sentence
						String query_updateQuantity = "UPDATE shoppingcart SET quantity = "+ quantity +" where customer_id = "+ thisUser.id +" and movie_id = "+ movieId +" and movie_title = '"+ movieTitle +"'";
						// Perform the query
						statement_updateQuantity.executeUpdate(query_updateQuantity);
						statement_updateQuantity.close();
					}
				}
				rs.close();
				statement.close();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}
}
