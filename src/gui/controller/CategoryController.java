package gui.controller;

import be.Category;
import gui.model.CategoryModel;
import gui.model.CategoryModelSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class CategoryController{
    CategoryModel model = new CategoryModel();
    private CategoryModelSingleton modelSingleton;
    @FXML
    private TextField txtCategory;
    @FXML
    private Label labelCategory;
    List<Category> categories = new ArrayList<>(model.getCategories());

    public void clickSave(ActionEvent actionEvent) {
        String title = txtCategory.getText();
        modelSingleton = CategoryModelSingleton.getInstance();

        if(title!=null && model.isCategoryDuplicate(title)==true)
        {
            labelCategory.setText("Category already exist");
        }
        else if(title!=null && model.isCategoryDuplicate(title)==false)
        {

            modelSingleton = CategoryModelSingleton.getInstance();
            modelSingleton.getCategoryModel().addCategory(title);
            modelSingleton.getCategoryModel().fetchAllCategories();

            txtCategory.clear();
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }


    public void clickCancel(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();

    }
}
