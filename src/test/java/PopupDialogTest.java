import arm.davsoft.msgman.utils.ResourceManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;

/**
 * Author: tatevik
 * Date: 07.10.2015
 * Time: 00:19
 */
public class PopupDialogTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(ResourceManager.getUIThemeStyle());

//        Dialogs.showInfoPopup("Title", "Header", "Info");
//        Dialogs.showInfoPopup("Title", null, "Info");

//        Dialogs.showWarningPopup("Title", "Header", "Warning");
//        Dialogs.showWarningPopup("Title", null, "Warning");

//        Dialogs.showConfirmPopup("Title", "Header", "Confirm");
//        Dialogs.showConfirmPopup("Title", null, "Confirm");

//        Dialogs.showErrorDialog("Header", "Exception ");
//        Dialogs.showErrorDialog(null, "Exception");

//        Dialogs.showExceptionDialog("Header", "Exception ", new NullPointerException("Temp exception."));
//        Dialogs.showExceptionDialog(null, "Exception", new NullPointerException("Temp exception."));

//        Dialogs.showMsgQuantityPopup("Title", "Header", "Content");

//        Alert alert = new Alert(Alert.AlertType.NONE, "Processing...", ButtonType.CANCEL);
//        alert.setGraphic(new ProcessIndicator("images/icons/process/fs/step_1@2x.png", true));
//        alert.show();
        primaryStage.show();
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Test
    public void mainTest() {
        launch();
    }
}
