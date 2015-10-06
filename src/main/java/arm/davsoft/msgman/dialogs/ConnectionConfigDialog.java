package arm.davsoft.msgman.dialogs;

import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.utils.ResourceManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/1/15 <br/>
 * <b>Time:</b> 11:25 PM <br/>
 */
public class ConnectionConfigDialog {
    final Stage stage = new Stage();
    VBox wrapper = new VBox();
    TilePane tilePane = new TilePane();

    Label lblHostName = new Label(ResourceManager.getMessage("label.hostNameIPAddress")),
            lblPort = new Label(ResourceManager.getMessage("label.port")),
            lblSID = new Label(ResourceManager.getMessage("label.serviceNameSID")),
            lblUserName = new Label(ResourceManager.getMessage("label.userName")),
            lblPassword = new Label(ResourceManager.getMessage("label.password"));

    TextField txtHostName = new TextField(),
            txtPort = new TextField(),
            txtSID = new TextField(),
            txtUserName = new TextField();

    PasswordField txtPassword = new PasswordField();

    Button okButton = new Button(ResourceManager.getMessage("label.button.ok")),
            cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));

    ButtonBar buttonBar = new ButtonBar();

    ConnectionConfig tempConfig;
    boolean processCancelled;

    public ConnectionConfigDialog(ConnectionConfig config, Window ownerWindow) {
        tempConfig = config.clone();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerWindow);
        if (config.isNew()) {
            stage.setTitle(ResourceManager.getMessage("title.dialog.newConnection") + " " + config.getDbServerType().getName());
        } else {
            stage.setTitle(ResourceManager.getMessage("title.dialog.configureConnection"));
        }

        wrapper.setPadding(new Insets(5));
        wrapper.setSpacing(5);
        wrapper.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);

        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setPrefColumns(2);

        txtHostName.textProperty().bindBidirectional(tempConfig.getHostNameProperty());

        txtPort.textProperty().bindBidirectional(tempConfig.getPortProperty(), new NumberStringConverter("####"));

        txtSID.textProperty().bindBidirectional(tempConfig.getSIDProperty());

        txtUserName.textProperty().bindBidirectional(tempConfig.getUserNameProperty());

        txtPassword.textProperty().bindBidirectional(tempConfig.getPasswordProperty());


        tilePane.getChildren().add(lblHostName);
        tilePane.getChildren().add(txtHostName);

        if (tempConfig.isORAServer() || tempConfig.isMySQLServer()) {
            tilePane.getChildren().add(lblPort);
            tilePane.getChildren().add(txtPort);
        }

        if (tempConfig.isORAServer()) {
            tilePane.getChildren().add(lblSID);
            tilePane.getChildren().add(txtSID);
        }

        tilePane.getChildren().add(lblUserName);
        tilePane.getChildren().add(txtUserName);
        tilePane.getChildren().add(lblPassword);
        tilePane.getChildren().add(txtPassword);

        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> okAction());

        cancelButton.setOnAction(event -> cancelAction());

        buttonBar.getButtons().addAll(cancelButton, okButton);
        wrapper.getChildren().addAll(tilePane, buttonBar);

        stage.setScene(new Scene(wrapper));
        stage.getScene().getStylesheets().add(ResourceManager.getUIThemeStyle());
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void okAction() {
        processCancelled = false;
        closeDialog();
    }

    private void cancelAction() {
        processCancelled = true;
        closeDialog();
    }

    private ConnectionConfig closeDialog() {
        if (processCancelled) {
            tempConfig = null;
        } else {
            tempConfig.setIsNew(false);
        }
        stage.close();
        return tempConfig;
    }

    public ConnectionConfig getConfig() {
        return tempConfig;
    }
}
