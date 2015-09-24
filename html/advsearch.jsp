  <%@ page import="MyClasses.*,java.util.*" %> 
  <%@ include file="header.jsp" %>
  <!-- end Header -->
  
  <!-- AdvanceSearch -->
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <div id="advance-search">
    <br>
    <br>   
    <form action="/project2_8/servlet/advSearch" method="POST">
      <h1 >Advance Search</h1> <br>
      <div class="row" >
        <label> Title: </label> 
        <input type="TEXT" name="title"><br>
      </div>
      <div class="row" >
        <label> Year: </label>  
        <input type="TEXT" name="year"><br>
      </div>
      <div class="row" >
        <label> Director: </label>  
        <input type="TEXT" name="director"><br>
      </div>
      <div class="row" >
        <label> Star's First Name:  </label> 
        <input type="TEXT" name="fn"><br>
      </div>
      <div class="row" >
        <label> Star's Last Name: </label> 
        <input type="TEXT" name="ln"><br>
      </div>
      <div class="row" >
        <label> Substring Search: </label> 
        <input type="CHECKBOX" name="sub"><br>
      </div>
      <div class="row" >
        <input type="HIDDEN" name="rpp" value="5">
        <input type="SUBMIT" value=" Search " class="button"> 
        <input type="RESET" value="  Reset  " class="button"> 
      </div>
    </form>
  </div>
  <!-- End AdvanceSearch -->

  <!-- Footer -->
  <%@ include file="footer.jsp" %>
  <!-- end Footer -->
</div>
<!-- end Shell -->
</body>
</html>