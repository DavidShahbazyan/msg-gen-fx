package arm.davsoft.msgman.components;

import javafx.scene.control.CheckBox;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 1/26/16 <br/>
 * <b>Time:</b> 1:41 PM <br/>
 */
public class CheckBoxToggleSwitch extends CheckBox {
    public CheckBoxToggleSwitch() {
        initStyleClass();
    }

    public CheckBoxToggleSwitch(String text) {
        super(text);
        initStyleClass();
    }

    private void initStyleClass() {
        getStyleClass().add("check-box-toggle-switch");
    }
}
