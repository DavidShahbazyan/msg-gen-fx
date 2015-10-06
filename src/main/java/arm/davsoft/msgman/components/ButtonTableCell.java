package arm.davsoft.msgman.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/10/15 <br/>
 * <b>Time:</b> 3:22 PM <br/>
 */
public class ButtonTableCell <T> extends TableCell<T, Boolean> {
    final Button cellButton;

    public ButtonTableCell() {
        this("");
    }

    public ButtonTableCell(String buttonText) {
        cellButton = new Button(buttonText);
        cellButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                doAction(ButtonTableCell.this.getTableView().getItems().get(ButtonTableCell.this.getIndex()));
            }
        });
    }

    public void doAction(T rowItem) {

    }
    
    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty) {
            setGraphic(cellButton);
        }
    }
}
