  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <h1><%= shoppingCart.size() %> Record(s).</h1>
      <%
        for(int i = 0; i < shoppingCart.size(); i++){
          Cart c = shoppingCart.get(i);
      %>
        <tr>
          <td><a href="/project2_8/servlet/singleMovie?movieId=<%= c.movieId %>"><%= c.movieTitle %></a></td>
          <td>
            <form method="post" action="/project2_8/servlet/shoppingCart?update=yes&movieId=<%= c.movieId %>">
                <input name="quantity_update" type="text" value='<%= c.quantity%>' class="input-small">
                <button type="submit" class="btn btn-success">Update</button>
            </form>
            <form method="post" action="/project2_8/servlet/shoppingCart?update=no&movieId=<%= c.movieId %>">
                <button type="submit" class="btn btn-success">Remove</button>
            </form>
          </td>
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