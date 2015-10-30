package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.domains.Message;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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

import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/30/15 <br/>
 * <b>Time:</b> 3:07 PM <br/>
 */
public class MessagesDialog extends CustomDialog {
    
    private MessagesDialog(Window ownerWindow) {
        super(ownerWindow);
    }
    
    public static MessagesDialog create(List<Message> messages) {
        return create(messages, null);
    }
    
    public static MessagesDialog create(List<Message> messages, Window ownerWindow) {
        MessagesDialog dialog = new MessagesDialog(ownerWindow);
        return dialog.prepare(messages);
    }

    private MessagesDialog prepare(List<Message> messages) {
        stage.setTitle("More...");

        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(5));

        TableView<Message> messagesTable = new TableView<>(FXCollections.observableArrayList(messages));
        messagesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        messagesTable.setEditable(true);
        VBox.setVgrow(messagesTable, Priority.ALWAYS);

        TableColumn<Message, Integer> idColl = new TableColumn<>("ID");
        idColl.setCellValueFactory(new PropertyValueFactory<>("id"));
//        valueColl.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Message, String> valueColl = new TableColumn<>("VALUE");
        valueColl.setCellValueFactory(new PropertyValueFactory<>("text"));
        valueColl.setCellFactory(TextFieldTableCell.forTableColumn());

        messagesTable.getColumns().addAll(idColl, valueColl);

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
