package Scenes;

import Components.UserInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Header {
    private static final Image logo = new Image("file:src/Images/Logo.png", 55, 55, true, true);
    private static final Label picturerama = new Label("Picturerama");
    private Label pageTitle;
    private static Button homeButton = new Button("Home");
    private static GridPane gridPane;


    public Header() {
        this.pageTitle = new Label();
        if (UserInfo.getUserName() != null) {
            homeButton.setOnAction(e -> StageInitializer.setMainMenuScene());
        } else {
            homeButton.setOnAction(e -> StageInitializer.setLoginScene());
        }
        gridPane = new GridPane();
        this.setGridPane();
    }

    public static Label getPicturerama() {
        return picturerama;
    }

    public static GridPane getGridPane() {
        return gridPane;
    }

    public void setPageTitle(String newTitle) {
        this.pageTitle.setText(newTitle);
    }

    /**
     * setGridPane void, sets padding and alignment for the grid pane
     */
    private void setGridPane() {
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(new ImageView(logo), 0,0);
        gridPane.add(picturerama, 1 ,0);
        gridPane.add(pageTitle, 2, 0);
        gridPane.add(homeButton, 3, 0);
    }
}
