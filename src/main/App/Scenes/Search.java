package Scenes;

import Components.PhotoContainer;
import Components.UserInfo;
import Css.Css;
import Database.Hibernate;
import Database.HibernateClasses.Album;
import Database.HibernateClasses.Photo;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Search extends SceneBuilder {
  private ArrayList<Photo> photoList = new ArrayList<>();
  private ScrollPane scrollPane = new ScrollPane();
  private VBox scrollPaneVBox = new VBox();
  private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
  private ArrayList<PhotoContainer> photoContainerList = new ArrayList<>();
  private TextField searchTextField = new TextField();
  private CheckBox selectAllCheckBox = new CheckBox("Select all:");
  private Button addToAlbumButton = new Button("Add to album");
  ChoiceBox<String> choiceBox = new ChoiceBox<>();
  private Stage dialogWindow;
  private VBox dialogVbox;
  private HBox dialogHBox;
  private Text dialogText;


  public Search(){
    super();
    photoList.addAll(UserInfo.getUser().getPhotos());
    this.setLayout();
  }

  @Override
  public void setLayout(){
    super.setLayout();
    super.setPageTitle("Search");
    setupImagesInAScrollPane();
    setupSearchBar();
    setupAlbumButtons();
    super.getGridPane().add(scrollPane,0,1, 3, 1);
    super.getGridPane().add(searchTextField, 0, 0, 2, 1);
    super.getGridPane().add(selectAllCheckBox, 2, 0, 1, 1);
    GridPane.setHalignment(selectAllCheckBox, HPos.RIGHT);
    super.getGridPane().add(addToAlbumButton, 0, 2, 3, 1);
    super.getGridPane().setGridLinesVisible(false);
    super.getGridPane().setMaxWidth(700.0D);
    Css.setTextField(searchTextField);
    super.getGridPane().getStylesheets().add("file:src/main/App/Css/SearchField.css");
  }

  private void setupImagesInAScrollPane(){
    photoList.stream().forEach(photo -> {
      PhotoContainer photoContainer = new PhotoContainer(photo);
      scrollPaneVBox.getChildren().add(photoContainer.getPhotoContainer());
      photoContainerList.add(photoContainer);
      checkBoxArrayList.add(photoContainer.getCheckBox());
    });
    scrollPane.setContent(scrollPaneVBox);
    scrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    scrollPaneVBox.setStyle("-fx-background-color: #FFFFFF");
    scrollPane.fitToWidthProperty().set(true);
    scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
  }

  private void setupSearchBar(){
    searchTextField.setId("searchField");
    searchTextField.setPromptText("Search for image...");
    searchTextField.setOnKeyTyped(action -> filter());
    selectAllCheckBox.setOnAction(action -> checkBoxArrayList.stream().forEach(checkBox -> checkBox.setSelected(selectAllCheckBox.isSelected())));
  }

  private void setupAlbumButtons(){
    Css.setButtonsSignUpLogin(addToAlbumButton);
    addToAlbumButton.setOnAction(s->{
        createNewAlbumButtonPressed();
    });
  }

  private void filter(){
    scrollPaneVBox.getChildren().clear();
    if(searchTextField.getText().equals("")){
      photoContainerList.stream().forEach(child -> scrollPaneVBox.getChildren().add(child.getPhotoContainer()));
    } else {
      photoContainerList.stream().forEach(child -> {
        if(child.getPhoto().getTitle().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
          scrollPaneVBox.getChildren().add(child.getPhotoContainer());
        }
      });
    }
  }

  private void createAlbumPopupDialog() {
    dialogWindow = new Stage();
    dialogWindow.initModality(Modality.APPLICATION_MODAL);

    dialogVbox = new VBox();
    dialogVbox.setAlignment(Pos.CENTER);

    dialogText = new Text();

    dialogHBox = new HBox();
    dialogHBox.setPadding(new Insets(10,10,10,10));
    dialogHBox.setSpacing(10);

    Scene dialogScene = new Scene(dialogVbox, 500, 100);
    dialogWindow.setScene(dialogScene);
    dialogWindow.show();
  }
  private void createNewAlbumButtonPressed() {
    createAlbumPopupDialog();

    dialogWindow.getIcons().add(new Image("file:src/main/App/Images/Logo.png"));
    dialogWindow.setTitle("Add to Album");

    dialogText.setText("Please select the name of the album: ");
    Css.setTextAlbums(dialogText);

    setupChoiceBox();
    Css.setChoiceBoxAlbums(choiceBox);

    Button addAlbum = new Button("Add to album");
    Css.setAddAlbumButton(addAlbum);
    addAlbum.setOnAction(e -> {
      updateUser(choiceBox.getValue());
      dialogWindow.close();
    });

    dialogHBox.getChildren().addAll(choiceBox, addAlbum);
    dialogVbox.getChildren().addAll(dialogText, dialogHBox);
  }

  public void setupChoiceBox(){
    choiceBox.getStyleClass().add("choice-box");
    choiceBox.getStylesheets().add("file:src/main/App/Css/ChoiceBoxStyle.css");
    UserInfo.getUser().getAlbums().forEach(s->{
      choiceBox.getItems().add(s.getName());
    });
  }

  public ArrayList<Photo> getCheckedPhotos(){
    ArrayList<Photo> checkedPhotos = new ArrayList<>();
    for (int i = 0; i<checkBoxArrayList.size(); i++) {
      if(checkBoxArrayList.get(i).isSelected()){
        checkedPhotos.add(photoList.get(i));
      }
    }
    return checkedPhotos;
  }
  public int indexOfAlbum(String albumName){
    int index = -1;
    List<Album> albums = UserInfo.getUser().getAlbums();
    for (int i = 0; i <albums.size() ; i++) {
      if(albums.get(i).getName().equals(albumName)){
        index = i;
      }
    }
    return index;
  }
  public void updateUser(String albumnName){
    int index = indexOfAlbum(albumnName);
    ArrayList<Photo> checkedPhoto = getCheckedPhotos();
    checkedPhoto.forEach(s ->{
      UserInfo.getUser().getAlbums().get(index).getAlbumPhotos().add(s);
    });
    Hibernate.updateUser(UserInfo.getUser());
  }
}

