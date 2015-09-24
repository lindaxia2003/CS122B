  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <h1>Found <%= movieList.size() %> result(s).</h1>
      <%
        for(int i = 0; i < movieList.size(); i++){
          Movie m = movieList.get(i);
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