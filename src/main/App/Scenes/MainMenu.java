package Scenes;

import Components.Authentication;
import Css.Css;
import javafx.scene.control.Button;

public class MainMenu extends SceneBuilder {

    private Button uploadButton = new Button("Upload");
    private Button myPhotosButton = new Button("Photos");
    private Button albumsButton = new Button("Albums");
    private Button logOutButton = new Button("Log out");

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
        super.setPageTitle("Main Menu");

        Css.setButtonsMainMenu(uploadButton, myPhotosButton, albumsButton, logOutButton);

        uploadButton.setOnAction(e -> StageInitializer.setUploadScene());
        myPhotosButton.setOnAction(e -> StageInitializer.setSearchScene());
	    albumsButton.setOnAction(e -> StageInitializer.setAlbumsScene());
        logOutButton.setOnAction(e -> Authentication.logout());

        super.getGridPane().add(uploadButton,0,0);
        super.getGridPane().add(myPhotosButton, 1,0);
        super.getGridPane().add(albumsButton, 0,1);
        super.getGridPane().add(logOutButton, 1,1);
    }
}
