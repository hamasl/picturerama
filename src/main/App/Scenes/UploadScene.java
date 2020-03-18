package Scenes;

import Components.ImageAnalyzer;
import Components.UserInfo;
import Css.Css;
import Database.Hibernate;
import Database.HibernateClasses.Photo;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;


public class UploadScene extends SceneBuilder {
    private Label titleLabel = new Label("Title: ");
    private TextField titleField = new TextField();
    private Label urlLabel = new Label("URL: ");
    private TextField urlField = new TextField();
    private Button uploadButton = new Button("Upload image");
    private Label feedbackLabel = new Label();

    public UploadScene() {
        super();
        this.setLayout();
    }

    @Override
    public void setLayout() {
        super.setLayout();
        super.setPageTitle("Upload");
        titleField.setPromptText("Title here...");
        urlField.setPromptText("URL here...");
        super.getGridPane().add(titleLabel, 0, 0);
        super.getGridPane().add(titleField, 0, 1);
        super.getGridPane().add(urlLabel, 0, 2);
        super.getGridPane().add(urlField, 0, 3);
        super.getGridPane().add(uploadButton, 0, 4);
        super.getGridPane().add(feedbackLabel, 0, 5);
        super.getGridPane().setAlignment(Pos.TOP_CENTER);
        Css.setButtonsSignUpLogin(uploadButton);
        Css.setLabel(titleLabel, urlLabel);
        Css.setTextField(titleField,urlField);

        uploadButton.setOnAction(event -> {
            if (checkField()) {
                upLoadComplete();
            }
        });

        super.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && checkField()) {
                upLoadComplete();
            }

        });
    }


    private boolean checkField() {
        if (titleField.getText().trim().length() == 0 || urlField.getText().trim().length() == 0) {
            feedbackLabel.setText("Title or URL are missing");
            Css.setErrorLabel(feedbackLabel);
            return false;
        }
        return true;
    }

    private void upLoadComplete(){
    	try {
		    Photo photo = ImageAnalyzer.analyze(titleField.getText(), urlField.getText());
		    UserInfo.getUser().getPhotos().add(photo);
		    Hibernate.updateUser(UserInfo.getUser());
		    StageInitializer.setMainMenuScene();
	    } catch (IOException e) {
		    Css.setErrorLabel(feedbackLabel);
		    feedbackLabel.setText("Something went wrong when retrieving image from url.");
	    } catch (NullPointerException e) {
		    Css.setErrorLabel(feedbackLabel);
		    feedbackLabel.setText("Something went wrong when analyzing image.");
	    }
    }
}
