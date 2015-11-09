package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.utils.Dialogs;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
    private List<Message> messages;
    private MessagesDialog(Window ownerWindow) {
        super(ownerWindow);
    }
    
    public static MessagesDialog create(List<Message> messages) {
        return create(messages, null);
    }
    
    public static MessagesDialog create(List<Message> messages, Window ownerWindow) {
        MessagesDialog dialog = new MessagesDialog(ownerWindow);
        dialog.messages = messages;
        return dialog.prepare();
    }

    private MessagesDialog prepare() {
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

        Button btnOK = new Button("OK");
        btnOK.setOnAction(event -> stage.close());

        Button btnCopy = new Button("Copy");
        btnCopy.setOnAction(event -> copyDataToClipboard());

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(btnCopy, btnOK);
        VBox.setVgrow(buttonBar, Priority.NEVER);

        root.getChildren().addAll(messagesTable, buttonBar);
        stage.setScene(new Scene(root));
        return this;
    }

    private void copyDataToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(getMessagesAsString());
        clipboard.setContent(content);
        Dialogs.showInfoPopup("Done!", null, "The data is now in your clipboard.");
    }

    private String getMessagesAsString() {
        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            sb.append(message.getId()).append('\t').append(message.getText()).append('\n');
        }
        return sb.toString();
    }

    @Override
    public void show() {
        super.show();
        super.requestFocus();
    }
}
