package gui.controller;

import gui.model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMovieController {
    MovieModel model = new MovieModel();

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtFile;

    public void clickChoose(ActionEvent actionEvent) {

    }

    public void clickSave(ActionEvent actionEvent) {
        if (txtTitle !=null && txtFile != null)
        {
            String title = txtTitle.getText();
            String path = txtFile.getText();

            model.addMovie(title, path);

            txtTitle.clear();
            txtFile.clear();
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
