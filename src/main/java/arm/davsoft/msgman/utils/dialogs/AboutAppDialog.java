package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.utils.ResourceManager;
import arm.davsoft.msgman.utils.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/2/15 <br/>
 * <b>Time:</b> 4:45 PM <br/>
 */
public class AboutAppDialog extends CustomDialog {

    private AboutAppDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static AboutAppDialog create(Window ownerWindow) {
        AboutAppDialog dialog = new AboutAppDialog(ownerWindow);
        return dialog.prepare();
    }

    private AboutAppDialog prepare() {
        String title = ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.RELEASE.VERSION");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerWindow);
        stage.setResizable(false);
        stage.setTitle(ResourceManager.getMessage("title.dialog.about") + " " + title);

        ImageView background = new ImageView("/images/background.png");
        background.setFitWidth(500);
        background.setFitHeight(500);

        String aboutTheApp = ResourceManager.getMessage("label.aboutTheApp");

        aboutTheApp = aboutTheApp.replace("{productName}", ResourceManager.getParam("APPLICATION.NAME"));
        aboutTheApp = aboutTheApp.replace("{releaseVersion}", ResourceManager.getParam("APPLICATION.RELEASE.VERSION"));
        aboutTheApp = aboutTheApp.replace("{releaseDate}", ResourceManager.getParam("APPLICATION.RELEASE.DATE"));
        aboutTheApp = aboutTheApp.replace("{javaVersion}", ResourceManager.getParam("APPLICATION.JAVA.VERSION"));
        aboutTheApp = aboutTheApp.replace("{developers}", Utils.concatStrings(ResourceManager.getParamList("APPLICATION.DEVELOPERS"), "\n"));
        aboutTheApp = aboutTheApp.replace("{designers}", Utils.concatStrings(ResourceManager.getParamList("APPLICATION.DESIGNERS"), "\n"));
        aboutTheApp = aboutTheApp.replace("{partners}", Utils.concatStrings(ResourceManager.getParamList("APPLICATION.PARTNERS"), "\n"));
        aboutTheApp = aboutTheApp.replace("{copyrights}", ResourceManager.getParam("APPLICATION.COPYRIGHTS"));

        TextArea aboutTheAppTextArea = new TextArea(aboutTheApp);
        aboutTheAppTextArea.setEditable(false);
        aboutTheAppTextArea.setFocusTraversable(false);
        aboutTheAppTextArea.setWrapText(true);
        aboutTheAppTextArea.setPrefSize(300, 200);
        aboutTheAppTextArea.setMinSize(aboutTheAppTextArea.getPrefWidth(), aboutTheAppTextArea.getPrefHeight());
        aboutTheAppTextArea.setMaxSize(aboutTheAppTextArea.getPrefWidth(), aboutTheAppTextArea.getPrefHeight());

        AnchorPane p = new AnchorPane(background, aboutTheAppTextArea);
        AnchorPane.setTopAnchor(background, (double) 0);
        AnchorPane.setRightAnchor(background, (double) 0);
        AnchorPane.setBottomAnchor(background, (double) 0);
        AnchorPane.setLeftAnchor(background, (double) 0);

        AnchorPane.setTopAnchor(aboutTheAppTextArea, (double) 200);
        AnchorPane.setRightAnchor(aboutTheAppTextArea, (double) 50);
        AnchorPane.setLeftAnchor(aboutTheAppTextArea, (double) 50);

        p.setStyle("-fx-background-color: transparent;");

        stage.setScene(new Scene(p));
        stage.getScene().getStylesheets().addAll(Application.getUserAgentStylesheet());

        stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.close();
            }
        });

        return this;
    }

}
