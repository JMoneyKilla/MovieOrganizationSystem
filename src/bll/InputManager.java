package bll;

import be.Movie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputManager {

    /**
     * Seperates last viewed string into integers of year, month, and day.
     * Checks the current year, month, and day to the last viewed to see if movie was viewed in last 2 years
     * to the day.
     * @param movie
     * @return true if movie was not viewed in last two years and false if it was.
     */
    public boolean isTwoOld(Movie movie){
        Calendar calendar = Calendar.getInstance();
        int viewedYear = Integer.parseInt(movie.lastViewedNoHyphens().substring(0,4));
        int viewedMonth = Integer.parseInt(movie.lastViewedNoHyphens().substring(4,6));
        int viewedDay = Integer.parseInt(movie.lastViewedNoHyphens().substring(6,8));
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        if(currentYear-viewedYear>2)
            return true;
        else if (currentYear-viewedYear==2 && currentMonth>viewedMonth) {
            return true;
        } else if (currentYear-viewedYear==2 && currentMonth==viewedMonth && currentDay>=viewedDay) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given movie has a user rating less than 6
     * @param m
     * @return true if user rating is less than 6 and false if rating is 6 or higher.
     */
    public boolean isRatingTooLow(Movie m){
        if(Double.parseDouble(m.getRating())<6.0)
            return true;
        else{
            return false;
        }
    }

    /**
     * Checks all movies from db for movies that are both too low rating and not viewed in the last 2 years
     * @return list of movies with low rating and 2 year old last viewed.
     */
    public List<Movie> getOldBadMovies(){
        MovieManager mm = new MovieManager();
        List<Movie> allMovies = mm.getAllMovies();
        List<Movie> oldAndBad = new ArrayList<>();
        for (Movie m: allMovies
             ) {
            if(isTwoOld(m) && isRatingTooLow(m)){
                oldAndBad.add(m);
            }
        }
        return oldAndBad;
    }
}
