package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.enums.Tag;
import arm.davsoft.msgman.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 12/3/15 <br/>
 * <b>Time:</b> 3:01 PM <br/>
 */
public class AvailableTagsDialog extends CustomDialog {
    private AvailableTagsDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static AvailableTagsDialog create(Window ownerWindow) {
        AvailableTagsDialog dialog = new AvailableTagsDialog(ownerWindow);
        return dialog.prepare();
    }

    private AvailableTagsDialog prepare() {
        stage.setTitle("Available Tags List");
        stage.setResizable(false);
        TableView<Tag> tagTableView = new TableView<>(FXCollections.observableArrayList(Tag.values()));
        tagTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tagTableView.setPrefSize(800, 500);
        tagTableView.setFocusTraversable(false);
        HBox.setHgrow(tagTableView, Priority.ALWAYS);
        VBox.setVgrow(tagTableView, Priority.ALWAYS);
        TableColumn<Tag, String> nameColl = new TableColumn<>("Name");
        nameColl.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColl.setMinWidth(150);
        nameColl.setPrefWidth(Region.USE_COMPUTED_SIZE);
        nameColl.setMaxWidth(300);
        TableColumn<Tag, String> attrColl = new TableColumn<>("Attributes");
        attrColl.setCellValueFactory(new PropertyValueFactory<Tag, String>("attributesList") {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tag, String> param) {
                return new SimpleStringProperty(Utils.concatStrings(param.getValue().getAttributesList()));
            }
        });
        tagTableView.getColumns().addAll(nameColl, attrColl);
        stage.setScene(new Scene(tagTableView));
        return this;
    }

}
