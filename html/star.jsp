  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
        <tr>
          <td><img src='<%= thisStar.photo_url%>' height="300" width="200"></td>
          <td><%= thisStar.fn %> <%= thisStar.ln %></td>
          <td><%= thisStar.dob %></td>
        </tr>
        <tr>
          <%
            for(int i = 0; i < thisStar.featuredMovies.size(); i++){
            int movieId = thisStar.featuredMovies.get(i);
            String movieTitle = thisStar.featuredMoviesTitle.get(i);
          %>
            <td><a href="/project2_8/servlet/singleMovie?movieId=<%= movieId %>"><%= movieTitle %></a></td>
          <%
            }
          %>
        </tr>
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