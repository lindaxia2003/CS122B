  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <h1>There are <%= genreList.size() %> genre(s).</h1>
      <%
        for(int i = 0; i < genreList.size(); i++){
          Genre g  = genreList.get(i);
      %>
        <tr>
          <td><a href="/project2_8/servlet/browseByGenres?genre=<%= g.id %>"><%= g.name %></a></td>
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