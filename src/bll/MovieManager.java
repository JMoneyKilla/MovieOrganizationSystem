package bll;

import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;
import java.sql.SQLException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MovieManager {

    MovieDAO movieDAO = new MovieDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    public List<Movie> getAllMovies() {
        try {
            return movieDAO.getAllMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to add a movie to the database and moves the mp4 file to the project folder
     * @param title
     * @param path
     */
    public void addMovie(String title, String path) {

        String imdbRating;
        try {
            imdbRating = getImdbRating(title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newPath = moveFile(path);

        String lastviewed = String.valueOf(java.time.LocalDate.now());
        
        try {
            movieDAO.addMovie(title, null, newPath, lastviewed, imdbRating);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    /**
     * Scrapes IMDB webpage, searches for movieTitle and gets movie id from first result of webpage.
     * Goes to first results webpage using the movie id and makes grabs the rating from rating element on webpage
     *
     * @param movieTitle
     * @return IMDB rating as String
     * @throws IOException
     */
    public String getImdbRating(String movieTitle) throws IOException {

        // Search for the movie on IMDb
        String url = "https://www.imdb.com/find?q=" + movieTitle;
        url += "&s=all";
        Document doc = Jsoup.connect(url).get();

        // Get the first search result
        Element result = doc.selectFirst(".ipc-metadata-list--base .ipc-metadata-list-summary-item__t");

        //Get imdb Id for the movie
        String movieUrl = result.attr("href");
        String movieId = movieUrl.substring(7, 16);

        // Get the movie page
        Document document = Jsoup.connect("https://www.imdb.com/title/" + movieId).get();

        // Get rating of movie and turn into String
        Element ratingResult = document.selectFirst(".eUYAaq");
        String rating = ratingResult.wholeText();

        return rating;
    }

    /**
     * Moves selected file to the project folder
     *
     * @param inputPath the location of the file that is getting moved
     * @return the new path to the location
     */
    private String moveFile(String inputPath) {
        File f = new File(inputPath);
        String movieName = f.getName();
        String outputPath = ("Movies/" + movieName);
        try {
            Files.move(Path.of(inputPath), Path.of(outputPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputPath;
    }

    /**
     * Tries to remove the movie from the database
     * @param movie
     */
    public void removeMovie(Movie movie) {

        try {
            movieDAO.deleteMovieByID(movie.getId());
            categoryDAO.removeMovieFromCategory(movie);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to update the title of a movie
     * @param title
     * @param movie
     */
    public void updateTitle(String title, Movie movie) {
        movie.setName(title);
        try {
            movieDAO.updateTitle(movie);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * tries to update the IMDB rating of a movie
     *
     * @param movie
     */
    public void updateIMDB(Movie movie) {
        try {
            String rating = getImdbRating(movie.getName());
            movie.setImdbRating(rating);
            movieDAO.updateIMDBRating(movie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    /**
     * tries to update the user rating of a movie
     *
     * @param selectedMovie
     */
    public void updateRating(Movie selectedMovie) {
        try {
            movieDAO.updateUserRating(selectedMovie);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
