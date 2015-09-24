package MyClasses;
import java.util.*;

public class Star {

	public Star(){
		
		id = 0; 
		fn = ln = dob = photo_url = "";
	}

	public int id;
	public String fn;
	public String ln;
	public String dob;
	public String photo_url;
	public List<Integer> featuredMovies = new ArrayList<>();
	public List<String> featuredMoviesTitle = new ArrayList<>();
	
}	
