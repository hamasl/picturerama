package Scenes;

import Components.FileLogger;
import Components.PopupWindow;
import Components.PhotoContainer;
import Components.UserInfo;
import Css.Css;
import Css.FeedBackType;
import Database.Hibernate;
import Database.HibernateClasses.Album;
import Database.HibernateClasses.Photo;
import Database.HibernateClasses.Tags;
import java.util.Optional;
import java.util.logging.Level;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for the Search scene
 */
final class PhotosScene extends SceneBuilder {
  private final List<Photo> PHOTO_LIST = new ArrayList<>();
  private final ScrollPane SCROLL_PANE = new ScrollPane();
  private final VBox SCROLL_PANE_VBOX = new VBox();
  private final List<CheckBox> CHECKBOX_ARRAY_LIST = new ArrayList<>();
  private final List<PhotoContainer> PHOTO_CONTAINER_LIST = new ArrayList<>();
  private final TextField SEARCH_TEXT_FIELD = new TextField();
  private final HBox SELECT_ALL_HBOX = new HBox();
  private final CheckBox SELECT_ALL_CHECKBOX = new CheckBox();
  private final Button ADD_TO_ALBUM_BUTTON = new Button("Add to album");
  private final ChoiceBox<String> CHOICE_BOX = new ChoiceBox<>();
  private final Button DELETE_BUTTON = new Button("Delete selected photos");
  private final Label FEEDBACK_LABEL = new Label();

  /**
   * Sets up the photos scene and adds all the users photos to the photo list
   */
  PhotosScene() {
    super();
    PHOTO_LIST.addAll(UserInfo.getUser().getPhotos());
    this.setLayout();
  }

  /**
   * Sets up the layout of the photos scene overrides the setLayout method of SceneBuilder
   */
  @Override
  void setLayout() {
    super.setLayout();
    super.setPageTitle("Photos");
    setupImagesInAScrollPane();
    setupSearchBar();
    setupAlbumButtons();
    setupSelectAllHBox();
    setupDeleteButton();
    super.getGridPane().add(SCROLL_PANE, 0, 1, 3, 1);
    super.getGridPane().add(SEARCH_TEXT_FIELD, 0, 0, 2, 1);
    super.getGridPane().add(FEEDBACK_LABEL, 2, 0, 1, 1);
    GridPane.setHalignment(FEEDBACK_LABEL, HPos.LEFT);
    super.getGridPane().add(SELECT_ALL_HBOX, 2, 0, 1, 1);
    GridPane.setHalignment(SELECT_ALL_HBOX, HPos.RIGHT);
    super.getGridPane().getStylesheets().add("file:src/main/App/Css/SelectAllCheckBoxStyle.css");
    super.getGridPane().add(ADD_TO_ALBUM_BUTTON, 0, 2, 1, 1);
    super.getGridPane().add(DELETE_BUTTON, 2, 2, 1, 1);
    super.getGridPane().setMaxWidth(700.0D);
    //Styles layout components
    Css.setTextField(700, 20, 17, SEARCH_TEXT_FIELD);
    super.getGridPane().getStylesheets().add("file:src/main/App/Css/SearchField.css");
    Css.setAlbumScrollPaneBorder(SCROLL_PANE);
  }

  /**
   * Sets up the scroll pane in the search scene with all the photos of the user
   */
  private void setupImagesInAScrollPane() {
    if (!PHOTO_LIST.isEmpty()) {
      PHOTO_LIST.forEach(photo -> {
        PhotoContainer photoContainer = new PhotoContainer(photo);
        SCROLL_PANE_VBOX.getChildren().add(photoContainer.getPhotoContainerHBox());
        PHOTO_CONTAINER_LIST.add(photoContainer);
        CHECKBOX_ARRAY_LIST.add(photoContainer.getCheckBox());
      });
    } else {
      Text noPhotosText = new Text("No photos stored. You can upload photos in \"Upload\"");
      Css.setText(17, noPhotosText);
      SCROLL_PANE_VBOX.getChildren().add(noPhotosText);
      SCROLL_PANE_VBOX.setAlignment(Pos.CENTER);
      SELECT_ALL_HBOX.setDisable(true);
    }
    SCROLL_PANE.setContent(SCROLL_PANE_VBOX);
    SCROLL_PANE.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    SCROLL_PANE.fitToWidthProperty().set(true);
    SCROLL_PANE.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
  }

  /**
   * Sets up the search bar and its functionality
   */
  private void setupSearchBar() {
    SEARCH_TEXT_FIELD.setId("searchField");
    SEARCH_TEXT_FIELD.setTooltip(new Tooltip("To search by multiple tags, use comma as separation"));
    SEARCH_TEXT_FIELD.setPromptText("Search for image...");
    SEARCH_TEXT_FIELD.setOnKeyTyped(action -> filter());
    SELECT_ALL_CHECKBOX.setOnAction(action -> CHECKBOX_ARRAY_LIST.forEach(checkBox -> checkBox.setSelected(SELECT_ALL_CHECKBOX.isSelected())));
  }

  /**
   * Sets up the select-all button
   */
  private void setupSelectAllHBox() {
    SELECT_ALL_HBOX.getChildren().addAll(new Label("Select all:"), SELECT_ALL_CHECKBOX);
    SELECT_ALL_HBOX.setAlignment(Pos.CENTER_RIGHT);
    SELECT_ALL_CHECKBOX.getStyleClass().add("check-box");
  }

  /**
   * Sets up button for deleting photos
   */
  private void setupDeleteButton() {
    Css.setButton(700, 25, 20, DELETE_BUTTON);
    DELETE_BUTTON.setOnAction(action -> deleteSelectedPhotos());
  }

  /**
   * Sets up the add to album button
   */
  private void setupAlbumButtons() {
    Css.setButton(700, 25, 20, ADD_TO_ALBUM_BUTTON);
    ADD_TO_ALBUM_BUTTON.setOnAction(s -> createNewAlbumButtonPressed());
  }

  /**
   * Method for the search functionality.
   * Filters the scrollpanes photos, showing a photo if its title contains the searchtext or one if its tags are equal to the searchtext.
   */
  private void filter() {
    SCROLL_PANE_VBOX.getChildren().clear();
    if (SEARCH_TEXT_FIELD.getText().trim().equals("")) {
      PHOTO_CONTAINER_LIST.forEach(child -> SCROLL_PANE_VBOX.getChildren().add(child.getPhotoContainerHBox()));
    } else {
      PHOTO_CONTAINER_LIST.forEach(container -> {
        if (container.getPhoto().getTitle().toLowerCase().contains(SEARCH_TEXT_FIELD.getText().trim().toLowerCase())) {
          SCROLL_PANE_VBOX.getChildren().add(container.getPhotoContainerHBox());
        } else {
          String textInput = SEARCH_TEXT_FIELD.getText().trim().toLowerCase().replaceAll(" ", "");
          String[] multipleTags = textInput.split(",");
          if (getPhotoTags(container.getPhoto()).containsAll(Arrays.asList(multipleTags))) {
            SCROLL_PANE_VBOX.getChildren().add(container.getPhotoContainerHBox());
          }
        }
      });
    }
  }

  /**
   * Method that creates the popup that can create albums and creates the action popup
   */
  private void createNewAlbumButtonPressed() {
    PopupWindow popupWindow = new PopupWindow(StageInitializer.getStage(), 500, 100);
    popupWindow.getDialogWindow().setTitle("Add to Album");
    popupWindow.getDialogText().setText("Please select the name of the album: ");

    setupChoiceBox();
    Css.setChoiceBoxAlbums(CHOICE_BOX);

    Button addAlbum = new Button("Add to album");
    Css.setButton(500, 20, 17, addAlbum);
    addAlbum.setOnAction(e -> {
      updateUser(CHOICE_BOX.getValue());
      popupWindow.getDialogWindow().close();
    });

    popupWindow.getDialogHBox().getChildren().addAll(CHOICE_BOX, addAlbum);
  }

  /**
   * Sets up the checkboxes and adds styling to it
   */
  private void setupChoiceBox() {
    CHOICE_BOX.getItems().clear();
    CHOICE_BOX.getStyleClass().add("choice-box");
    CHOICE_BOX.getStylesheets().add("file:src/main/App/Css/ChoiceBoxStyle.css");
    UserInfo.getUser().getAlbums().forEach(s -> CHOICE_BOX.getItems().add(s.getName()));
  }

  /**
   * Helper method to get the checked photos in the search scene
   *
   * @return a list of checked photos
   */
  private ArrayList<Photo> getCheckedPhotos() {
    ArrayList<Photo> checkedPhotos = new ArrayList<>();
    for (int i = 0; i < CHECKBOX_ARRAY_LIST.size(); i++) {
      if (CHECKBOX_ARRAY_LIST.get(i).isSelected()) {
        checkedPhotos.add(PHOTO_LIST.get(i));
      }
    }
    return checkedPhotos;
  }

  /**
   * Method that updates the photos in the selected album
   *
   * @param albumName name of the selected album
   */
  private void updateUser(String albumName) {
    Album album = UserInfo.getUser().getAlbums().stream().filter(a -> a.getName().equals(albumName)).findAny().orElse(null);
    ArrayList<Photo> checkedPhoto = getCheckedPhotos();
    if (checkedPhoto.isEmpty()) {
      Css.playFeedBackLabelTransition(FeedBackType.ERROR, "Unsuccessful: No photos were chosen", 13, FEEDBACK_LABEL, 6);
    } else if (album == null) {
      Css.playFeedBackLabelTransition(FeedBackType.ERROR, "Unsuccessful: No album were chosen", 13, FEEDBACK_LABEL, 6);
    } else {
      checkedPhoto.forEach(s -> s.addAlbum(album));
      Css.playFeedBackLabelTransition(FeedBackType.SUCCESSFUL, "Added to " + albumName, 13, FEEDBACK_LABEL, 6);
    }
    Hibernate.updateUser(UserInfo.getUser());
  }

  /**
   * Private method for deleting selected photos.
   */
  private void deleteSelectedPhotos() {
    ArrayList<Photo> selectedPhotos = getCheckedPhotos();
    if (selectedPhotos.isEmpty()) {
      Css.playFeedBackLabelTransition(FeedBackType.ERROR, "Unsuccessful: No photos were chosen", 13, FEEDBACK_LABEL, 6);
    } else {
      boolean successfulDeleteSelectedPhotos = true;
      for (Photo photo : selectedPhotos) {
        Optional<PhotoContainer> optionalPhotoContainer = PHOTO_CONTAINER_LIST.stream().filter(c -> c.getPhoto().equals(photo)).findAny();
        if (optionalPhotoContainer.isPresent()) {
          photo.getAlbums().forEach(album -> album.getPhotos().remove(photo));
          UserInfo.getUser().getPhotos().remove(photo);
          PhotoContainer photoContainer = optionalPhotoContainer.get();
          photoContainer.getCheckBox().setSelected(false);
          SCROLL_PANE_VBOX.getChildren().remove(photoContainer.getPhotoContainerHBox());
        } else {
          //If one of the pictures were not successfully deleted, then the operation was not successful
          successfulDeleteSelectedPhotos = false;
          FileLogger.getLogger().log(Level.FINE, "Photo: {0} is not present in the list containers", photo);
          FileLogger.closeHandler();
        }
      }
      Hibernate.updateUser(UserInfo.getUser());
      if (successfulDeleteSelectedPhotos) {
        Css.playFeedBackLabelTransition(FeedBackType.SUCCESSFUL, "Deleted successfully", 13, FEEDBACK_LABEL, 6);
      } else {
        Css.playFeedBackLabelTransition(FeedBackType.ERROR, "One or more photos could not be deleted", 13, FEEDBACK_LABEL, 6);
      }
    }
  }

  /**
   * Helping method to retrieve all the tags to a specific photo.
   *
   * @param photo gets the tags of the photo.
   * @return all the tags to the specific photo.
   */
  private List<String> getPhotoTags(Photo photo) {
    return photo.getTags().stream()
        .map(Tags::getTag)
        .map(String::toLowerCase)
        .collect(Collectors.toList());
  }
}
