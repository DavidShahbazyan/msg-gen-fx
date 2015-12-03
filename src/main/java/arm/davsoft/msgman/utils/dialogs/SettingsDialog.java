package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.utils.FXMLFactory;
import arm.davsoft.msgman.utils.ResourceManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/21/15 <br/>
 * <b>Time:</b> 3:52 PM <br/>
 */
public class SettingsDialog extends CustomDialog {

    private SettingsDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static SettingsDialog create(Window ownerWindow) {
        SettingsDialog dialog = new SettingsDialog(ownerWindow);
        return dialog.prepare();
    }

    private SettingsDialog prepare() {
        try {
            Parent root = FXMLFactory.getFXMLParent("settingsScreen.fxml");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(ownerWindow);
            stage.setTitle(ResourceManager.getMessage("title.dialog.settings"));
            stage.setScene(new Scene(root));
//            stage.getScene().getStylesheets().add(ResourceManager.getUIThemeStyle());
            stage.getScene().getStylesheets().add(Application.getUserAgentStylesheet());
            stage.setResizable(false);
            return this;
        } catch (IOException ex) {
            Main.LOGGER.error("Error occurred in SettingsDialog.prepare() method:", ex);
        }
        return null;
    }

}
