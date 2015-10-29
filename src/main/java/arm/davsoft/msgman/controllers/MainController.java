package arm.davsoft.msgman.controllers;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.components.ApplicationTitleBar;
import arm.davsoft.msgman.components.ButtonTableCell;
import arm.davsoft.msgman.components.CheckBoxTableCell;
import arm.davsoft.msgman.domains.FileItem;
import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.IDMVersion;
import arm.davsoft.msgman.implementations.ConnectionConfigImpl;
import arm.davsoft.msgman.implementations.MessageFinderImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.MessageFinder;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.service.MessageTransferService;
import arm.davsoft.msgman.utils.Dialogs;
import arm.davsoft.msgman.utils.FileProcessor;
import arm.davsoft.msgman.utils.ResourceManager;
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
    private Logger logger = Main.LOGGER; // Logger.getLogger(MainController.class)
    private MessageTransferService messageTransferService;
    private MessageFinder messageFinder;

    private StringProperty
            projectPathProperty =            new SimpleStringProperty(),
            serverTypeProperty =             new SimpleStringProperty(),
            serverHostProperty =             new SimpleStringProperty(),
            portNumberProperty =             new SimpleStringProperty(),
            usernameProperty =               new SimpleStringProperty(),
            databaseProperty =               new SimpleStringProperty(),
            emptyMessagesQuantityProperty =  new SimpleStringProperty(),
            messageRangeProperty =           new SimpleStringProperty(),
            totalHardcodedMessagesProperty = new SimpleStringProperty()
            ;
    private ListProperty<FileItem> fileItemsTableViewData = new SimpleListProperty<>();

    private List<Message> emptyMessagesList = new ArrayList<>();
    private List<Message> messagesToTransfer = new ArrayList<>();

//    private Range currentMessageRange;

    private BooleanProperty
            browseProjectMenuItemDisabledProperty =     new SimpleBooleanProperty(false),
            appSettingsMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            exitAppMenuItemDisabledProperty =           new SimpleBooleanProperty(false),
            scanProjectMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            scanDbMenuItemDisabledProperty =            new SimpleBooleanProperty(false),
            generateMessagesMenuItemDisabledProperty =  new SimpleBooleanProperty(false),
            putMessagesMenuItemDisabledProperty =  new SimpleBooleanProperty(false),
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
            lbl_emptyMessagesQuantityVisibleProperty =  new SimpleBooleanProperty(false),
            lbl_messageRangeVisibleProperty =           new SimpleBooleanProperty(false),
            lbl_totalHardcodedMessagesVisibleProperty = new SimpleBooleanProperty(false),

            fileItemsTableViewVisibleProperty =         new SimpleBooleanProperty(false)
            ;


    @FXML
    private VBox rootContainer;
    @FXML
    private MenuItem browseProjectMenuItem, appSettingsMenuItem, exitAppMenuItem, scanProjectMenuItem, scanDbMenuItem,
            generateMessagesMenuItem, putMessagesMenuItem, transferMessagesMenuItem, cleanMessageRangeMenuItem, connectMSSQLMenuItem,
            connectOracleMenuItem, connectMySQLMenuItem, configConnectionMenuItem, aboutAppMenuItem;
    @FXML
    private Button scanProjectButton, transferMessagesButton, cleanMessageRangeButton;
    @FXML
    private CheckMenuItem toggleFullScreenModeMenuItem;
    @FXML
    private TitledPane detailsPanel;
    @FXML
    private Label lbl_projectPath, lbl_serverType, lbl_serverHost, lbl_portNumber, lbl_username, lbl_database, lbl_emptyMessagesQuantity, lbl_messageRange, lbl_totalHardcodedMessages;
    @FXML
    private TableView<FileItem> fileItemsTableView;
    @FXML
    private TableColumn<FileItem, Boolean> checkboxColumn, moreActions;
    @FXML
    private TableColumn<FileItem, String> filePathColumn, selMessages, totMessages;// messageQuantity;



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
        logger.info("Main controller instantiation started.");
        prepareForm();
        logger.info("Main controller instantiated successfully.");
    }

    private void prepareForm() {
        rootContainer.getChildren().add(0, new ApplicationTitleBar(Main.getPrimaryStage(), ResourceManager.getParam("APPLICATION.NAME")));
        initFilesTable();
        initPropertyBindings();
        validate();
    }

    private void initPropertyBindings() {
        // Data bindings
        lbl_projectPath.textProperty().bind(projectPathProperty);
        lbl_serverType.textProperty().bind(serverTypeProperty);
        lbl_serverHost.textProperty().bind(serverHostProperty);
        lbl_portNumber.textProperty().bind(portNumberProperty);
        lbl_username.textProperty().bind(usernameProperty);
        lbl_database.textProperty().bind(databaseProperty);
        lbl_emptyMessagesQuantity.textProperty().bind(emptyMessagesQuantityProperty);
        lbl_messageRange.textProperty().bind(messageRangeProperty);
        lbl_totalHardcodedMessages.textProperty().bind(totalHardcodedMessagesProperty);
        fileItemsTableView.itemsProperty().bind(fileItemsTableViewData);

        // Visibility bindings
        lbl_projectPath.visibleProperty().bind(lbl_projectPathVisibleProperty);
        lbl_serverType.visibleProperty().bind(lbl_serverTypeVisibleProperty);
        lbl_serverHost.visibleProperty().bind(lbl_serverHostVisibleProperty);
        lbl_portNumber.visibleProperty().bind(lbl_portNumberVisibleProperty);
        lbl_username.visibleProperty().bind(lbl_usernameVisibleProperty);
        lbl_database.visibleProperty().bind(lbl_databaseVisibleProperty);
        lbl_emptyMessagesQuantity.visibleProperty().bind(lbl_emptyMessagesQuantityVisibleProperty);
        lbl_messageRange.visibleProperty().bind(lbl_messageRangeVisibleProperty);
        lbl_totalHardcodedMessages.visibleProperty().bind(lbl_totalHardcodedMessagesVisibleProperty);
        fileItemsTableView.visibleProperty().bind(fileItemsTableViewVisibleProperty);

        // Disability bindings
        browseProjectMenuItem.disableProperty().bind(browseProjectMenuItemDisabledProperty);
        appSettingsMenuItem.disableProperty().bind(appSettingsMenuItemDisabledProperty);
        exitAppMenuItem.disableProperty().bind(exitAppMenuItemDisabledProperty);
        scanProjectMenuItem.disableProperty().bind(scanProjectMenuItemDisabledProperty);
        scanDbMenuItem.disableProperty().bind(scanDbMenuItemDisabledProperty);
        generateMessagesMenuItem.disableProperty().bind(generateMessagesMenuItemDisabledProperty);
        putMessagesMenuItem.disableProperty().bind(putMessagesMenuItemDisabledProperty);
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
        lbl_emptyMessagesQuantityVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null);
//        lbl_messageRangeVisibleProperty.set(currentMessageRange != null && currentMessageRange.isValid());
        lbl_messageRangeVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null && messageTransferService.getConfig().getMessagesRange() != null);
        lbl_totalHardcodedMessagesVisibleProperty.set(true);

        detailsPanelVisibleProperty.set(lbl_projectPathVisibleProperty.get()
                        || lbl_serverTypeVisibleProperty.get()
                        || lbl_serverHostVisibleProperty.get()
                        || lbl_portNumberVisibleProperty.get()
                        || lbl_usernameVisibleProperty.get()
                        || lbl_databaseVisibleProperty.get()
                        || lbl_emptyMessagesQuantityVisibleProperty.get()
        );

        fileItemsTableViewVisibleProperty.set(fileItemsTableViewData != null);
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
        putMessagesMenuItemDisabledProperty.set(messageFinder == null || messageTransferService == null);
        transferMessagesMenuItemDisabledProperty.set(messageFinder == null || messageTransferService == null);
        cleanMessageRangeMenuItemDisabledProperty.set(messageFinder == null || messageTransferService == null);

        connectMSSQLMenuItemDisabledProperty.set(false);
        connectOracleMenuItemDisabledProperty.set(false);
        connectMySQLMenuItemDisabledProperty.set(false);

        configConnectionMenuItemDisabledProperty.set(messageTransferService == null || messageTransferService.getConfig() == null);

        aboutAppMenuItemDisabledProperty.set(false);
    }

    private void updateConnectionDetails() {
        if (messageTransferService != null) {
            serverTypeProperty.setValue(messageTransferService.getConfig().getDbServerType().getName());
            serverHostProperty.setValue(messageTransferService.getConfig().getHostName());
            portNumberProperty.setValue(String.valueOf(messageTransferService.getConfig().getPort()));
            usernameProperty.setValue(messageTransferService.getConfig().getUserName());
            databaseProperty.setValue(messageTransferService.getConfig().getDbName());
            messageRangeProperty.setValue(messageTransferService.getConfig().getMessagesRange().toString());
        }

        if (emptyMessagesList != null) {
            emptyMessagesQuantityProperty.setValue(String.valueOf(emptyMessagesList.size()));
        }

        if (fileItemsTableViewData != null && fileItemsTableViewData.get() != null) {
            int quantity = 0;
            for (FileItem fileItem : fileItemsTableViewData.get()) {
                quantity += fileItem.getTotalMessagesQuantity();
            }
            totalHardcodedMessagesProperty.set(String.valueOf(quantity));
            logger.info("Total hardcoded messages: " + quantity);
        }
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

            selMessages.setCellValueFactory(new PropertyValueFactory<>("selMessagesQuantity"));
            selMessages.setPrefWidth(80);
            selMessages.setMinWidth(selMessages.getPrefWidth());
            selMessages.setMaxWidth(selMessages.getPrefWidth());

            totMessages.setCellValueFactory(new PropertyValueFactory<>("totMessagesQuantity"));
            totMessages.setPrefWidth(80);
            totMessages.setMinWidth(totMessages.getPrefWidth());
            totMessages.setMaxWidth(totMessages.getPrefWidth());

            moreActions.setSortable(false);
            moreActions.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
            moreActions.setCellFactory(p -> new ButtonTableCell<FileItem>("More...") {
                @Override
                public void doAction(FileItem rowItem) {
                    Dialogs.showMessagesDialog(rowItem);
                }
            });
            moreActions.setPrefWidth(80);
            moreActions.setMinWidth(moreActions.getPrefWidth());
            moreActions.setMaxWidth(moreActions.getPrefWidth());
//            moreActions.setVisible(false);

            fileItemsTableView.setEditable(true);
        } catch (Exception ex) {
            logger.error("Error occurred in initFilesTable method: ", ex);
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
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                logger.info("Project scan started.");
                updateTitle(ResourceManager.getMessage("label.menuItem.edit.scanProject"));
                if (messageFinder != null) {
                    fileItemsTableViewData.clear();
                    updateMessage(ResourceManager.getMessage("label.scanningProjectForMessages"));
                    messageFinder.findFileList();
                    updateTitle(ResourceManager.getMessage("label.filteringEmptyMessages"));
                    List<FileItem> retVal = new ArrayList<>();
                    if (messageFinder != null) {
                        int workDone = 0;
                        int workToDo = messageFinder.getFilesList().size();
                        for (FileItem item : messageFinder.getFilesList()) {
                            if (item.getLineItems().size() > 0) {
                                retVal.add(item);
                            }
                            workDone++;
                            updateMessage(ResourceManager.getMessage("label.filtered") + workDone + " / " + workToDo);
                            updateProgress(workDone, workToDo);
                            Thread.sleep(1);
                        }
                    }
                    updateMessage(ResourceManager.getMessage("label.finalizing"));
                    fileItemsTableViewData.setValue(FXCollections.observableArrayList(retVal));
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.projectScanSucceeded"));
            }

            @Override
            protected void failed() {
                super.failed();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.projectScanFailed"));
            }
        };

        Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void scanForNewMessagesInDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.scanDB"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
            if (checkTheMessageRangeToBeSet()) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        logger.info("DB scan started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.scanDB"));
                        messageTransferService.generateNewEmptyMessages();
                        initEmptyMessages();
                        Thread.sleep(3000); // FIXME: UNCOMMENT the 2 lines above and REMOVE this line after testing!!!
                        validate();
                        logger.info("DB scan completed.");
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
    private void generateEmptyMessages(ActionEvent event) {
        Range messageRange = Dialogs.showRangeDialog("Message Range", "Please define message range.");
        if (messageRange != null && messageRange.isValid()) {
            if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        logger.info("Message generation started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"));
                        messageTransferService.generateNewEmptyMessages(messageRange);
                        initEmptyMessages();
                        validate();
                        logger.info("Message generation completed.");
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
    private void putMessagesToFiles(ActionEvent event) {
        try {
            putMessagesToFiles(messageFinder.getMarkedFiles(), messageTransferService.loadMessages());
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @FXML
    private void transferMessagesToDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.transferToDB"), null, ResourceManager.getMessage("label.confirmation.transferToDB"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    logger.info("Message transfer started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.transferToDB"));
                    updateMessage("Preparing messages for transfer...");
                    logger.info("Preparing messages for transfer...");
                    logger.info("DB Name: " + messageTransferService.getConfig().getDbName());
                    prepareMessagesForTransfer();
                    StringBuilder transferredMessagesList = new StringBuilder();
                    List<Message> messagesToTransfer = getMessagesToTransfer();
                    int totalMessagesToTransfer = messagesToTransfer.size();
                    int messageCounter = 0;
                    Iterator<Message> messageIterator = messagesToTransfer.iterator();
                    while (messageIterator.hasNext()) {
                        Message message = messageIterator.next();
                        messageTransferService.transferMessage(message);
                        updateMessage("Messages transferred: " + messageCounter + "/" + totalMessagesToTransfer);
                        updateProgress(++messageCounter, totalMessagesToTransfer);
                        transferredMessagesList.append(message.getId());
                        if (messageIterator.hasNext()) {
                            transferredMessagesList.append(", ");
                        }
                    }
                    logger.info("Messages transferred: " + transferredMessagesList.toString());
                    updateMessage("Getting empty messages...");
                    logger.info("Getting empty messages...");
                    initEmptyMessages();
                    validate();
                    logger.info("Message transfer completed.");
                    return null;
                }
            };

            Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, true);

            task.setOnSucceeded(event1 -> {
                try {
                    String confirmTitle = ResourceManager.getMessage("title.dialog.putToFiles");
                    String confirmContent = ResourceManager.getMessage("label.confirmation.putToFilesAfterTransferringToDB");
                    if (Dialogs.showConfirmPopup(confirmTitle, null, confirmContent)) {
                        putMessagesToFiles(messageFinder.getMarkedFiles(), messagesToTransfer);
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            });
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    private void removeUnusedMessagesFromDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.removeFromDB"), null, ResourceManager.getMessage("label.confirmation.removeFromDB"))) {
            if (checkTheMessageRangeToBeSet()) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        logger.info("Message cleaning started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.removeFromDB"));
                        messageTransferService.removeUnusedMessages(getUsedMessages());
                        initEmptyMessages();
                        validate();
                        logger.info("Message cleaning completed.");
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
        logger.info("Application terminated by user.");
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
            initEmptyMessages();
            updateConnectionDetails();
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
            initEmptyMessages();
            updateConnectionDetails();
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
            initEmptyMessages();
            updateConnectionDetails();
        }
        validate();
    }

    @FXML
    private void configureConnection(ActionEvent event) {
        ConnectionConfig config = Dialogs.showConnectionPopup(rootContainer.getScene().getWindow(), messageTransferService.getConfig());
        if (config != null) {
            messageTransferService = new MessageTransferService(config);
            updateConnectionDetails();
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

    @FXML
    private void specifyNewMessageRange(ActionEvent event) {
        messageTransferService.getConfig().setMessagesRange(Dialogs.showRangeDialog("Message Range", "Please define message range."));
        updateConnectionDetails();
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
                    logger.info("Message transfer to files started.");
                    updateTitle("Put messages into files.");
                    updateMessage("Preparing...");
                    logger.info("Preparing...");
                    int counter = 0;
                    for (File file : filesList) {
                        updateMessage(file.getName());
                        FileProcessor.replaceFileTags(file, messages);
                        updateProgress(++counter, filesList.size());
                    }
                    updateMessage("Finalizing...");
                    logger.info("Finalizing...");
                    logger.info("Message transfer to files completed.");
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).error("Error occurred in putMessagesToFiles method: ", ex);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Dialogs.showConfirmPopup("Scan project?", null, "Scan project once more?")) {
                    scanForNewMessagesInProject(new ActionEvent());
                }
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
        if (checkTheMessageRangeToBeSet()) {
            emptyMessagesList = messageTransferService.loadEmptyMessages();
        }
        updateConnectionDetails();
    }

    private boolean checkTheMessageRangeToBeSet() {
        if (messageTransferService.getConfig().getMessagesRange() == null) {
            specifyNewMessageRange(new ActionEvent());
        }
        return messageTransferService.getConfig().isMessageRangeValid();
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
                logger.info("Dummy task started.");
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
                logger.info("Dummy task completed.");
                return null;
            }
        };

        Dialogs.showTaskProgressDialog(primaryStage, task, true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
