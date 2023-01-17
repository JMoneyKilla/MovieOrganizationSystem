package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;
import gui.model.CategoryModelSingleton;
import gui.model.MovieModelSingleton;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class InputManager {
    CategoryModelSingleton categoryModelSingleton;
    MovieModelSingleton movieModelSingleton;

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
        if (Double.parseDouble(m.getRating()) < 6.0 || m.getRating()==null)
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

    /**
     * Uses movieModelSingleton to get all movies.
     * Checks if movies' names in allMovies contain query.
     * Adds movie to filtered list if query is contained in movie name.
     * @param query what the user types in search box
     * @return returns list of movies filtered by name.
     */
    public List<Movie> searchMovies(String query) {
        movieModelSingleton = MovieModelSingleton.getInstance();
        movieModelSingleton.getMovieModel().fetchAllMovies();
        List<Movie> allMovies = new ArrayList<>(movieModelSingleton.getMovieModel().getMovies());
        movieModelSingleton.getMovieModel().getMovies().clear();
        List<Movie> filtered = new ArrayList<>();

        for (Movie m: allMovies
             ) {
            if(m.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    /**
     * Gets list of all movies from movieModelSingleton.
     * If query equals "" all movies are returned.
     * Checks if the first character of the imdb rating equals the first character of the query
     * and returns if true.
     * @param query what the user types in search box
     * @return List of movies with matching Imdb rating
     */
    public List<Movie> searchImdbRating(String query) {
        movieModelSingleton = MovieModelSingleton.getInstance();
        movieModelSingleton.getMovieModel().fetchAllMovies();
        List<Movie> allMovies = new ArrayList<>(movieModelSingleton.getMovieModel().getMovies());
        movieModelSingleton.getMovieModel().getMovies().clear();
        List<Movie> filtered = new ArrayList<>();

        if(query.equals(""))
            return allMovies;

        for (Movie m : allMovies) {
            if (m.getImdbRating().charAt(0) == query.charAt(0)) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    /**
     * Uses movieModelSingleton to get a hashmap with all Movies organized by category.
     * Checks if the search bar is empty, if empty a list of all movies will be returned.
     * If search is not empty, category/categories are checked against hashmap's keys, if there are
     * any matches then the movie is added to a list "prefiltered".
     * Prefiltered is then returned after any duplicate movies are removed.
     *
     * @param query what the user types in the search box.
     *
     * @return List of movies that have a category that matches the query.
     *
     * @throws SQLException
     */
    public List<Movie> searchCategories(String query) {
        movieModelSingleton = MovieModelSingleton.getInstance();
        List<Movie> preFilter = new ArrayList<>();
        HashMap<Category, Movie> categorizedMovies = movieModelSingleton.getMovieModel().getCategorizedMovies();

        if(query.equals("")){
            movieModelSingleton.getMovieModel().fetchAllMovies();
            return movieModelSingleton.getMovieModel().getMovies();
        }
        else{
            List<String> categoriesTyped = seperateBySpaces(query);
            for (String s: categoriesTyped
            ) {
                categorizedMovies.forEach((categoryKey, movie) -> {
                    if(categoryKey.getName().toLowerCase().equals(s.toLowerCase())){
                        preFilter.add(movie);
                    }
                });
            }
        }
        return removeDuplicates(preFilter);
    }

    /**
     * checks if query string contains a space, splits it on the space and adds the strings
     * to a list
     * @param query what the user types in search box
     * @return List of one or more strings which represent the categories that were typed
     */
    public List<String> seperateBySpaces(String query){
        List<String> categoriesTyped = List.of(query.split(" "));
        return categoriesTyped;
    }

    /**
     * Takes a list of movies and puts them in a hashmap.
     * Uses the Movie name as a key and the Movie object as a value so
     * map only contains movies with unique name.
     * Adds all values of the map to a List to return.
     * @param preFilter
     * @return List of movies with unique names, no duplicates.
     */
    public List<Movie> removeDuplicates(List<Movie> preFilter){
        Map<String, Movie> map = new HashMap<>();

        for (Movie m: preFilter
        ) {
            map.put(m.getName(), m);
        }
        List<Movie> filtered = new ArrayList<>(map.values());

        return filtered;
    }

    /**
     * Checks if the user input is not a duplicate of an already existing category.
     * Returns true, if it is. Returns false if it isn't.
     */
    public boolean isCategoryDuplicate(String title)
    {
        categoryModelSingleton = CategoryModelSingleton.getInstance();
        categoryModelSingleton.getCategoryModel().fetchAllCategories();
        List<Category> categories;
        categories = categoryModelSingleton.getCategoryModel().getCategories();
        for(Category c : categories)
        {
            if(title.equalsIgnoreCase(c.getName()))
            {
                return true;
            }
        }
        return false;
    }
}



