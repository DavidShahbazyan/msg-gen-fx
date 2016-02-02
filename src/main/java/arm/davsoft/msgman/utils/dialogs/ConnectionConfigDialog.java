package arm.davsoft.msgman.utils.dialogs;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.IDMVersion;
import arm.davsoft.msgman.implementations.ConnectionConfigImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.utils.ResourceManager;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

import java.util.Objects;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/1/15 <br/>
 * <b>Time:</b> 11:25 PM <br/>
 */
public class ConnectionConfigDialog extends CustomDialog {
    private ConnectionConfig _config;
    private ConnectionConfig _tempConfig;


    private Label lbl_dbType, lbl_dbTypeName, lbl_idmVer, lbl_httpIp, lbl_port, lbl_dbName, lbl_sid, lbl_login, lbl_pass;
    private ComboBox<IDMVersion> val_idmVer;
    private TextField val_httpIp, val_port, val_sid, val_login, val_dbName;
    private PasswordField val_pass = new PasswordField();
    private Button btn_ok, btn_cancel;
    private ButtonBar btnBar;


    private ConnectionConfigDialog(Window ownerWindow) {
        super(ownerWindow);
    }

    public static ConnectionConfigDialog create(DBServerType serverType, Window ownerWindow) {
        ConnectionConfigDialog dialog = new ConnectionConfigDialog(ownerWindow);
        dialog._tempConfig = new ConnectionConfigImpl(serverType);
        dialog._tempConfig.initConnectionConfig();
        return dialog.prepare();
    }

    public static ConnectionConfigDialog edit(ConnectionConfig config, Window ownerWindow) {
        Objects.requireNonNull(config, "Connection configuration can not be null.");
        ConnectionConfigDialog dialog = new ConnectionConfigDialog(ownerWindow);
        dialog._config = config;
        dialog._tempConfig = config.clone();
        dialog._tempConfig.setIsNew(false);
        return dialog.prepare();
    }

    private void initComponents() {
        lbl_dbType = new Label(ResourceManager.getMessage("label.dbType"));
        lbl_dbTypeName = new Label(_tempConfig.getDbServerType().name());
        lbl_idmVer = new Label(ResourceManager.getMessage("label.idmVersion"));
        lbl_httpIp = new Label(ResourceManager.getMessage("label.hostNameIPAddress"));
        lbl_port = new Label(ResourceManager.getMessage("label.port"));
        lbl_dbName = new Label(ResourceManager.getMessage("label.dbName"));
        lbl_sid = new Label(ResourceManager.getMessage("label.serviceNameSID"));
        lbl_login = new Label(ResourceManager.getMessage("label.userName"));
        lbl_pass = new Label(ResourceManager.getMessage("label.password"));

        val_idmVer = new ComboBox<>(FXCollections.observableArrayList(IDMVersion.values()));

        val_httpIp = new TextField();
        val_port = new TextField();
        val_sid = new TextField();
        val_login = new TextField();
        val_dbName = new TextField();

        val_pass = new PasswordField();

        btn_ok = new Button(ResourceManager.getMessage("label.button.ok"));
        btn_ok.setDefaultButton(true);

        btn_cancel = new Button(ResourceManager.getMessage("label.button.cancel"));
        btn_cancel.setCancelButton(true);

        btnBar = new ButtonBar();
        btnBar.getButtons().addAll(btn_cancel, btn_ok);
    }

    private ConnectionConfigDialog prepare() {
        try {
            VBox root = new VBox(5);
            root.setPadding(new Insets(5));
            root.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);

            initModality(Modality.APPLICATION_MODAL);
            initOwner(ownerWindow);
            setTitle(ResourceManager.getMessage("title.dialog.configureConnection"));

            initComponents();

            val_idmVer.valueProperty().bindBidirectional(_tempConfig.getIdmVersionProperty());
            val_idmVer.valueProperty().addListener((observable, oldValue, newValue) -> _tempConfig.initConnectionConfig());
            val_idmVer.setDisable(!_tempConfig.isNew());
            val_idmVer.setMaxWidth(Double.MAX_VALUE);

            val_httpIp.textProperty().bindBidirectional(_tempConfig.getHostNameProperty());
            val_httpIp.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            val_port.textProperty().bindBidirectional(_tempConfig.getPortProperty(), new NumberStringConverter("####"));
            val_port.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            val_sid.textProperty().bindBidirectional(_tempConfig.getSIDProperty());
            val_sid.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            val_login.textProperty().bindBidirectional(_tempConfig.getUserNameProperty());
            val_login.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            val_dbName.textProperty().bindBidirectional(_tempConfig.getDbNameProperty());
            val_dbName.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            val_pass.textProperty().bindBidirectional(_tempConfig.getPasswordProperty());
            val_pass.textProperty().addListener((observable, oldValue, newValue) -> _tempConfig.validate());

            btn_ok.disableProperty().bind(Bindings.not(_tempConfig.isValidProperty()));
            btn_ok.setOnAction(event -> okAction());

            btn_cancel.setOnAction(event -> close());

            TilePane tilePane = new TilePane();
            tilePane.setHgap(10);
            tilePane.setVgap(5);
            tilePane.setPrefColumns(2);
            tilePane.setTileAlignment(Pos.CENTER_LEFT);

            tilePane.getChildren().addAll(lbl_dbType, lbl_dbTypeName);
            tilePane.getChildren().addAll(lbl_idmVer, val_idmVer);
            tilePane.getChildren().addAll(lbl_httpIp, val_httpIp);

            if (_tempConfig.isORAServer() || _tempConfig.isMySQLServer()) {
                tilePane.getChildren().addAll(lbl_port, val_port);
            }

            if (_tempConfig.isMSSQLServer() || _tempConfig.isMySQLServer()) {
                tilePane.getChildren().addAll(lbl_dbName, val_dbName);
            }

            if (_tempConfig.isORAServer()) {
                tilePane.getChildren().addAll(lbl_sid, val_sid);
            }

            tilePane.getChildren().addAll(lbl_login, val_login);
            tilePane.getChildren().addAll(lbl_pass, val_pass);

            root.getChildren().addAll(tilePane, btnBar);

            stage.setScene(new Scene(root));
            stage.getScene().getStylesheets().add(Application.getUserAgentStylesheet());
            stage.setResizable(false);
            return this;
        } catch (Exception ex) {
            Main.LOGGER.error("Error occurred in ConnectionConfigDialog.prepare() method:", ex);
        }
        return null;
    }

    private void okAction() {
        _config = _tempConfig;
        _config.updateDataSource();
        close();
    }

    public ConnectionConfig getConfig() {
        return _config;
    }
}
