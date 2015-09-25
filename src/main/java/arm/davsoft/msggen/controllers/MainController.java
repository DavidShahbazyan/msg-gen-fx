package arm.davsoft.msggen.controllers;

import arm.davsoft.msggen.interfaces.ConnectionConfig;
import arm.davsoft.msggen.components.ButtonTableCell;
import arm.davsoft.msggen.components.CheckBoxTableCell;
import arm.davsoft.msggen.domains.FileItem;
import arm.davsoft.msggen.domains.Message;
import arm.davsoft.msggen.enums.DBServerType;
import arm.davsoft.msggen.enums.IDMVersion;
import arm.davsoft.msggen.implementations.ConnectionConfigImpl;
import arm.davsoft.msggen.implementations.MessageFinderImpl;
import arm.davsoft.msggen.interfaces.MessageFinder;
import arm.davsoft.msggen.interfaces.Range;
import arm.davsoft.msggen.service.MessageTransferService;
import arm.davsoft.msggen.utils.Dialogs;
import arm.davsoft.msggen.utils.FileProcessor;
import arm.davsoft.msggen.utils.ResourceManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/18/15 <br/>
 * <b>Time:</b> 2:24 PM <br/>
 */
public class MainController implements Initializable {
    private MessageTransferService messageTransferService;
    private MessageFinder messageFinder;

    private StringProperty
            projectPathProperty = new SimpleStringProperty(),
            serverTypeProperty = new SimpleStringProperty(),
            serverHostProperty = new SimpleStringProperty(),
            portNumberProperty = new SimpleStringProperty(),
            usernameProperty = new SimpleStringProperty(),
            databaseProperty = new SimpleStringProperty()
            ;
    private ListProperty<FileItem> fileItemsTableViewData = new SimpleListProperty<>();

    private List<Message> emptyMessagesList = new ArrayList<>();
    private List<Message> messagesToTransfer = new ArrayList<>();

    private Range currentMessageRange;

    private BooleanProperty
            browseProjectMenuItemDisabledProperty =     new SimpleBooleanProperty(false),
            appSettingsMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            exitAppMenuItemDisabledProperty =           new SimpleBooleanProperty(false),
            scanProjectMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            scanDbMenuItemDisabledProperty =            new SimpleBooleanProperty(false),
            generateMessagesMenuItemDisabledProperty =  new SimpleBooleanProperty(false),
            transferMessagesMenuItemDisabledProperty =  new SimpleBooleanProperty(false),
            cleanMessageRangeMenuItemDisabledProperty = new SimpleBooleanProperty(false),
            connectMSSQLMenuItemDisabledProperty =      new SimpleBooleanProperty(false),
            connectOracleMenuItemDisabledProperty =     new SimpleBooleanProperty(false),
            connectMySQLMenuItemDisabledProperty =      new SimpleBooleanProperty(false),
            configConnectionMenuItemDisabledProperty =  new SimpleBooleanProperty(false),
            aboutAppMenuItemDisabledProperty =          new SimpleBooleanProperty(false),

            detailsPanelVisibleProperty =               new SimpleBooleanProperty(false),
            lbl_projectPathVisibleProperty =            new SimpleBooleanProperty(false),
            lbl_serverTypeVisibleProperty =             new SimpleBooleanProperty(false),
            lbl_serverHostVisibleProperty =             new SimpleBooleanProperty(false),
            lbl_portNumberVisibleProperty =             new SimpleBooleanProperty(false),
            lbl_usernameVisibleProperty =               new SimpleBooleanProperty(false),
            lbl_databaseVisibleProperty =               new SimpleBooleanProperty(false),

            fileItemsTableViewVisibleProperty =         new SimpleBooleanProperty(false)
            ;


    @FXML
    private VBox rootContainer;
    @FXML
    private MenuItem browseProjectMenuItem, appSettingsMenuItem, exitAppMenuItem, scanProjectMenuItem, scanDbMenuItem,
            generateMessagesMenuItem, transferMessagesMenuItem, cleanMessageRangeMenuItem, connectMSSQLMenuItem,
            connectOracleMenuItem, connectMySQLMenuItem, configConnectionMenuItem, aboutAppMenuItem;
    @FXML
    private Button scanProjectButton, transferMessagesButton, cleanMessageRangeButton;
    @FXML
    private CheckMenuItem toggleFullScreenModeMenuItem;
    @FXML
    private TitledPane detailsPanel;
    @FXML
    private Label lbl_projectPath, lbl_serverType, lbl_serverHost, lbl_portNumber, lbl_username, lbl_database;
    @FXML
    private TableView<FileItem> fileItemsTableView;
    @FXML
    private TableColumn<FileItem, Boolean> checkboxColumn, moreActions;
    @FXML
    private TableColumn<FileItem, String> filePathColumn, messageQuantity;



    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.getLogger(MainController.class).info("Main controller instantiation started.");
        prepareForm();
        Logger.getLogger(MainController.class).info("Main controller instantiated successfully.");
    }

    private void prepareForm() {
        initFilesTable();
        initPropertyBindings();
        validate();
    }

    private void initPropertyBindings() {
        lbl_projectPath.textProperty().bind(projectPathProperty);
        lbl_serverType.textProperty().bind(serverTypeProperty);
        lbl_serverHost.textProperty().bind(serverHostProperty);
        lbl_portNumber.textProperty().bind(portNumberProperty);
        lbl_username.textProperty().bind(usernameProperty);
        lbl_database.textProperty().bind(databaseProperty);


        lbl_projectPath.visibleProperty().bind(lbl_projectPathVisibleProperty);
        lbl_serverType.visibleProperty().bind(lbl_serverTypeVisibleProperty);
        lbl_serverHost.visibleProperty().bind(lbl_serverHostVisibleProperty);
        lbl_portNumber.visibleProperty().bind(lbl_portNumberVisibleProperty);
        lbl_username.visibleProperty().bind(lbl_usernameVisibleProperty);
        lbl_database.visibleProperty().bind(lbl_databaseVisibleProperty);

        fileItemsTableView.visibleProperty().bind(fileItemsTableViewVisibleProperty);

        browseProjectMenuItem.disableProperty().bind(browseProjectMenuItemDisabledProperty);
        appSettingsMenuItem.disableProperty().bind(appSettingsMenuItemDisabledProperty);
        exitAppMenuItem.disableProperty().bind(exitAppMenuItemDisabledProperty);
        scanProjectMenuItem.disableProperty().bind(scanProjectMenuItemDisabledProperty);
        scanDbMenuItem.disableProperty().bind(scanDbMenuItemDisabledProperty);
        generateMessagesMenuItem.disableProperty().bind(generateMessagesMenuItemDisabledProperty);
        transferMessagesMenuItem.disableProperty().bind(transferMessagesMenuItemDisabledProperty);
        cleanMessageRangeMenuItem.disableProperty().bind(cleanMessageRangeMenuItemDisabledProperty);
        connectMSSQLMenuItem.disableProperty().bind(connectMSSQLMenuItemDisabledProperty);
        connectOracleMenuItem.disableProperty().bind(connectOracleMenuItemDisabledProperty);
        connectMySQLMenuItem.disableProperty().bind(connectMySQLMenuItemDisabledProperty);
        configConnectionMenuItem.disableProperty().bind(configConnectionMenuItemDisabledProperty);
        aboutAppMenuItem.disableProperty().bind(aboutAppMenuItemDisabledProperty);

        scanProjectButton.disableProperty().bind(scanProjectMenuItemDisabledProperty);
        transferMessagesButton.disableProperty().bind(transferMessagesMenuItemDisabledProperty);
        cleanMessageRangeButton.disableProperty().bind(cleanMessageRangeMenuItemDisabledProperty);

        fileItemsTableView.itemsProperty().bind(fileItemsTableViewData);
    }

    private void validate() {
        checkVisibility();
        checkEditability();
    }

    private void checkVisibility() {
        /**
         * VISIBLE = true
         * INVISIBLE = false
         */

        lbl_projectPathVisibleProperty.set(messageFinder != null);
        lbl_serverTypeVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);
        lbl_serverHostVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);
        lbl_portNumberVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);
        lbl_usernameVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);
        lbl_databaseVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);

        detailsPanelVisibleProperty.set(lbl_projectPathVisibleProperty.get()
                        || lbl_serverTypeVisibleProperty.get()
                        || lbl_serverHostVisibleProperty.get()
                        || lbl_portNumberVisibleProperty.get()
                        || lbl_usernameVisibleProperty.get()
                        || lbl_databaseVisibleProperty.get()
        );

        fileItemsTableViewVisibleProperty.set(!fileItemsTableViewData.isEmpty());
    }

    private void checkEditability() {
        /**
         * ENABLED = false
         * DISABLED = true
         */

        browseProjectMenuItemDisabledProperty.set(false);
        appSettingsMenuItemDisabledProperty.set(false);
        exitAppMenuItemDisabledProperty.set(false);

        scanProjectMenuItemDisabledProperty.set(messageFinder == null);
        scanDbMenuItemDisabledProperty.set(messageTransferService == null || messageTransferService.getConfig() == null || messageTransferService.getConfig().getDbName() == null || messageTransferService.getConfig().getDbName().equals(""));
        generateMessagesMenuItemDisabledProperty.set(messageTransferService == null || messageTransferService.getConfig() == null || messageTransferService.getConfig().getDbName() == null || messageTransferService.getConfig().getDbName().equals(""));
        transferMessagesMenuItemDisabledProperty.set(messageFinder == null || messageTransferService == null);
        cleanMessageRangeMenuItemDisabledProperty.set(messageFinder == null || messageTransferService == null);

        connectMSSQLMenuItemDisabledProperty.set(false);
        connectOracleMenuItemDisabledProperty.set(false);
        connectMySQLMenuItemDisabledProperty.set(false);

        configConnectionMenuItemDisabledProperty.set(messageTransferService == null || messageTransferService.getConfig() == null);

        aboutAppMenuItemDisabledProperty.set(false);
    }

    private void updateConnectionDetails(ConnectionConfig config) {
        serverTypeProperty.setValue(config.getDbServerType().getName());
        serverHostProperty.setValue(config.getHostName());
        portNumberProperty.setValue(String.valueOf(config.getPort()));
        usernameProperty.setValue(config.getUserName());
        databaseProperty.setValue(config.getDbName());
    }

    private void initFilesTable() {
        try {
            CheckBox selectAllFilesCheckBox = new CheckBox();
            selectAllFilesCheckBox.setOnAction(event -> setAllFilesSelected(selectAllFilesCheckBox.isSelected()));

            checkboxColumn.setSortable(false);
            checkboxColumn.setGraphic(selectAllFilesCheckBox);
            checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
            checkboxColumn.setCellFactory(param -> new CheckBoxTableCell<>());
            checkboxColumn.setPrefWidth(32);
            checkboxColumn.setMinWidth(checkboxColumn.getPrefWidth());
            checkboxColumn.setMaxWidth(checkboxColumn.getPrefWidth());
            checkboxColumn.setEditable(true);

            filePathColumn.setCellValueFactory(new PropertyValueFactory<>("relativePath"));
            messageQuantity.setCellValueFactory(new PropertyValueFactory<>("messagesSelTot"));
            messageQuantity.setPrefWidth(80);
            messageQuantity.setMinWidth(messageQuantity.getPrefWidth());
            messageQuantity.setMaxWidth(messageQuantity.getPrefWidth());

            moreActions.setSortable(false);
            moreActions.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
            moreActions.setCellFactory(p -> new ButtonTableCell<FileItem>("More...") {
                @Override
                public void doAction(FileItem rowItem) {
                    refreshFilesTableData();
                }
            });
            moreActions.setPrefWidth(80);
            moreActions.setMinWidth(moreActions.getPrefWidth());
            moreActions.setMaxWidth(moreActions.getPrefWidth());

            fileItemsTableView.setEditable(true);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class).error("Error occurred in initFilesTable method: ", ex);
            throw ex;
        }
    }

    private void refreshFilesTableData() {
//        fileItemsTableView.getItems().clear();
//        fileItemsTableView.setItems(FXCollections.observableArrayList(getFilesList()));
    }

    /* ------------- Main Menu Actions ------------- */

    @FXML
    private void toggleFullScreenMode(ActionEvent event) {
        Stage stage = (Stage) rootContainer.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
        stage.centerOnScreen();
        toggleFullScreenModeMenuItem.setSelected(stage.isFullScreen());
    }

    @FXML
    private void browseProject(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ResourceManager.getMessage("title.dialog.browseProject"));
        directoryChooser.setInitialDirectory(Dialogs.getUserHomeDir());
        File file = directoryChooser.showDialog(Dialogs.getParentWindow(rootContainer));
        if (file != null) {
            projectPathProperty.setValue(file.getAbsolutePath());
            this.messageFinder = new MessageFinderImpl(file.getAbsolutePath());
        }
        validate();
    }

    @FXML
    private void openAppSettings(ActionEvent event) {
        Dialogs.showSettingsDialog(rootContainer.getScene().getWindow());
        validate();
    }

    @FXML
    private void scanForNewMessagesInProject(ActionEvent event) {
//        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.scanProject"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    Logger.getLogger(MainController.class).info("Project scan started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.scanProject"));
                    if (messageFinder != null) {
                        updateMessage(ResourceManager.getMessage("label.scanningProjectForMessages"));
                        messageFinder.findFileList();
                        updateMessage(ResourceManager.getMessage("label.filteringEmptyMessages"));
                        List<FileItem> retVal = new ArrayList<>();
                        if (messageFinder != null) {
                            for (FileItem item : messageFinder.getFilesList()) {
                                if (item.getLineItems().size() > 0) {
                                    retVal.add(item);
                                }
                            }
                        }
                        updateMessage(ResourceManager.getMessage("label.finalizing"));
                        fileItemsTableViewData.setValue(FXCollections.observableArrayList(retVal));
                    }
                    validate();
                    Logger.getLogger(MainController.class).info("Project scan completed.");
                    return null;
                }
            };

            Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, true);

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
//        }
    }

    @FXML
    private void scanForNewMessagesInDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.scanDB"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    Logger.getLogger(MainController.class).info("DB scan started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.scanDB"));
//                    messageTransferService.generateNewEmptyMessages(currentMessageRange);
//                    initEmptyMessages();
                    Thread.sleep(3000); // FIXME: UNCOMMENT the 2 lines above and REMOVE this line after testing!!!
                    validate();
                    Logger.getLogger(MainController.class).info("DB scan completed.");
                    return null;
                }
            };

            Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, false);

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    private void generateEmptyMessages(ActionEvent event) {
        currentMessageRange = Dialogs.showRangeDialog("Message Range", "Please define message range.");
        if (currentMessageRange != null) {
            if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Logger.getLogger(MainController.class).info("Message generation started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"));
                        messageTransferService.generateNewEmptyMessages(currentMessageRange);
                        initEmptyMessages();
    //                    Thread.sleep(3000); // FIXME: UNCOMMENT the 2 lines above and REMOVE this line after testing!!!
                        validate();
                        Logger.getLogger(MainController.class).info("Message generation completed.");
                        return null;
                    }
                };

                Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, false);

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        }
    }

    @FXML
    private void transferMessagesToDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.transferToDB"), null, ResourceManager.getMessage("label.confirmation.transferToDB"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    Logger.getLogger(MainController.class).info("Message transfer started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.transferToDB"));
                    updateMessage("Preparing messages for transfer...");
                    prepareMessagesForTransfer();
                    List<Message> messagesToTransfer = getMessagesToTransfer();
                    int totalMessagesToTransfer = messagesToTransfer.size();
                    int messageCounter = 0;
                    Iterator<Message> messageIterator = messagesToTransfer.iterator();
                    while (messageIterator.hasNext()) {
                        Message message = messageIterator.next();
                        messageTransferService.transferMessage(message);
                        updateMessage("Messages transferred: " + messageCounter + "/" + totalMessagesToTransfer);
                        updateProgress(++messageCounter, totalMessagesToTransfer);
                    }
                    updateMessage("Putting messages into files...");
                    putMessagesToFiles(messageFinder.getMarkedFiles(), messagesToTransfer);
                    updateMessage("Getting empty messages...");
                    initEmptyMessages();
                    validate();
                    Logger.getLogger(MainController.class).info("Message transfer completed.");
                    return null;
                }
            };

            Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, true);

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    private void removeUnusedMessagesFromDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.removeFromDB"), null, ResourceManager.getMessage("label.confirmation.removeFromDB"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    Logger.getLogger(MainController.class).info("Message cleaning started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.removeFromDB"));
                    messageTransferService.removeUnusedMessages(currentMessageRange, getUsedMessages());
                    initEmptyMessages();
                    validate();
                    Logger.getLogger(MainController.class).info("Message cleaning completed.");
                    return null;
                }
            };

            Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, false);

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

/*
    @FXML
    private void minimizeApp(ActionEvent event) {
        Main.getPrimaryStage().setIconified(true);
    }

    @FXML
    private void maximizeRestoreApp(ActionEvent event) {
        Main.getPrimaryStage().setMaximized(!Main.getPrimaryStage().isMaximized());
        Main.getPrimaryStage().centerOnScreen();
    }
*/

    @FXML
    private void exitApp(ActionEvent event) {
        Logger.getLogger(MainController.class).info("Application terminated by user.");
        Platform.exit();
    }

    @FXML
    private void connectToMSSQL(ActionEvent event) {
        IDMVersion idmVersion = Dialogs.showIDMVersionPopup();
        ConnectionConfig config = null;
        if (idmVersion != null) {
            config = Dialogs.showConnectionPopup(rootContainer.getScene().getWindow(), new ConnectionConfigImpl(idmVersion, DBServerType.MSSQLServer));
        }
        if (config != null && config.isValid()) {
            messageTransferService = new MessageTransferService(config);
            config.setDbName(Dialogs.showSchemaNamesPopup(messageTransferService.loadSchemaNames()));
            updateConnectionDetails(config);
        }
        validate();
    }

    @FXML
    private void connectToOracle(ActionEvent event) {
        IDMVersion idmVersion = Dialogs.showIDMVersionPopup();
        ConnectionConfig config = null;
        if (idmVersion != null) {
            config = Dialogs.showConnectionPopup(rootContainer.getScene().getWindow(), new ConnectionConfigImpl(idmVersion, DBServerType.ORAServer));
        }
        if (config != null && config.isValid()) {
            messageTransferService = new MessageTransferService(config);
            updateConnectionDetails(config);
        }
        validate();
    }

    @FXML
    private void connectToMySQL(ActionEvent event) {
        IDMVersion idmVersion = Dialogs.showIDMVersionPopup();
        ConnectionConfig config = null;
        if (idmVersion != null) {
            config = Dialogs.showConnectionPopup(rootContainer.getScene().getWindow(), new ConnectionConfigImpl(idmVersion, DBServerType.MySQLServer));
        }
        if (config != null && config.isValid()) {
            messageTransferService = new MessageTransferService(config);
            config.setDbName(Dialogs.showSchemaNamesPopup(messageTransferService.loadSchemaNames()));
            updateConnectionDetails(config);
        }
        validate();
    }

    @FXML
    private void configureConnection(ActionEvent event) {
        ConnectionConfig config = Dialogs.showConnectionPopup(rootContainer.getScene().getWindow(), messageTransferService.getConfig());
        if (config != null) {
            messageTransferService = new MessageTransferService(config);
            updateConnectionDetails(config);
        }
        validate();
    }

    @FXML
    private void aboutApp(ActionEvent event) {
//        String title = "About App";
//        String header = ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.VERSION");
//        String content = ResourceManager.getMessage("label.aboutTheApp");
//        Dialogs.showInfoPopup(title, header, content);
        Dialogs.showAboutAppDialog(rootContainer.getScene().getWindow());
    }
    /* ------------- /Main Menu Actions ------------- */


    /* ------------- Other methods ------------- */

    private void setAllFilesSelected(boolean isSelected) {
        for (FileItem item : fileItemsTableView.getItems()) {
            item.setIsSelected(isSelected);
        }
        validate();
    }

    private void putMessagesToFiles(List<File> filesList, List<Message> messages) throws IOException {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    Logger.getLogger(MainController.class).info("Message transfer to files started.");
                    updateTitle("Put messages into files.");
                    updateMessage("Preparing...");
                    int counter = 0;
                    for (File file : filesList) {
                        updateMessage(file.getName());
                        FileProcessor.replaceFileTags(file, messages);
                        updateProgress(++counter, filesList.size());
                    }
                    updateMessage("Finalizing...");
                    Logger.getLogger(MainController.class).info("Message transfer to files completed.");
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).error("Error occurred in putMessagesToFiles method: ", ex);
                }
                return null;
            }
        };

        Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    
    private Set<Integer> getUsedMessages() {
        Set<Integer> usedMessages = null;
        try {
            usedMessages = messageFinder.findUsedMessages();
        } catch (IOException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return usedMessages;
    }

    private void prepareMessagesForTransfer() {
        messageFinder.initSelectedMessages();
        Iterator<Message> emptyMessagesIterator = emptyMessagesList.iterator();
        for (String msgText : messageFinder.getSelectedMessages()) {
            if (emptyMessagesIterator.hasNext()) {
                Message message = emptyMessagesIterator.next();
                if (message.isEmpty()) {
                    message.setText(msgText);
                }
            }
        }
    }

    private List<Message> getMessagesToTransfer() {
        messagesToTransfer = new ArrayList<>();
        for (Message message : emptyMessagesList) {
            if (!message.isEmpty()) {
                messagesToTransfer.add(message);
            }
        }
        return messagesToTransfer;
    }

    private void initEmptyMessages() {
        emptyMessagesList = messageTransferService.loadEmptyMessages(currentMessageRange);
    }
    /* ------------- /Other methods ------------- */





    // FIXME: REMOVE the methods below after testing!!!

    @FXML
    private void testAction(ActionEvent event) {
        if (Dialogs.showConfirmPopup("I'm ready to start!", "Task will now start.", "Continue?")) {
            dummyTask(Dialogs.getParentWindow((Node) event.getTarget()));
        }
    }

    private void dummyTask(Window primaryStage) {
        final int[] i = {0};
        int limit = 1000;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Logger.getLogger(MainController.class).info("Dummy task started.");
                updateMessage("Preparing...");
                Thread.sleep(3000);
                String s = "";
                while (i[0] != limit) {
                    i[0]++;
                    s = i[0] + "/" + limit;
                    updateMessage("Processed: " + s);
                    updateProgress(i[0], limit);
                    Thread.sleep(10);
                }
                updateMessage("Finalizing...");
                Thread.sleep(3000);
                Logger.getLogger(MainController.class).info("Dummy task completed.");
                return null;
            }
        };

        Dialogs.showTaskProgressDialog(primaryStage, task, true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
