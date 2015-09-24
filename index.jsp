  <%@ page import="MyClasses.*,java.util.*" %>
  <% 
    String loginErr = (String)session.getAttribute("login_Err");
    if(loginErr != null){
      session.removeAttribute("login_Err");
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
        <h1 id="logo"><a href="/project2_8/servlet/index"></a></h1>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
      </div>
      <!-- Login -->
      <div align="left" id="login">
        <br>
        <br>
        <h1 >Please Login</h1>
        <br>
        
        <%
          if(loginErr != null){
        %>
            <hr/>
            <span style= "color:red"><%= loginErr %></span>
            <hr/>
        <%
          }
        %>

        <form action="/project2_8/servlet/index" method="post">
          <div class="login-row">
            <label>Username </label> <input type="text" name="username" placeholder="Username" required autofocus/> <br />
          </div>
          <div class="login-row">
            <label>Password </label> <input type="password" name="password" placeholder="Password" required /> <br />
          </div>
          <div class="login-row">     
            <input type="submit" value="Login" class="login-button">
          </div>
        </form>
        <br>
        <br>
      </div>
      <!-- end Login -->
      <br>
      <div align="left">
        <p> &copy; 2015 Fabflix. Designed by Group 8, CS122b, UCI. </a></p>
      </div>
    </div>
    <!-- end Shell -->
  </body>
</html>