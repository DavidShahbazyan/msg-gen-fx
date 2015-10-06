package arm.davsoft.msgman.domains;

import arm.davsoft.msgman.interfaces.Selectable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public class LineItem implements Selectable {
    private Integer number;
    private String line;
    private String value;
    private BooleanProperty isSelected;

    public LineItem() {
        this(null, null, null, false);
    }

    public LineItem(Integer number, String line, String value, boolean selected) {
        this.number = number;
        this.line = line;
        this.value = value;
        this.isSelected = new SimpleBooleanProperty(selected);
    }

    public Integer getNumber() {
        return number;
    }

    public String getLine() {
        return line;
    }

    public String getValue() {
        return value;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    @Override
    public boolean getIsSelected() {
        return isSelected.get();
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }
}
