package arm.davsoft.msggen.interfaces;

import javafx.beans.property.BooleanProperty;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/8/15 <br/>
 * <b>Time:</b> 2:15 PM <br/>
 */
public interface Selectable {
    BooleanProperty isSelectedProperty();

    boolean getIsSelected();

    void setIsSelected(boolean isSelected);
}
