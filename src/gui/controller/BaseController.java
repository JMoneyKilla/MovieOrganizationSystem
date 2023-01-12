package gui.controller;

import be.Category;
import be.Movie;
import bll.InputManager;
import gui.model.MovieModelSingleton;
import gui.model.CategoryModel;
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
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable{

    OldLowRatedPopUpController oldLowRatedPopUpController = new OldLowRatedPopUpController();
    CategoryModel cm = new CategoryModel();
    MovieModelSingleton movieModelSingleton;
    InputManager inputManager = new InputManager();
    @FXML
    private ListView<Category> lstCategories;
    @FXML
    private Slider sliderRating;
    @FXML
    private Label labelRating;
    @FXML
    private TableView<Movie> tableViewMovies;
    @FXML
    private TableColumn<Movie, String> columnTitle, columnUserRating, columnImdbRating;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        columnTitle.setEditable(true);
        columnTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnUserRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnImdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        movieModelSingleton = MovieModelSingleton.getInstance();
        tableViewMovies.setItems(movieModelSingleton.getMovieModel().getMovies());
        movieModelSingleton.getMovieModel().fetchAllMovies();

        lstCategories.setItems(cm.getCategories());
        cm.fetchAllCategories();
        lstCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
                cm.selectCategory(newValue.getId());
                tableViewMovies.refresh();
                tableViewMovies.setItems(cm.getMoviesInCategory());
            }
        });
        sliderRating.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
                double rating = sliderRating.getValue()/10;
                if (selectedMovie !=null){
                labelRating.textProperty().setValue(String.valueOf(String.format("%.1f", rating)));
                System.out.println(rating);
            }
            else
            {
                labelRating.setText("Select a movie in order to rate it");
            }
            }
        });
        oldLowRatedPopUpController.deleteOldPopup();
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
                movieModelSingleton.getMovieModel().removeMovie(selected);
                movieModelSingleton.getMovieModel().fetchAllMovies();
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
        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();
        int category_id;
        if (selectedCategory != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this category?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                category_id = selectedCategory.getId();
                cm.removeCategory(category_id);
            }
        }
    }

    public void clickAddRating(ActionEvent actionEvent) {
        Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie!=null && sliderRating !=null)
        {
            selectedMovie.setRating(labelRating.getText());
            movieModelSingleton.getMovieModel().updateRating(selectedMovie);
            System.out.println(labelRating.getText());
        }
        else
        {
            labelRating.setText("Please select a movie or a rating");
        }
    }

    public void editName(TableColumn.CellEditEvent<Movie, String> movieStringCellEditEvent) {
        String newTitle = movieStringCellEditEvent.getNewValue();
        Movie movie = tableViewMovies.getSelectionModel().getSelectedItem();
        movieModelSingleton.getMovieModel().updateTitle(newTitle, movie);
    }

    public void btnUpdateImdb(ActionEvent actionEvent) {
        if(tableViewMovies.getSelectionModel().getSelectedItem()!=null){
            movieModelSingleton.getMovieModel().updateIMDB(tableViewMovies.getSelectionModel().getSelectedItem());
        }
    }

    public void clickAddToCategory(ActionEvent actionEvent) {
        Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie!=null)
        {
            AddCategoryToMovieMenuController.selected = tableViewMovies.getSelectionModel().getSelectedItem();
            Node n = (Node) actionEvent.getSource();
            Window stage = n.getScene().getWindow();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/AddCategoryToMovieMenu.fxml"));
                Stage addCategoryMenu = new Stage();
                addCategoryMenu.setScene(new Scene(root));
                addCategoryMenu.setTitle("Add Category To Movie");
                addCategoryMenu.initOwner(stage);
                addCategoryMenu.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {

            labelRating.setText("Movie or Category has not been selected");
        }
    }

    /**
     * Tries to play selected movie in the default video player.
     * @param actionEvent
     */
    public void btnPlayMovie(ActionEvent actionEvent) {
        if(tableViewMovies.getSelectionModel().getSelectedItem()!=null){
            String path = tableViewMovies.getSelectionModel().getSelectedItem().getAbsolutePath();
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
