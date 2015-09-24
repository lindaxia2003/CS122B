  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <%
        for(int i = 0; i < movieList.size(); i++){
          Movie m = movieList.get(i);
      %>
        <tr>
          <td><img src='<%= m.banner_url%>' height="300" width="200"></td>
          <td><%= m.title %></td>
          <td><%= m.year %></td>
          <td><%= m.director %></td>
          <td><a href='<%= m.trailer_url%>' target="_blank">Watch Trailer</a></td>
        </tr>
        <tr>
          <%
            for(int k = 0; k < m.genresInMovie.size(); k++){
            String genre = m.genresInMovie.get(k);
          %>
            <td><%= genre %></td>
          <%
            }
          %>
        </tr>
        <tr>
          <%
            for(int j = 0; j < m.starsInMovie.size(); j++){
            Star s = m.starsInMovie.get(j);
          %>
            <td><a href="/project2_8/servlet/star?starId=<%= s.id %>"><%= s.fn %> <%= s.ln %></a></td>
          <%
            }
          %>
        </tr>
        <tr>
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