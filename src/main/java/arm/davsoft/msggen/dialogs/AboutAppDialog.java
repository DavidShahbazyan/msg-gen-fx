package arm.davsoft.msggen.dialogs;

import arm.davsoft.msggen.utils.ResourceManager;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/2/15 <br/>
 * <b>Time:</b> 4:45 PM <br/>
 */
public final class AboutAppDialog {
    private AboutAppDialog() { throw new RuntimeException("Should not have an instance."); }

    public static void show(Window ownerWindow) {
        String title = ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.RELEASE.VERSION");

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerWindow);
        stage.setResizable(false);
        stage.setTitle(ResourceManager.getMessage("title.dialog.about") + " " + title);

        ImageView background = new ImageView("/images/splashScreen.jpg");

        String aboutTheApp = ResourceManager.getMessage("label.aboutTheApp");

        aboutTheApp = aboutTheApp.replace("{productName}", ResourceManager.getParam("APPLICATION.NAME"));
        aboutTheApp = aboutTheApp.replace("{releaseVersion}", ResourceManager.getParam("APPLICATION.RELEASE.VERSION"));
        aboutTheApp = aboutTheApp.replace("{releaseDate}", ResourceManager.getParam("APPLICATION.RELEASE.DATE"));
        aboutTheApp = aboutTheApp.replace("{javaVersion}", ResourceManager.getParam("APPLICATION.JAVA.VERSION"));
        aboutTheApp = aboutTheApp.replace("{developers}", ResourceManager.getParam("APPLICATION.DEVELOPERS").replace(';', '\n'));
        aboutTheApp = aboutTheApp.replace("{designers}", ResourceManager.getParam("APPLICATION.DESIGNERS"));
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
        stage.getScene().getStylesheets().addAll(ResourceManager.getUIThemeStyle());
        stage.show();
        stage.requestFocus();
    }
}
