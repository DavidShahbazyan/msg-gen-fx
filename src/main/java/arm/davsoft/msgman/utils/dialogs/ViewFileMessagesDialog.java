package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.domains.FileItem;
import arm.davsoft.msgman.domains.LineItem;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/28/15 <br/>
 * <b>Time:</b> 11:34 PM <br/>
 */
public class ViewFileMessagesDialog extends CustomDialog {

    private ViewFileMessagesDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static ViewFileMessagesDialog create(FileItem fileItem) {
        return create(fileItem, null);
    }
    
    public static ViewFileMessagesDialog create(FileItem fileItem, Window ownerWindow) {
        ViewFileMessagesDialog dialog = new ViewFileMessagesDialog(ownerWindow);
        return dialog.prepare(fileItem);
    }

    private ViewFileMessagesDialog prepare(FileItem fileItem) {
        stage.setTitle("More...");

        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(5));

        TableView<LineItem> messagesTable = new TableView<>(FXCollections.observableArrayList(fileItem.getLineItems()));
        messagesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        messagesTable.setEditable(true);
        VBox.setVgrow(messagesTable, Priority.ALWAYS);

        TableColumn<LineItem, String> valueColl = new TableColumn<>("VALUE");
        valueColl.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColl.setCellFactory(TextFieldTableCell.forTableColumn());

        messagesTable.getColumns().addAll(valueColl);

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> stage.close());

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().add(okButton);
        VBox.setVgrow(buttonBar, Priority.NEVER);

        root.getChildren().addAll(messagesTable, buttonBar);
        stage.setScene(new Scene(root));
        return this;
    }

    @Override
    public void show() {
        super.show();
        super.requestFocus();
    }
}
