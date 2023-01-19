import be.Movie;
import bll.InputManager;
import gui.controller.AlertNotification;
import gui.controller.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MovieOrganizationSystem extends Application {

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GUI/view/MovieMenu.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Movie Organization");
        stage.centerOnScreen();
        stage.show();
        deleteOldPopup(stage);

    }
    public void deleteOldPopup(Stage stage){
        List<Movie> oldLowMovies = null;
        InputManager popupInputManager = new InputManager();
        try {
            oldLowMovies = popupInputManager.getOldBadMovies();
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
        if(!oldLowMovies.isEmpty()){
            Stage popupWindow = new Stage();
            popupWindow.setTitle("Delete old movies?");
            popupWindow.initModality(Modality.WINDOW_MODAL);
            popupWindow.setResizable(false);
            popupWindow.setAlwaysOnTop(true);

            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/view/OldLowRatedPopUp.fxml"));
                Scene scene = new Scene(root);
                popupWindow.setScene(scene);
                popupWindow.initOwner(stage);
                popupWindow.show();
            } catch (IOException e) {
                AlertNotification.showAlertWindow(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
