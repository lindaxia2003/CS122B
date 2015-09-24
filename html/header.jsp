<%@ page import="MyClasses.*,java.util.*" %>

<%
  Integer index = (Integer) session.getAttribute("index");
  if(index == null){
      index = 0;
  }
  Integer resultPerPage = (Integer) session.getAttribute("result_Per_Page");
  if(resultPerPage == null){
    resultPerPage = 5;
  }
  List<Movie> movieList = (List<Movie>)session.getAttribute("movie_list");
  if(movieList == null){
      movieList = new ArrayList<Movie>();
  }
  List<Genre> genreList = (List<Genre>)session.getAttribute("genre_list");
  if(genreList == null){
      genreList = new ArrayList<Genre>();
  }
  List<String> titleList = (List<String>)session.getAttribute("title_list");
  if(titleList == null){
      titleList = new ArrayList<String>();
  }
  List<Cart> shoppingCart = (List<Cart>)session.getAttribute("shopping_cart");
  if(shoppingCart == null){
      shoppingCart = new ArrayList<Cart>();
  }
  Star thisStar = (Star)session.getAttribute("this_Star");
  if(thisStar == null){
    thisStar = new Star();
  }
  String checkoutMessage = (String)session.getAttribute("checkout_Message");
    if(checkoutMessage != null){
      session.removeAttribute("checkout_Message");
  }
  ArrayList<String> addMovieErr = (ArrayList<String>) session.getAttribute("addMovie_err");
    if(addMovieErr != null){
      session.removeAttribute("addMovie_err");
  } 
  Admin thisAdmin = (Admin)session.getAttribute("admin_object");
  if(thisAdmin == null){
    thisAdmin = new Admin();
  }
%>

<!DOCTYPE html >
<html lang="en-US" >
<head>
    <title>Febflix</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="/project2_8/css/style.css" type="text/css" media="all" />
    <script type="text/javascript" src="/project2_8/js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="/project2_8/js/jquery-func.js"></script>
</head>

<body>
<!-- Shell -->
<div id="shell">

  <div id="header">

    <h1 id="logo">
      <a href="/project2_8/servlet/index">  </a>
    </h1>

    <!-- User Navigation -->
    <div id="navigation">
      <ul>
          <li><a href="/project2_8/servlet/index">Home</a></li>
          <li><a href="/project2_8/servlet/advSearch">Advance Search</a></li>
          <li><a href="/project2_8/servlet/showGenres">Browse by Genres</a></li>
          <li><a href="/project2_8/servlet/showTitles">Browse by Titles</a></li>

          <%
            if(thisAdmin.email.equals("")){
          %>
              <li><a href="/project2_8/servlet/shoppingCart?addToCart=no">My Cart</a></li>
              <li><a href="/project2_8/servlet/checkout">Checkout</a></li>
          <%
            }
          %>
          <li><a href="/project2_8/servlet/logout">Log Out</a></li>
      </ul>
    </div>
    <!-- end Navigation -->

    <!-- Admin Navigation -->
    <%
      if(!thisAdmin.email.equals("")){
    %>
      <div id="navigation">
        <ul>
            <li><a href="/project2_8/servlet/addMovie">Add Movie</a></li>
            <li><a href="CheckDB?option=1">Movie Warnings</a></li>
            <li><a href="CheckDB?option=2">Star Warnings</a></li>
            <li><a href="CheckDB?option=3">Genre Management</a></li>
            <li><a href="CheckDB?option=4">Customer Management</a></li>
        </ul>
      </div>
    <%
      }
    %>
    <!-- end Navigation -->
    
    <!-- sub-navigationh -->
    <div id="sub-navigation">
      <div id="search" >
        <form action="/project2_8/servlet/search" method="get" accept-charset="utf-8">
          <label for="search-field">SEARCH</label>          
          <input type="text" name="searchInput" value="Enter search here" id="search-field" title="Enter search here" class="blink search-field"  />
          <input type="submit" value="GO!" class="search-button" />
        </form>
      </div>
    </div>
    <!-- end sub-navigation -->

    <!-- start of sort/pagination -->  
    <div id="sort">
      <ul>
          <li><a href="/project2_8/servlet/sort?order=titleA">AscTitle</a></li>
          <li><a href="/project2_8/servlet/sort?order=titleD">DescTitle</a></li>
          <li><a href="/project2_8/servlet/sort?order=yearA">AscYear</a></li>
          <li><a href="/project2_8/servlet/sort?order=yearD">DescYear</a></li>
          <li><a href="/project2_8/servlet/page?rpp=5">5rpp</a></li>
          <li><a href="/project2_8/servlet/page?rpp=20">20rpp</a></li>
          <li><a href="/project2_8/servlet/page?rpp=100">100rpp</a></li>
          <% 
            if(index > 1){
          %>
            <li><a href="/project2_8/servlet/page?pageAction=prev">PrevPage</a></li>
          <%
            }
          %>
          <% 
            if(((index*resultPerPage -1) < movieList.size()) && index > 0){
          %>
            <li><a href="/project2_8/servlet/page?pageAction=next">NextPage</a></li>
          <%
            }
          %>
      </ul>
    </div>
  </div>
