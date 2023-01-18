package gui.controller;

import be.Movie;
import bll.InputManager;
import gui.model.MovieModelSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    public void deleteOldPopup(){
        List<Movie> oldLowMovies = null;
        try {
            oldLowMovies = inputManager.getOldBadMovies();
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
        }
        if(!oldLowMovies.isEmpty()){
            Stage popupWindow = new Stage();
            popupWindow.setTitle("Delete old movies?");
            popupWindow.initModality(Modality.WINDOW_MODAL);
            popupWindow.setResizable(false);
            popupWindow.setAlwaysOnTop(true);

            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/OldLowRatedPopUp.fxml"));
                Scene scene = new Scene(root);
                popupWindow.setScene(scene);
                popupWindow.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
