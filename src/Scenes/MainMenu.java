package Scenes;

import Css.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainMenu extends SceneBuilder {

    private static Button uploadButton = new Button("Upload");
    private static Button myPhotosButton = new Button("My photos");
    private static Button mapButton = new Button("Map");
    private static Button logOutButton = new Button("Log out");
    private static Button aboutButton = new Button("About");
    private static Alert infoDialog = new Alert(AlertType.INFORMATION);

    public MainMenu() {
        super();
        this.setLayout();
    }


    /**
     * Sets the layout of the main menu. The setLayout()-method from SceneBuilder is overridden, but also
     * called in the method in order to modify the method.
     */
    @Override
    public void setLayout() {
        super.setLayout();
        super.setPageTitle("- Main Menu");

        Css.setButtonsMainMenu(uploadButton, myPhotosButton, mapButton, logOutButton);

        uploadButton.setOnAction(e -> StageInitializer.setUploadScene());
        myPhotosButton.setOnAction(e -> StageInitializer.setSearchScene());
        mapButton.setOnAction(e -> System.out.println("Edit pressed"));
        logOutButton.setOnAction(e -> System.out.println("Log out pressed"));
        aboutButton.setOnAction(e -> infoDialog.showAndWait());

        super.getGridPane().add(uploadButton,0,0);
        super.getGridPane().add(myPhotosButton, 1,0);
        super.getGridPane().add(mapButton, 0,1);
        super.getGridPane().add(logOutButton, 1,1);

        super.getBorderPane().setBottom(aboutButton);
        BorderPane.setAlignment(aboutButton, Pos.BOTTOM_RIGHT);

        infoDialog.setTitle("Information dialog - About");
        infoDialog.setHeaderText("About the application Picturerama");
        infoDialog.setContentText("Created by: William, Diderik, Rokas, Martin, Olaf and Hermann. \n 2020");
    }

}
