package gui.controller;

import be.Category;
import gui.model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable{

    CategoryModel model = new CategoryModel();
    @FXML
    private ListView<Category> lstCategories;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstCategories.setItems(model.getCategories());
        model.fetchAllCategories();

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
}
