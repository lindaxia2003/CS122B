  <%@ page import="MyClasses.*,java.util.*" %> 
  <%@ include file="header.jsp" %>
  <!-- end Header -->
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <%
    if(checkoutMessage != null){
  %>
      <hr/>
      <span style= "color:red"><%= checkoutMessage %></span>
      <hr/>
  <%
    }
  %>

  <%
    if(shoppingCart.size() > 0){      
  %>
      <br>
      <div id="advance-search">
        <br>
        <br>   
        <form action="/project2_8/servlet/checkout" method="post">
          <h1 >Checkout</h1> <br>
          <div class="row" >
            <label> First Name: </label> 
            <input type="TEXT" name="fn"><br>
          </div>
          <div class="row" >
            <label> Last Name: </label>  
            <input type="TEXT" name="ln"><br>
          </div>
          <div class="row" >
            <label> Creditcard Number: </label>  
            <input type="TEXT" name="cc_id"><br>
          </div>
          <div class="row" >
            <label> Expiration Date:  </label> 
            <input type="TEXT" name="e_date" placeholder="YYYY-MM-DD"><br>
          </div>
          <div class="row" >
            <input type="SUBMIT" value=" Checkout " class="button"> 
            <input type="RESET" value="  Reset  " class="button"> 
          </div>
        </form>
      </div>
      <!-- End AdvanceSearch -->
  <%
    }else{
  %>
      <h1>Empty shopping cart.</h1>
  <%
    }
  %>
  <!-- Footer -->
  <%@ include file="footer.jsp" %>
  <!-- end Footer -->
</div>
<!-- end Shell -->
</body>
</html>