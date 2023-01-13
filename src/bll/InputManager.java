package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class InputManager {

    MovieDAO movieDAO = new MovieDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Seperates last viewed string into integers of year, month, and day.
     * Checks the current year, month, and day to the last viewed to see if movie was viewed in last 2 years
     * to the day.
     *
     * @param movie
     * @return true if movie was not viewed in last two years and false if it was.
     */
    public boolean isTwoOld(Movie movie) {
        Calendar calendar = Calendar.getInstance();
        int viewedYear = Integer.parseInt(movie.lastViewedNoHyphens().substring(0, 4));
        int viewedMonth = Integer.parseInt(movie.lastViewedNoHyphens().substring(4, 6));
        int viewedDay = Integer.parseInt(movie.lastViewedNoHyphens().substring(6, 8));
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentYear - viewedYear > 2)
            return true;
        else if (currentYear - viewedYear == 2 && currentMonth > viewedMonth) {
            return true;
        } else if (currentYear - viewedYear == 2 && currentMonth == viewedMonth && currentDay >= viewedDay) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given movie has a user rating less than 6
     *
     * @param m
     * @return true if user rating is less than 6 and false if rating is 6 or higher.
     */
    public boolean isRatingTooLow(Movie m) {
        if (Double.parseDouble(m.getRating()) < 6.0)
            return true;
        else {
            return false;
        }
    }

    /**
     * Checks all movies from db for movies that are both too low rating and not viewed in the last 2 years
     *
     * @return list of movies with low rating and 2 year old last viewed.
     */
    public List<Movie> getOldBadMovies() throws SQLException {
        MovieManager mm = new MovieManager();
        List<Movie> allMovies = mm.getAllMovies();
        List<Movie> oldAndBad = new ArrayList<>();
        for (Movie m : allMovies
        ) {
            if (isTwoOld(m) && isRatingTooLow(m)) {
                oldAndBad.add(m);
            }
        }
        return oldAndBad;
    }

    public List<Movie> searchMovies(String query) {
        List<Movie> movies;
        try {
            movies = movieDAO.getAllMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Movie> filtered = new ArrayList<>();

        for (Movie m : movies) {
            if (m.getName().toLowerCase().contains(query.toLowerCase()))
                filtered.add(m);
        }
        return filtered;
    }

    public List<Movie> searchImdbRating(String query) {
        List<Movie> movies;
        List<Movie> filtered = new ArrayList<>();
        try {
            movies = movieDAO.getAllMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(query.equals(""))
            return movies;

        for (Movie m : movies) {
            if (m.getImdbRating().charAt(0) == query.charAt(0)) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    public List<Movie> searchCategories(String query) throws SQLException {
        boolean isEmpty = true;
        List<Category> categories = categoryDAO.getAllCategories();
        List<Movie> preFilter = new ArrayList<>();
        int category_id;

        if(!query.equals("")){
            isEmpty = false;
        }
        if(isEmpty){
            return movieDAO.getAllMovies();

        }
        else{
           List<String> categoriesTyped = seperateBySpaces(query);
            for (String s: categoriesTyped
                 ) {
                for (Category c: categories
                     ) {
                    if(c.getName().toLowerCase().contains(s.toLowerCase())){
                        category_id = c.getId();
                        preFilter.addAll(categoryDAO.getMovieByCategory(category_id));
                    }
                }
            }
        }
       return removeDuplicates(preFilter);
    }

    public List<String> seperateBySpaces(String query){
        List<String> categoriesTyped = List.of(query.split(" "));
        return categoriesTyped;
    }

    public List<Movie> removeDuplicates(List<Movie> preFilter){
        Map<String, Movie> map = new HashMap<>();

        for (Movie m: preFilter
        ) {
            map.put(m.getName(), m);
        }
        List<Movie> filtered = new ArrayList<>(map.values());

        return filtered;
    }
}




