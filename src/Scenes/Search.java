package Scenes;

import Components.Photo;
import Components.PhotoContainer;
import Css.Css;
import Database.DBConnection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Search extends SceneBuilder {
    private static ArrayList<Photo> photoList;
    private static ScrollPane scrollPane;
    private static VBox scrollPaneVBox;
    private static ArrayList<CheckBox> checkBoxArrayList;
    private static ArrayList<PhotoContainer> photoContainerList;

    private static Button searchButton;
    private static TextField searchTextField;
    private static CheckBox selectAllCheckBox;

    private static Button addToAlbumButton;
    private static Button viewAlbumsButton;

    public Search(){
        super();
        photoList = new ArrayList<>();
        DBConnection.getPhotos().stream().forEach(photo -> {
            photoList.add(photo);
        });
        scrollPane = new ScrollPane();
        scrollPaneVBox = new VBox();
        checkBoxArrayList = new ArrayList<>();
        photoContainerList = new ArrayList<>();
        searchButton = new Button("Search");
        searchTextField = new TextField();
        selectAllCheckBox = new CheckBox("Select all:");
        addToAlbumButton = new Button("Add to album");
        viewAlbumsButton = new Button("View albums");
        this.setLayout();
    }

    @Override
    public Scene getScene(){
        return super.getScene();
    }

    @Override
    public void setLayout(){
        super.setLayout();
        super.getGridPane().setAlignment(Pos.TOP_LEFT);
        super.setPageTitle(" - Search");
        setupImagesInAScrollPane();
        setupSearchBar();
        setupAlbumButtons();
        super.getGridPane().add(scrollPane,0,5,114, 40);
        super.getGridPane().add(searchTextField, 0, 0, 50, 4);
        super.getGridPane().add(searchButton, 50, 0, 15, 4);
        super.getGridPane().add(selectAllCheckBox, 100, 0, 15, 4);
        super.getGridPane().add(addToAlbumButton, 57, 46, 25, 4);
        super.getGridPane().add(viewAlbumsButton, 30, 46, 25, 4);
    }


    private void setupImagesInAScrollPane(){
        photoList.stream().forEach(photo -> {
            PhotoContainer photoContainer = new PhotoContainer(photo);
            scrollPaneVBox.getChildren().add(photoContainer.getPhotoContainer());
            photoContainerList.add(photoContainer);
            checkBoxArrayList.add(photoContainer.getCheckBox());
        });
        scrollPane.setContent(scrollPaneVBox);
        scrollPaneVBox.setStyle("-fx-background-color: #FFFFFF");
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.setMinHeight(560); //This is done to not resize height of the scroll pane when searching for images
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void setupSearchBar(){
        searchTextField.setPromptText("Write some keywords to filter images...");
        searchTextField.setOnAction(action -> filter());
        searchButton.setOnAction(action -> filter());
        Css.setButtons(searchButton);
        selectAllCheckBox.setOnAction(action -> {
            checkBoxArrayList.stream().forEach(checkBox -> {
                checkBox.setSelected(selectAllCheckBox.isSelected());
            });
        });
    }

    private void setupAlbumButtons(){
        Css.setButtons(addToAlbumButton, viewAlbumsButton);
        viewAlbumsButton.setPrefSize(500, 100);
        addToAlbumButton.setPrefSize(500, 100);
    }

    private void filter(){
        scrollPaneVBox.getChildren().clear();
        if(searchTextField.getText().equals("")){
            photoContainerList.stream().forEach(child -> {
                scrollPaneVBox.getChildren().add(child.getPhotoContainer());
            });
        } else {
            photoContainerList.stream().forEach(child -> {
                if(child.getPhoto().getTitle().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
                    scrollPaneVBox.getChildren().add(child.getPhotoContainer());
                }
            });
        }
    }
}

