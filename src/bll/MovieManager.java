package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;
import java.sql.SQLException;
import java.util.HashMap;
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

    public List<Movie> getAllMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    public HashMap<Category, Movie> getCategorizedMovies() throws SQLException {
        return movieDAO.getAllCatMovies();
    }

    /**
     * Tries to add a movie to the database and moves the mp4 file to the project folder
     * @param title
     * @param path
     */
    public void addMovie(String title, String path) throws IOException, SQLException {

        String imdbRating = getImdbRating(title);
        String newPath = moveFile(path);

        String lastviewed = String.valueOf(java.time.LocalDate.now());

        movieDAO.addMovie(title, null, newPath, lastviewed, imdbRating);
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
    private String moveFile(String inputPath) throws IOException {
        File f = new File(inputPath);
        String movieName = f.getName();
        String outputPath = ("Movies/" + movieName);

        Files.move(Path.of(inputPath), Path.of(outputPath));

        return outputPath;
    }

    /**
     * Tries to remove the movie from the database
     * @param movie
     */
    public void removeMovie(Movie movie) throws SQLException {
        movieDAO.removeMovieFromCategory(movie);
        movieDAO.deleteMovieByID(movie.getId());
    }

    /**
     * Tries to update the title of a movie
     * @param title
     * @param movie
     */
    public void updateTitle(String title, Movie movie) throws SQLException {
        movie.setName(title);
        movieDAO.updateTitle(movie);
    }

    /**
     * tries to update the IMDB rating of a movie
     *
     * @param movie
     */
    public void updateIMDB(Movie movie) throws SQLException, IOException {
        String rating = getImdbRating(movie.getName());
        movie.setImdbRating(rating);
        movieDAO.updateIMDBRating(movie);
    }
    /**
     * tries to update the user rating of a movie
     *
     * @param selectedMovie
     */
    public void updateRating(Movie selectedMovie) throws SQLException {
        movieDAO.updateUserRating(selectedMovie);
    }
}