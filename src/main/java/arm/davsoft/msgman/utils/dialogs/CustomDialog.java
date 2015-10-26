package arm.davsoft.msgman.utils.dialogs;

import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/21/15 <br/>
 * <b>Time:</b> 5:42 PM <br/>
 */
public abstract class CustomDialog {
    protected final Window ownerWindow;
    protected final Stage stage = new Stage();

    protected CustomDialog(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    public void show() {
        stage.show();
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public void requestFocus() {
        stage.requestFocus();
    }
}
