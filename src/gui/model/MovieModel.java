package gui.model;

import be.Movie;
import dal.MovieDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class MovieModel {
    private final ObservableList<Movie> movies;
    MovieDAO movieDAO = new MovieDAO();

    public MovieModel() {
        movies = FXCollections.observableArrayList();
    }

    public void fetchAllMovies()
    {
        movies.clear();
        try {
            movies.addAll(movieDAO.getAllMovies());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }
    public void addMovie(String title, String path) {
    }
}
