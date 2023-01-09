package gui.controller;

import be.Category;
import be.Movie;
import gui.model.CategoryModel;
import gui.model.MovieModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class BaseController implements Initializable{

    MovieModel mm = new MovieModel();
    CategoryModel cm = new CategoryModel();
    AddMovieController addMovieController = new AddMovieController();
    @FXML
    private ListView<Category> lstCategories;
    @FXML
    private Slider sliderRating;
    @FXML
    private Label labelRating;
    @FXML
    private TableView<Movie> tableViewMovies;
    @FXML
    private TableColumn<Movie, String> columnTitle, columnCategory, columnImdbRating;
    @FXML
    private TableColumn<Movie, Integer> columnRating;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        tableViewMovies.setItems(mm.getMovies());
        mm.fetchAllMovies();
        lstCategories.setItems(cm.getCategories());
        cm.fetchAllCategories();
        sliderRating.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
                double rating = sliderRating.getValue()/10;
                if (selectedMovie !=null){
                labelRating.textProperty().setValue(String.valueOf(String.format("%.1f", rating)));
            }
            else
            {
                labelRating.setText("Select a movie in order to rate it");
            }
            }
        });

    }
    public void clickAddMovie(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Window stage = n.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/AddMovieMenu.fxml"));
            Stage addPlaylistWindow = new Stage();
            addPlaylistWindow.setScene(new Scene(root));
            addPlaylistWindow.setTitle("Add Movie");
            addPlaylistWindow.initOwner(stage);
            addPlaylistWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteMovie(ActionEvent actionEvent) {
        if(tableViewMovies.getSelectionModel().getSelectedItem()!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this movie from the program? The file will still be on the computer", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                Movie selected = tableViewMovies.getSelectionModel().getSelectedItem();
                MovieModel movieModel = new MovieModel();
                movieModel.removeMovie(selected);
                mm.fetchAllMovies();
            }
        }
    }

    public void clickEditMovie(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Window stage = n.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/AddMovieMenu.fxml"));
            Stage addPlaylistWindow = new Stage();
            addPlaylistWindow.setScene(new Scene(root));
            addPlaylistWindow.setTitle("Edit Movie");
            addPlaylistWindow.initOwner(stage);
            addPlaylistWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickAddCategory(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Window stage = n.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/CategoryMenu.fxml"));
            Stage addPlaylistWindow = new Stage();
            addPlaylistWindow.setScene(new Scene(root));
            addPlaylistWindow.setTitle("Create new Category");
            addPlaylistWindow.initOwner(stage);
            addPlaylistWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteCategory(ActionEvent actionEvent) {
    }

    public void clickAddRating(ActionEvent actionEvent) {
        Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie!=null && sliderRating !=null)
        {
            selectedMovie.setRating(labelRating.getText());
            mm.updateRating(selectedMovie);
            System.out.println(labelRating.getText());
        }
        else
        {
            labelRating.setText("Please select a movie or a rating");
        }
    }
}
