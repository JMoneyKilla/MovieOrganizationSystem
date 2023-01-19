package gui.controller;

import be.Category;
import be.Movie;
import gui.model.CategoryModelSingleton;
import gui.model.MovieModelSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable{

    OldLowRatedPopUpController oldLowRatedPopUpController = new OldLowRatedPopUpController();
    MovieModelSingleton movieModelSingleton;
    CategoryModelSingleton categoryModelSingleton;
    @FXML
    private ListView<Category> lstCategories;
    @FXML
    private Slider sliderRating;
    @FXML
    private Label labelRating;
    @FXML
    private TableView<Movie> tableViewMovies;
    @FXML
    private TableColumn<Movie, String> columnTitle, columnUserRating, columnImdbRating, columnLastViewed;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox comboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        columnTitle.setEditable(true);
        columnTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnUserRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnImdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        columnLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastViewed"));
        movieModelSingleton = MovieModelSingleton.getInstance();
        tableViewMovies.setItems(movieModelSingleton.getMovieModel().getMovies());
        movieModelSingleton.getMovieModel().fetchAllMovies();
        ObservableList<String> choiceBoxOptions = FXCollections.observableArrayList("Movies", "Imdb Rating", "Categories");
        comboBox.setItems(choiceBoxOptions);

        //Listener to change search type based on selected filter option
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(lstCategories.getSelectionModel().getSelectedItem()!=null&&(comboBox.getSelectionModel().getSelectedItem()!=null
                        || comboBox.getSelectionModel().getSelectedItem() == null))
                {
                    labelRating.setText("Show all movies to use filter");
                }
                else if(comboBox.getSelectionModel().getSelectedItem()==null)
                {
                    labelRating.setText("Select what item, you want to filter");
                }
                else if(lstCategories.getSelectionModel().getSelectedItem()==null){
                    if(comboBox.getSelectionModel().getSelectedItem()=="Movies")
                    {
                        labelRating.setText("");
                        movieModelSingleton.getMovieModel().searchMovie(newValue);
                    }
                    else if(comboBox.getSelectionModel().getSelectedItem()=="Imdb Rating")
                    {
                        labelRating.setText("");
                        movieModelSingleton.getMovieModel().searchImdbRating(newValue);
                    }
                    else if(comboBox.getSelectionModel().getSelectedItem()=="Categories")
                    {
                        labelRating.setText("");
                        movieModelSingleton.getMovieModel().searchCategories(newValue);
                    }
                }
            }
        });

        //Listener to chang tableviewmovies based on selected category

        categoryModelSingleton = CategoryModelSingleton.getInstance();
        lstCategories.setItems(categoryModelSingleton.getCategoryModel().getCategories());
        categoryModelSingleton.getCategoryModel().fetchAllCategories();
        lstCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
                if(newValue != null) {
                    categoryModelSingleton.getCategoryModel().setMoviesInCategory(newValue.getId());
                    tableViewMovies.refresh();
                    tableViewMovies.setItems(categoryModelSingleton.getCategoryModel().getMoviesInCategory());
                }
            }
        });
        //Listener to change labelrating to value of slider in 0.0 format
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
            Stage addMovieWindow = new Stage();
            addMovieWindow.setScene(new Scene(root));
            addMovieWindow.setTitle("Add Movie");
            addMovieWindow.initModality(Modality.WINDOW_MODAL);
            addMovieWindow.centerOnScreen();
            addMovieWindow.initOwner(stage);
            addMovieWindow.show();


        } catch (IOException e) {
            AlertNotification.showAlertWindow(e.getMessage());
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


    public void clickAddCategory(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Window stage = n.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/CategoryMenu.fxml"));
            Stage addCategoryWindow = new Stage();
            addCategoryWindow.setScene(new Scene(root));
            addCategoryWindow.setTitle("Create new Category");
            addCategoryWindow.initModality(Modality.WINDOW_MODAL);
            addCategoryWindow.initOwner(stage);
            addCategoryWindow.show();

        } catch (IOException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            e.printStackTrace();
        }
    }

    public void clickDeleteCategory(ActionEvent actionEvent) {
        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();
        int category_id;
        if(selectedCategory==null)
            labelRating.setText("Select a category to delete");
        if (selectedCategory != null) {
            labelRating.setText("");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this category?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                category_id = selectedCategory.getId();
                categoryModelSingleton.getCategoryModel().removeCategory(category_id);
                categoryModelSingleton.getCategoryModel().fetchAllCategories();
                tableViewMovies.setItems(movieModelSingleton.getMovieModel().getMovies());
            }
        }
    }

    //Updates movie user rating based on labelRating value
    public void clickAddRating(ActionEvent actionEvent) {
        Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie!=null && labelRating.getText().length()==3 || labelRating.getText().length()==4)
        {
            selectedMovie.setRating(labelRating.getText());
            movieModelSingleton.getMovieModel().updateRating(selectedMovie);
            sliderRating.setValue(0.0);
            labelRating.setText("Rating has been added to "+selectedMovie.getName());
        }
        else if(selectedMovie==null)
        {
            labelRating.setText("Please select a movie");
        }
        else if(selectedMovie!=null && labelRating.getText().length()>4 || labelRating.getText().length()<3)
        {
            labelRating.setText("Please select a rating");
        }
        movieModelSingleton.getMovieModel().fetchAllMovies();
    }

    public void editName(TableColumn.CellEditEvent<Movie, String> movieStringCellEditEvent) {
        String newTitle = movieStringCellEditEvent.getNewValue().trim();
        Movie movie = tableViewMovies.getSelectionModel().getSelectedItem();
        movieModelSingleton.getMovieModel().updateTitle(newTitle, movie);
        tableViewMovies.refresh();
    }

    public void btnUpdateImdb(ActionEvent actionEvent) {
        if(tableViewMovies.getSelectionModel().getSelectedItem() == null)
            labelRating.setText("Select a movie to update Imdb rating");
        if(tableViewMovies.getSelectionModel().getSelectedItem()!=null){
            labelRating.setText("");
            movieModelSingleton.getMovieModel().updateIMDB(tableViewMovies.getSelectionModel().getSelectedItem());
        }
    }

    public void clickAddToCategory(ActionEvent actionEvent) {
        Movie selectedMovie = tableViewMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie!=null)
        {
            labelRating.setText("");
            AddCategoryToMovieMenuController.selected = tableViewMovies.getSelectionModel().getSelectedItem();
            Node n = (Node) actionEvent.getSource();
            Window stage = n.getScene().getWindow();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/AddCategoryToMovieMenu.fxml"));
                Stage addCategoryMenu = new Stage();
                addCategoryMenu.setScene(new Scene(root));
                addCategoryMenu.setTitle("Add Category To Movie");
                addCategoryMenu.initModality(Modality.WINDOW_MODAL);
                addCategoryMenu.initOwner(stage);
                addCategoryMenu.show();

            } catch (IOException e) {
                AlertNotification.showAlertWindow(e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {

            labelRating.setText("Movie has not been selected");
        }
    }

    /**
     * Tries to play selected movie in the default video player.
     * @param actionEvent
     */
    public void btnPlayMovie(ActionEvent actionEvent) {
        if(tableViewMovies.getSelectionModel().getSelectedItem()==null)
            labelRating.setText("Please select a movie to play");
        if(tableViewMovies.getSelectionModel().getSelectedItem()!=null){
            labelRating.setText("");
            String path = tableViewMovies.getSelectionModel().getSelectedItem().getAbsolutePath();
            String lastViewed = String.valueOf(java.time.LocalDate.now());
            Movie movie = tableViewMovies.getSelectionModel().getSelectedItem();
            try {
                movie.setLastViewed(lastViewed);
                movieModelSingleton.getMovieModel().updateLastViewed(movie);
                Desktop.getDesktop().open(new File(path));
            } catch (IllegalArgumentException | IOException e) {
                AlertNotification.showAlertWindow(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void clickShowAllMovies(ActionEvent actionEvent) {
        tableViewMovies.refresh();
        txtSearch.clear();
        tableViewMovies.setItems(movieModelSingleton.getMovieModel().getMovies());
        movieModelSingleton.getMovieModel().fetchAllMovies();
        lstCategories.getSelectionModel().clearSelection();
    }
}