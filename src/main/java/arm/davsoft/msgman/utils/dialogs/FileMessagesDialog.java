package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.domains.FileItem;
import arm.davsoft.msgman.domains.LineItem;
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

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/28/15 <br/>
 * <b>Time:</b> 11:34 PM <br/>
 */
public class FileMessagesDialog extends CustomDialog {
    private List<FileItem> fileItems;

    private FileMessagesDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static FileMessagesDialog create(List<FileItem> fileItems) {
        return create(fileItems, null);
    }

    public static FileMessagesDialog create(List<FileItem> fileItems, Window ownerWindow) {
        FileMessagesDialog dialog = new FileMessagesDialog(ownerWindow);
        dialog.fileItems = fileItems;
        return dialog.prepare();
    }

    private FileMessagesDialog prepare() {
        stage.setTitle("More...");

        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(5));

        List<LineItem> tableData = new ArrayList<>();

        for (FileItem item : fileItems) {
            tableData.addAll(item.getLineItems());
        }

        TableView<LineItem> messagesTable = new TableView<>(FXCollections.observableArrayList(tableData));
        messagesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        messagesTable.setEditable(true);
        VBox.setVgrow(messagesTable, Priority.ALWAYS);

        TableColumn<LineItem, String> valueColl = new TableColumn<>("VALUE");
        valueColl.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColl.setCellFactory(TextFieldTableCell.forTableColumn());

        messagesTable.getColumns().addAll(valueColl);

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
        for (FileItem item : fileItems) {
            for (LineItem lineItem : item.getLineItems()) {
                sb.append(lineItem.getValue()).append('\n');
            }
        }
        return sb.toString();
    }

    @Override
    public void show() {
        super.show();
        super.requestFocus();
    }
}
