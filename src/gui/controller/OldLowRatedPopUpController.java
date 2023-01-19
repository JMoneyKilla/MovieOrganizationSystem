package gui.controller;

import be.Movie;
import bll.InputManager;
import gui.model.MovieModelSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OldLowRatedPopUpController implements Initializable {

    ObservableList oldMovies = FXCollections.observableArrayList();
    InputManager inputManager = new InputManager();
    MovieModelSingleton movieModelSingleton;
    @FXML
    private TableView tableViewOldMovies;
    @FXML
    private TableColumn columDOLastViewed;
    @FXML
    private TableColumn columnDORating;
    @FXML
    private TableColumn columnDOMovieTitle;


    public void setOldMovies(){
        List<Movie> movieList = null;
        try {
            movieList = inputManager.getOldBadMovies();
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
        }
        oldMovies.addAll(movieList);
    }

    public void clickYes(ActionEvent actionEvent) {
        movieModelSingleton = MovieModelSingleton.getInstance();
        List<Movie> movieList = null;
        try {
            movieList = inputManager.getOldBadMovies();
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
        }
        for (Movie m: movieList) {
            movieModelSingleton.getMovieModel().removeMovie(m);
            movieModelSingleton.getMovieModel().updateCategorizedMovies();
        }
        oldMovies.clear();
        movieModelSingleton.getMovieModel().fetchAllMovies();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void clickNo(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnDOMovieTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDORating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columDOLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastViewed"));
        setOldMovies();
        tableViewOldMovies.setItems(oldMovies);

    }
}
