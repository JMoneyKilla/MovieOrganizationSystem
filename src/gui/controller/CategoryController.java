package gui.controller;

import gui.model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CategoryController{
    CategoryModel model = new CategoryModel();
    @FXML
    private TextField txtCategory;

    public void clickSave(ActionEvent actionEvent) {
        if (txtCategory!=null)
        {
            model.addCategory(txtCategory.getText());
            txtCategory.clear();
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
        else
        {
            // do something before closing
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
