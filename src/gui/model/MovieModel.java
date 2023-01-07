package gui.model;

import be.Movie;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class MovieModel {
    private final ObservableList<Movie> movies;
    MovieManager bll = new MovieManager();

    public MovieModel() {
        movies = FXCollections.observableArrayList();
    }

    public void fetchAllMovies()
    {
        movies.clear();
        movies.addAll(bll.getAllMovies());
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }
    public void addMovie(String title, String path) {
    }

    public void updateRating(Movie selectedMovie) {
        bll.updateRating(selectedMovie);
    }
}
