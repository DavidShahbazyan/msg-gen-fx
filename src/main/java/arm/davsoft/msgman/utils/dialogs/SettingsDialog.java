package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.utils.ResourceManager;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/21/15 <br/>
 * <b>Time:</b> 3:52 PM <br/>
 */
public class SettingsDialog extends CustomDialog {

    private SettingsDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static SettingsDialog create(Window ownerWindow) {
        SettingsDialog dialog = new SettingsDialog(ownerWindow);
        return dialog.prepare();
    }

    private SettingsDialog prepare() {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerWindow);
        stage.setTitle(ResourceManager.getMessage("title.dialog.settings"));

        IntegerProperty workingCopyIdProperty = new SimpleIntegerProperty(Integer.valueOf(ResourceManager.getSetting("workingCopyId")));
        StringProperty messagePatternProperty = new SimpleStringProperty(ResourceManager.getSetting("message.pattern"));
        BooleanProperty exportLogToFileProperty = new SimpleBooleanProperty(Boolean.valueOf(ResourceManager.getSetting("exportLogToFile")));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);
        vBox.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);

        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setPrefColumns(2);
        tilePane.setTileAlignment(Pos.CENTER_LEFT);

        Label lblWorkingCopyId = new Label(ResourceManager.getMessage("label.workingCopyId"));
        TextField txtWorkingCopyId = new TextField();
        txtWorkingCopyId.textProperty().bindBidirectional(workingCopyIdProperty, new NumberStringConverter("####"));

        Label lblMessagePattern = new Label(ResourceManager.getMessage("label.messagePattern"));
        TextField txtMessagePattern = new TextField();
        txtMessagePattern.textProperty().bindBidirectional(messagePatternProperty);

        Label lblExportLogToFile = new Label(ResourceManager.getMessage("label.exportLogToFile"));
        CheckBox checkExportLogToFile = new CheckBox();
        checkExportLogToFile.selectedProperty().bindBidirectional(exportLogToFileProperty);

        tilePane.getChildren().add(lblWorkingCopyId);
        tilePane.getChildren().add(txtWorkingCopyId);

        tilePane.getChildren().add(lblMessagePattern);
        tilePane.getChildren().add(txtMessagePattern);

        tilePane.getChildren().add(lblExportLogToFile);
        tilePane.getChildren().add(checkExportLogToFile);

        Button okButton = new Button(ResourceManager.getMessage("label.button.ok"));
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> {
            try {
                if (!Files.exists(Paths.get("./settings.properties"))) {
                    Files.createFile(Paths.get("./settings.properties"));
                }
                PropertiesConfiguration config = new PropertiesConfiguration("./settings.properties");
                config.setProperty("workingCopyId", "" + workingCopyIdProperty.get());
                config.setProperty("message.pattern", messagePatternProperty.get());
                config.setProperty("exportLogToFile", "" + exportLogToFileProperty.get());
                config.save();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error("Error occurred in showSettingsDialog method: ", ex);
            }
            stage.close();
        });

        Button cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));
        cancelButton.setOnAction(event -> {
            stage.close();
            Logger.getLogger(getClass()).info("Terminated by user.");
        });

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(cancelButton, okButton);

        vBox.getChildren().addAll(tilePane, buttonBar);

        Scene dialogScene = new Scene(vBox);
        stage.setScene(dialogScene);
        stage.getScene().getStylesheets().add(ResourceManager.getUIThemeStyle());
        stage.setResizable(false);
        return this;
    }

    public void show() {
        super.show();
        super.requestFocus();
    }
}
