package edu.wpi.MochaManticores.views;

import com.jfoenix.responsive.JFXResponsiveHandler;
import edu.wpi.MochaManticores.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class LoginPage extends SceneController{

    @FXML
    private ImageView backgroundIMG;

    @FXML
    private GridPane contentPane;

    public void initialize(){
        double height = super.getHeight();
        double width = super.getWidth();
        backgroundIMG.setFitHeight(height);
        backgroundIMG.setFitWidth(width);
        contentPane.setPrefSize(width,height);

        App.getPrimaryStage().widthProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal.doubleValue() < 400) {
                new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_EX_SMALL);
           } else if (newVal.doubleValue() < 768){
               new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_SMALL);
           }else if (newVal.doubleValue() < 979){
               new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_MEDIUM);
           }else{
               new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_LARGE);
           }
        });

        App.getPrimaryStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            final double height2 = super.getHeight();
            final double width2 = super.getWidth();
            backgroundIMG.setFitHeight(height2);
            backgroundIMG.setFitWidth(width2);
            contentPane.setPrefSize(width2,height2);

            if (newVal.doubleValue() < 400) {
                new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_EX_SMALL);
            } else if (newVal.doubleValue() < 768){
                new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_SMALL);
            }else if (newVal.doubleValue() < 979){
                new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_MEDIUM);
            }else{
                new JFXResponsiveHandler(App.getPrimaryStage(), JFXResponsiveHandler.PSEUDO_CLASS_LARGE);
            }
        });

    }

    @FXML
    AnchorPane emergencyPopUp;

    public void onMouseClickedContinue(ActionEvent e) {
        App.setClearenceLevel(0);
        changeSceneTo("mainMenu");
    }

    public void emergencyBtnClicked(ActionEvent e) {

    }

    public void staffMenu(ActionEvent e) {
        App.setClearenceLevel(2);
        super.changeSceneTo("staffMainMenu");
    }
    public void closePopUp(ActionEvent e) {
        emergencyPopUp.setOpacity(0);
    }

    public void goToEmergencyForm(ActionEvent e) {
        changeSceneTo("genericForm");
    }

    public void exitApp(ActionEvent e){
        super.exitApp(e);
    }
}
