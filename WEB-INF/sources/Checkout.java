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

public class Checkout extends HttpServlet {

	public String getServletInfo() {
		return "checkout";
	}

	// Use http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// get session info
		HttpSession mySession = request.getSession();
		// login check
		Index.LoginCheck(request, response);
		
		// Forward to checkout.jsp
		RequestDispatcher view = request.getRequestDispatcher("/html/checkout.jsp");
		view.forward(request, response);
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
			String fn = request.getParameter("fn");
			String ln = request.getParameter("ln");
			String cc_id = request.getParameter("cc_id");
			String e_date = request.getParameter("e_date");
			fn = Database.cleanSQL(fn);
			ln = Database.cleanSQL(ln);
			cc_id = Database.cleanSQL(cc_id);
			e_date = Database.cleanSQL(e_date);
			
			// Declare our statement
			Statement statement = dbcon.createStatement();
			// construct the query sentence
			String query = "SELECT * from creditcards where id = '"+ cc_id +"' ";
			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			// check cc info to checkout or forward to checkout.jsp with err
			checkCreditcardInfo(fn, ln, e_date, rs, dbcon, request, response, mySession);
			
			rs.close();
			statement.close();
			dbcon.close();
			
			// Forward to checkout.jsp
			RequestDispatcher view = request.getRequestDispatcher("/html/checkout.jsp");
			view.forward(request, response);
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

	public void checkCreditcardInfo(String fn, String ln, String e_date, ResultSet rs, Connection dbcon, HttpServletRequest request, HttpServletResponse response, HttpSession mySession)throws IOException, ServletException{
		
		try{	
			if(!rs.next()){	// Creditcard info does not exist
				// set checkout message
				String checkoutMessage = "Creditcard info does not exist.";
				mySession.setAttribute("checkout_Message", checkoutMessage);
				// Forward to checkout.jsp
				RequestDispatcher view = request.getRequestDispatcher("/html/checkout.jsp");
				view.forward(request, response);
				return;
			}else{
				// get creditcard info
				String first_name = rs.getString("first_name");
				String last_name = rs.getString("last_name");
				String expiration = rs.getString("expiration");
				
				if(fn.equals(first_name)&&ln.equals(last_name)&&e_date.equals(expiration)){ // correct creditcard info
					
					finishCheckout(dbcon, mySession);

				}else{ // invalid creditcard info
					// set checkout message
					String checkoutMessage = "Invalid creditcard information.";
					mySession.setAttribute("checkout_Message", checkoutMessage);
					// Forward to checkout.jsp
					RequestDispatcher view = request.getRequestDispatcher("/html/checkout.jsp");
					view.forward(request, response);
					return;
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void finishCheckout(Connection dbcon, HttpSession mySession)throws IOException, ServletException{
		User thisUser = (User) mySession.getAttribute("user_object");
		
		try{
			// get this user's shopping cart info
			List<Cart> shoppingCart = getShoppingCart(thisUser.id, dbcon);
			
			// iterate each movie 
			for(int i = 0; i < shoppingCart.size(); i++){
  				// get the info of this user and this movie
  				Cart c = shoppingCart.get(i);    
  				
  				// iterate quantity-times to insert into sales_table  				
  				for(int j = 0; j < c.quantity; j++){
  					// Declare our statement
  					Statement statement_insert = dbcon.createStatement();
  					// construct the query_insert sentence
					String query_insert = "INSERT INTO sales (customer_id, movie_id) VALUES("+ thisUser.id +", "+ c.movieId +")";
					// Perform the query
					statement_insert.executeUpdate(query_insert);
					statement_insert.close();
  				}
  				
  				// delete from shoppingcart_table
  				// Declare our statement
  				Statement statement_remove = dbcon.createStatement();
  				// construct the query_remove sentence
				String query_remove = "delete from shoppingcart where customer_id = "+ thisUser.id +" and movie_id = "+ c.movieId +" ";
				// Perform the query
				statement_remove.executeUpdate(query_remove);
				statement_remove.close();
  			}

  			// set checkout message
  			String checkoutMessage = "Successfully checkouted.";
			mySession.setAttribute("checkout_Message", checkoutMessage);
			mySession.removeAttribute("shopping_cart");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}					
	}
}
