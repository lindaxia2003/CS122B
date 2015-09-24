package MyClasses;
import java.util.*;

public class Movie {

	public Movie(){
		
		id = year = 0; 
		title = director = banner_url = trailer_url = "";
	}

	public int id;
	public String title;
	public int year;
	public String director;
	public String banner_url;
	public String trailer_url;
	public List<Star> starsInMovie = new ArrayList<>();
	public List<String> genresInMovie = new ArrayList<>();
	
}	
