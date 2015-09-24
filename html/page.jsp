  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>

  <%
    List<Movie> movieListPaged = (List<Movie>)session.getAttribute("movie_list_paged");
    if(movieListPaged == null){
        movieListPaged = new ArrayList<Movie>();
    }
  %>

  <!-- end Header -->
  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <h1>Found <%= movieList.size() %> result(s).</h1> 

      <h1>This is page <%= index %>/ <%= movieList.size()/resultPerPage + (movieList.size() % resultPerPage == 0 ? 0:1) %>.</h1>
      <%
        for(int i = 0; i < movieListPaged.size(); i++){
          Movie m = movieListPaged.get(i);
      %>
        <tr>
          <td><img src='<%= m.banner_url%>' height="150" width="100"></td>
          <td><a href="/project2_8/servlet/singleMovie?movieId=<%= m.id %>"><%= m.title %></a></td>
          <td><%= m.year %></td>
          <td><%= m.director %></td>   
          <%
            if(thisAdmin.email.equals("")){
          %>
                <td><a href="/project2_8/servlet/shoppingCart?addToCart=yes&movieId=<%= m.id %>&movieTitle=<%= m.title%>">Add to Shopping Cart</a></td>
          <%
            }
          %>
        </tr>
      <%
          }
      %>
      </table>
    </div>
    <!-- end Main -->

  <!-- Footer -->
  <%@ include file="footer.jsp" %>
  <!-- end Footer -->
  </div>
  <!-- end Shell -->
</body>
</html>