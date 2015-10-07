import arm.davsoft.msgman.utils.Dialogs;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Author: tatevik
 * Date: 07.10.2015
 * Time: 00:19
 */
public class PopupDialogTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Dialogs.showInfoPopup("Title", "Header", "Info");
        Dialogs.showInfoPopup("Title", null, "Info");

        Dialogs.showWarningPopup("Title", "Header", "Warning");
        Dialogs.showWarningPopup("Title", null, "Warning");

        Dialogs.showConfirmPopup("Title", "Header", "Confirm");
        Dialogs.showConfirmPopup("Title", null, "Confirm");

        Dialogs.showExceptionDialog("Header", "Exception", new NullPointerException("Temp exception."));
        Dialogs.showExceptionDialog(null, "Exception", new NullPointerException("Temp exception."));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
