package gui.controller;

import be.Category;
import be.Movie;
import gui.model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCategoryToMovieMenuController implements Initializable {

    public static Movie selected;
    CategoryModel cm = new CategoryModel();
    @FXML
    private Label labelTxt;
    @FXML
    private Label labelMovieTitle;
    @FXML
    private ListView<Category> lstCategories;

    public void autofill(Movie movie){
        labelMovieTitle.setText(movie.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autofill(selected);
        int movie_id = selected.getId();
        cm.selectMovie(movie_id);
        lstCategories.setItems(cm.getMissingCategories());
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
            cm.addMovieToCategory(category_id, movie_id);
        }
        else
        {
            labelTxt.setText("Select a category");
        }
    }
}
