package arm.davsoft.msggen.components;

import arm.davsoft.msggen.interfaces.Selectable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/10/15 <br/>
 * <b>Time:</b> 3:22 PM <br/>
 */
public class CheckBoxTableCell<T extends Selectable> extends TableCell<T, Boolean> {
    final CheckBox cellCheckBox;

    public CheckBoxTableCell() {
        this("");
    }

    public CheckBoxTableCell(String checkBoxText) {
        cellCheckBox = new CheckBox(checkBoxText);
    }

    public void doAction(T rowItem) {
        rowItem.setIsSelected(!rowItem.getIsSelected());
    }
    
    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty) {
            cellCheckBox.setSelected(getTableView().getItems().get(getIndex()).getIsSelected());
            cellCheckBox.setOnAction(event -> CheckBoxTableCell.this.doAction(CheckBoxTableCell.this.getTableView().getItems().get(CheckBoxTableCell.this.getIndex())));
            setGraphic(cellCheckBox);
        }
    }
}
