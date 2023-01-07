package bll;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MovieManager {

    /**
     * Scrapes IMDB webpage, searches for movieTitle and gets movie id from first result of webpage.
     * Goes to first results webpage using the movie id and makes a string out of the html text
     * Searches for where the rating is in html text and makes a substring of just the rating numbers.
     * Parses string to double.
     * @param movieTitle
     * @return double that is rating of movie
     * @throws IOException
     */
    public double getImdbRating(String movieTitle) throws IOException {

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

        // Get the movie rating
        String movieBody = document.body().toString();
        int ratingLocation = movieBody.indexOf("<span class=\"sc-7ab21ed2-1 eUYAaq\">");
        int ratingLocationStart = ratingLocation + 35;
        int ratingLocationEnd = ratingLocationStart + 3;
        String location = movieBody.substring(ratingLocationStart, ratingLocationEnd);


        return Double.parseDouble(location);
    }
}
