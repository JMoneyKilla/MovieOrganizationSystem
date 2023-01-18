package gui.controller;

import gui.model.MovieModelSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class AddMovieController {

    private MovieModelSingleton movieModelSingleton;

    @FXML
    private Label labelAlert;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtFile;

    /**
     * Lets you select a mp4 file, the default start location is the windows video folder.
     * @param actionEvent
     */

    public void clickChoose(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Movie");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video", "*.mp4"));

        //the default folder you start in the is your default video folder.
        String userprofile = System.getenv("USERPROFILE");
        chooser.setInitialDirectory(new File(userprofile +"\\videos"));

        Node n = (Node) actionEvent.getSource();
        Window stage = n.getScene().getWindow();

        //this will put the full path into the path text field.
        File selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String title = selectedFile.getName();
            String fileTileWithOutMP4 = title.substring(0, title.length() - 4);
            txtTitle.setText(fileTileWithOutMP4);
            txtFile.setText(selectedFile.getAbsolutePath());
            txtFile.setEditable(false);
        }

    }

    /**
     * Looks at the information that has been input and uses it to try and add a movie to the program.
     * @param actionEvent
     */
    public void clickSave(ActionEvent actionEvent) {

        if(txtTitle.getText().equals("") || txtTitle == null || txtFile.getText().equals("") ||txtFile == null){
            labelAlert.setText("Please enter valid inputs");
        }

        if ((!txtTitle.getText().isEmpty() || !txtTitle.getText().isBlank()) && (!txtFile.getText().isEmpty() || !txtFile.getText().isBlank()))
        {
            String title = txtTitle.getText();
            String path = txtFile.getText();

            movieModelSingleton = MovieModelSingleton.getInstance();
            movieModelSingleton.getMovieModel().addMovie(title, path);
            movieModelSingleton.getMovieModel().fetchAllMovies();

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
