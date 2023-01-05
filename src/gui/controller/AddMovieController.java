package gui.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AddMovieController {
    public void clickChoose(ActionEvent actionEvent) {

    }

    public void clickSave(ActionEvent actionEvent) {
    }

    public void clickCancel(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}
