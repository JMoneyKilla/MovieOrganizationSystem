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
     * Goes to first results webpage using the movie id and makes a string out of the html text
     * Searches for where the rating is in html text and makes a substring of just the rating numbers.
     * saves the IMDB rating as a string
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

        // Converts the HTML of the website into a long string, then we seargh the string for "<span class=\"sc-7ab21ed2-1 eUYAaq\">"
        // Which is the element that has the rating.
        // Then make a substring of the that starts 35 chars after the start of <span class=...  and 3 chars after this to find the rating.
        String movieBody = document.body().toString();
        int ratingLocation = movieBody.indexOf("<span class=\"sc-7ab21ed2-1 eUYAaq\">");
        int ratingLocationStart = ratingLocation + 35;
        int ratingLocationEnd = ratingLocationStart + 3;
        String rating = movieBody.substring(ratingLocationStart, ratingLocationEnd);


        return rating;
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
