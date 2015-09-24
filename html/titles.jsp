  <%@ page import="MyClasses.*,java.util.*" %>
  <%@ include file="header.jsp" %>
  <!-- end Header -->

  <br>
  <br>
  <!-- Main -->
    <div>
      <table style="width:100%" border="1">
      <h1>There are <%= titleList.size() %> title(s).</h1>
      <%
        for(int i = 0; i < titleList.size(); i++){
          String t  = titleList.get(i);
      %>
        <tr>
          <td><a href="/project2_8/servlet/browseByTitles?title=<%= t %>"><%= t %></a></td>
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