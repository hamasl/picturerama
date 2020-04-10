package Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import Css.Css;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that creates an universal popup window
 */
public class PopupWindow {
    private Stage dialogWindow;
    private VBox dialogVBox;
    private HBox dialogHBox;
    private Text dialogText;

    /**
     * Constructor that takes in the width and height of the popup window
     * @param width the width of the popup
     * @param height the height of the popup
     */
    public PopupWindow(double width, double height){

        dialogWindow = new Stage();
        dialogWindow.initModality(Modality.APPLICATION_MODAL);
        dialogWindow.getIcons().add(new Image("file:src/main/App/Images/Logo.png"));
        dialogWindow.setResizable(false);

        dialogVBox = new VBox();
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);

        dialogText = new Text();
        Css.setText(17,dialogText);

        dialogHBox = new HBox();
        dialogHBox.setPadding(new Insets(10,10,10,10));
        dialogHBox.setSpacing(10);

        dialogVBox.getChildren().addAll(dialogText, dialogHBox);
        Scene dialogScene = new Scene(dialogVBox, width, height);
        dialogWindow.setScene(dialogScene);
        dialogWindow.show();
    }

    public Stage getDialogWindow() {
        return dialogWindow;
    }

    public VBox getDialogVBox() {
        return dialogVBox;
    }

    public HBox getDialogHBox() {
        return dialogHBox;
    }

    public Text getDialogText() {
        return dialogText;
    }

}
