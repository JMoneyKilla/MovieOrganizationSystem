package gui.controller;

import be.Category;
import be.Movie;
import gui.model.CategoryModelSingleton;
import gui.model.MovieModelSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoryToMovieMenuController implements Initializable {

    public static Movie selected;
    CategoryModelSingleton categoryModelSingleton = CategoryModelSingleton.getInstance();
    MovieModelSingleton movieModelSingleton = MovieModelSingleton.getInstance();
    @FXML
    private Label labelTxt;
    @FXML
    private Label labelMovieTitle;
    @FXML
    private ListView<Category> lstCategories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelMovieTitle.setText(selected.getName());
        int movie_id = selected.getId();
        categoryModelSingleton.getCategoryModel().setMissingCategoriesInMovie(movie_id);
        lstCategories.setItems(categoryModelSingleton.getCategoryModel().getMissingCategories());
    }


    public void clickClose(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }


    public void clickAddToMovie(ActionEvent actionEvent) {
        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();
        int category_id;
        int movie_id = selected.getId();
        if (selectedCategory!=null)
        {
            category_id = selectedCategory.getId();
            labelTxt.setText(""+selectedCategory.getName()+ " has been added to "+selected.getName());
            categoryModelSingleton.getCategoryModel().addMovieToCategory(category_id, movie_id);
            categoryModelSingleton.getCategoryModel().setMissingCategoriesInMovie(movie_id);
            lstCategories.setItems(categoryModelSingleton.getCategoryModel().getMissingCategories());
            movieModelSingleton.getMovieModel().updateCategorizedMovies();
        }
        else
        {
            labelTxt.setText("Select a category");
        }
    }
}
