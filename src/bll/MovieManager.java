package bll;

import be.Movie;
import dal.MovieDAO;

import java.sql.SQLException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MovieManager {

    MovieDAO movieDAO = new MovieDAO();
    public List<Movie> getAllMovies() {
        try {
            return movieDAO.getAllMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateRating(Movie selectedMovie) {
        try {
            movieDAO.updateRating(selectedMovie);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Scrapes IMDB webpage, searches for movieTitle and gets movie id from first result of webpage.
     * Goes to first results webpage using the movie id and makes grabs the rating from rating element on webpage
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
        String movieId = movieUrl.substring(7,16);

        // Get the movie page
        Document document = Jsoup.connect("https://www.imdb.com/title/" + movieId).get();

        // Get rating of movie and turn into String
        Element ratingResult = document.selectFirst(".eUYAaq");
        String rating = ratingResult.wholeText();

        return rating;
    }

    public static void main(String[] args) throws IOException {
        MovieManager mm = new MovieManager();
        System.out.println(mm.getImdbRating("Batman Begins"));
    }
    private String moveFile(String inputPath) {
        File f = new File(inputPath);
        String movieName = f.getName();
        String outputPath = ("Movie/" + movieName);
        try {
            Files.move(Path.of(inputPath), Path.of(outputPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputPath;
    }
}
