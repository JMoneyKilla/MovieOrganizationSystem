package gui.model;

import be.Movie;
import bll.InputManager;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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
        movies.addAll(bll.getAllMovies());
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }
    public void addMovie(String title, String path) {
        bll.addMovie(title,path);
    }

    public void updateRating(Movie selectedMovie) {
        bll.updateRating(selectedMovie);
    }
    public void removeMovie(Movie selectedMovie){
        movies.remove(selectedMovie);
        bll.removeMovie(selectedMovie);
    }
    public void updateTitle(String title, Movie movie){
        bll.updateTitle(title,movie);
    }
    public void updateIMDB(Movie movie){
        bll.updateIMDB(movie);
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
        movies.clear();;
        movies.addAll(im.searchCategories(text));
    }
}
