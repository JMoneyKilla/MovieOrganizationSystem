package gui.model;

import be.Movie;
import bll.InputManager;
import bll.MovieManager;
import gui.controller.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;


public class MovieModel {
    private final ObservableList<Movie> movies;
    InputManager im = new InputManager();
    MovieManager bll = new MovieManager();

    public MovieModel() {
        movies = FXCollections.observableArrayList();
    }


    public void fetchAllMovies()
    {
        movies.clear();
        try {
            movies.addAll(bll.getAllMovies());
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public void addMovie(String title, String path) {
        try {
            bll.addMovie(title,path);
        } catch (IOException e) {
            AlertNotification.showAlertWindow(e.getMessage());
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void updateRating(Movie selectedMovie) {
        try {
            bll.updateRating(selectedMovie);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }
    public void removeMovie(Movie selectedMovie){
        movies.remove(selectedMovie);
        try {
            bll.removeMovie(selectedMovie);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }
    public void updateTitle(String title, Movie movie){
        try {
            bll.updateTitle(title,movie);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }
    public void updateIMDB(Movie movie){
        try {
            bll.updateIMDB(movie);
        } catch (IOException | SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void searchMovie(String text) {
        movies.clear();
        movies.addAll(im.searchMovies(text));
    }

    public void searchImdbRating(String text) {
        movies.clear();
        movies.addAll(im.searchImdbRating(text));
    }

    public void searchCategories(String text) {
        movies.clear();
        try {
            movies.addAll(im.searchCategories(text));
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }
}
