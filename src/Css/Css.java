package Css;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Css class used for styling different JavaFX elements
 */
public class Css {

    /**
     * set pane takes in an amount of panes or its subclasses (border pane, grid pane)
     * @param args an amount of panes
     */
    public static void setPane(Pane... args){
        for(Pane arg : args){
            arg.setStyle("-fx-background-color: white");
        }
    }

    /**
     * setButtons takes in an amount of buttons and sets their styling
     *
     * @param args an amount of buttons
     */
    public static void setButtonsSignUpLogin(Button... args) {
        for (Button arg : args) {
            arg.setStyle("-fx-position:absolute;");
            arg.setPrefHeight(20);
            arg.setPrefWidth(400);
        }
    }

    public static void setErrorLabel(Label... args) {
        for (Label arg : args) {
            arg.setStyle("-fx-text-fill: red");
        }
    }

    //TODO add more functionality to method

    /**
     * Sets the dark mode for the application
     *
     * @param args a number of objects that are of or extends the Pane class
     */
    public static void setDarkMode(Pane... args) {
        for (Pane arg : args) {
            arg.setStyle("-fx-background-color: #2d2d2d");
        }
    }
}
