package arm.davsoft.msgman.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import arm.davsoft.msgman.components.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.apache.log4j.Logger;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.domains.FileItem;
import arm.davsoft.msgman.domains.IntegerRange;
import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.ErrorCode;
import arm.davsoft.msgman.implementations.MessageFinderImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.MessageFinder;
import arm.davsoft.msgman.service.MessageTransferService;
import arm.davsoft.msgman.utils.Dialogs;
import arm.davsoft.msgman.utils.FileProcessor;
import arm.davsoft.msgman.utils.ResourceManager;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/18/15 <br/>
 * <b>Time:</b> 2:24 PM <br/>
 */
public class MainScreenController implements Initializable {
    private Logger logger = Main.LOGGER; // Logger.getLogger(MainScreenController.class)
    private MessageTransferService messageTransferService;
    private MessageFinder messageFinder;

    private StringProperty
            projectPathProperty =            new SimpleStringProperty(),
            serverTypeProperty =             new SimpleStringProperty(),
            serverHostProperty =             new SimpleStringProperty(),
            portNumberProperty =             new SimpleStringProperty(),
            usernameProperty =               new SimpleStringProperty(),
            databaseProperty =               new SimpleStringProperty(),
            messageRangeProperty =           new SimpleStringProperty();

    private IntegerProperty emptyMessagesQuantityProperty =  new SimpleIntegerProperty(),
                            totalHardcodedMessagesProperty = new SimpleIntegerProperty();

    private ListProperty<FileItem> fileItemsTableViewData = new SimpleListProperty<>();

    private List<Message> emptyMessagesList = new ArrayList<>();
    private List<Message> messagesToTransfer = new ArrayList<>();

//    private Range currentMessageRange;

    private BooleanProperty
            browseProjectMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            appSettingsMenuItemDisabledProperty =         new SimpleBooleanProperty(false),
            appRestartMenuItemDisabledProperty =          new SimpleBooleanProperty(false),
            exitAppMenuItemDisabledProperty =             new SimpleBooleanProperty(false),
            scanProjectMenuItemDisabledProperty =         new SimpleBooleanProperty(false),
            scanDbMenuItemDisabledProperty =              new SimpleBooleanProperty(false),
            generateMessagesMenuItemDisabledProperty =    new SimpleBooleanProperty(false),
            putMessagesMenuItemDisabledProperty =         new SimpleBooleanProperty(false),
            transferMessagesMenuItemDisabledProperty =    new SimpleBooleanProperty(false),
            cleanMessageRangeMenuItemDisabledProperty =   new SimpleBooleanProperty(false),
            connectMSSQLMenuItemDisabledProperty =        new SimpleBooleanProperty(false),
            connectOracleMenuItemDisabledProperty =       new SimpleBooleanProperty(false),
            connectMySQLMenuItemDisabledProperty =        new SimpleBooleanProperty(false),
            configConnectionMenuItemDisabledProperty =    new SimpleBooleanProperty(false),
            availableTagsListMenuItemDisabledProperty =   new SimpleBooleanProperty(false),
            aboutAppMenuItemDisabledProperty =            new SimpleBooleanProperty(false),
            showHrdcddMsgsInPrjMenuItemDisabledProperty = new SimpleBooleanProperty(false),

            detailsPanelVisibleProperty =                 new SimpleBooleanProperty(false),
            lbl_projectPathVisibleProperty =              new SimpleBooleanProperty(false),
            lbl_serverTypeVisibleProperty =               new SimpleBooleanProperty(false),
            lbl_serverHostVisibleProperty =               new SimpleBooleanProperty(false),
            lbl_portNumberVisibleProperty =               new SimpleBooleanProperty(false),
            lbl_usernameVisibleProperty =                 new SimpleBooleanProperty(false),
            lbl_databaseVisibleProperty =                 new SimpleBooleanProperty(false),
            lbl_emptyMessagesQuantityVisibleProperty =    new SimpleBooleanProperty(false),
            lbl_messageRangeVisibleProperty =             new SimpleBooleanProperty(false),
            lbl_totalHardcodedMessagesVisibleProperty =   new SimpleBooleanProperty(false),

            fileItemsTableViewVisibleProperty =           new SimpleBooleanProperty(false)
            ;


    @FXML
    private VBox rootContainer;
    @FXML
    private Menu newConnectionMenu;
    @FXML
    private MenuItem browseProjectMenuItem, appSettingsMenuItem, appRestartMenuItem, exitAppMenuItem, scanProjectMenuItem, scanDbMenuItem,
            generateMessagesMenuItem, putMessagesMenuItem, transferMessagesMenuItem, cleanMessageRangeMenuItem,
            connectMSSQLMenuItem, connectOracleMenuItem, connectMySQLMenuItem, configConnectionMenuItem,
            availableTagsListMenuItem, submitIssueMenuItem, aboutAppMenuItem, showHrdcddMsgsInPrjMenuItem;
//    @FXML
//    private Button scanProjectButton, transferMessagesButton, cleanMessageRangeButton;
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
    @FXML
    private StatusBar statusBar;


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
        initIcons();
        initFilesTable();
        initPropertyBindings();
        validate();
    }

    private void initIcons() {
        browseProjectMenuItem.setGraphic(new ImageView("images/icons/general/menu-open.png"));
        appSettingsMenuItem.setGraphic(new ImageView("images/icons/general/settings.png"));
        appRestartMenuItem.setGraphic(new ImageView("images/icons/general/refresh.png"));
        exitAppMenuItem.setGraphic(new ImageView("images/icons/general/exit.png"));
        scanProjectMenuItem.setGraphic(new ImageView("images/icons/general/menu-find.png"));
        scanDbMenuItem.setGraphic(new ImageView("images/icons/general/menu-find.png"));
        generateMessagesMenuItem.setGraphic(new ImageView("images/icons/general/add.png"));
        putMessagesMenuItem.setGraphic(new ImageView("images/icons/general/checkOut.png"));
        transferMessagesMenuItem.setGraphic(new ImageView("images/icons/general/commit.png"));
        cleanMessageRangeMenuItem.setGraphic(new ImageView("images/icons/general/gc.png"));
        newConnectionMenu.setGraphic(new ImageView("images/icons/general/add.png"));
        connectMSSQLMenuItem.setGraphic(new ImageView("images/icons/general/sqlServer.png"));
        connectOracleMenuItem.setGraphic(new ImageView("images/icons/general/oracle.png"));
        connectMySQLMenuItem.setGraphic(new ImageView("images/icons/general/mysql.png"));
        configConnectionMenuItem.setGraphic(new ImageView("images/icons/general/edit.png"));
        availableTagsListMenuItem.setGraphic(new ImageView("images/icons/general/list.png"));
        submitIssueMenuItem.setGraphic(new ImageView("images/icons/general/quickfixBulb.png"));
        aboutAppMenuItem.setGraphic(new ImageView("images/icons/general/informationGreen.png"));
        showHrdcddMsgsInPrjMenuItem.setGraphic(new ImageView("images/icons/general/list.png"));
    }

    private void initPropertyBindings() {
        // Data bindings
        lbl_projectPath.textProperty().bind(projectPathProperty);
        lbl_serverType.textProperty().bind(serverTypeProperty);
        lbl_serverHost.textProperty().bind(serverHostProperty);
        lbl_portNumber.textProperty().bind(portNumberProperty);
        lbl_username.textProperty().bind(usernameProperty);
        lbl_database.textProperty().bind(databaseProperty);
        lbl_emptyMessagesQuantity.textProperty().bind(emptyMessagesQuantityProperty.asString());
        lbl_messageRange.textProperty().bind(messageRangeProperty);
        lbl_totalHardcodedMessages.textProperty().bind(totalHardcodedMessagesProperty.asString());
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
        appRestartMenuItem.disableProperty().bind(appRestartMenuItemDisabledProperty);
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
        availableTagsListMenuItem.disableProperty().bind(availableTagsListMenuItemDisabledProperty);
        aboutAppMenuItem.disableProperty().bind(aboutAppMenuItemDisabledProperty);
        showHrdcddMsgsInPrjMenuItem.disableProperty().bind(showHrdcddMsgsInPrjMenuItemDisabledProperty);
//        scanProjectButton.disableProperty().bind(scanProjectMenuItemDisabledProperty);
//        transferMessagesButton.disableProperty().bind(transferMessagesMenuItemDisabledProperty);
//        cleanMessageRangeButton.disableProperty().bind(cleanMessageRangeMenuItemDisabledProperty);
        fileItemsTableView.disableProperty().bind(Bindings.isEmpty(fileItemsTableViewData));
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
        lbl_messageRangeVisibleProperty.set(messageTransferService != null && messageTransferService.getConfig() != null && messageTransferService.getMessageRange() != null);
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

        cleanMessageRangeMenuItem.setVisible(false); // TODO: DB cleanup is disabled temporally.
//        cleanMessageRangeButton.setVisible(false); // TODO: DB cleanup is disabled temporally.
    }

    private void checkEditability() {
        /**
         * ENABLED = false
         * DISABLED = true
         */

        browseProjectMenuItemDisabledProperty.setValue(false);
        appSettingsMenuItemDisabledProperty.setValue(false);
        appRestartMenuItemDisabledProperty.setValue(false);
        exitAppMenuItemDisabledProperty.setValue(false);

        scanProjectMenuItemDisabledProperty.setValue(messageFinder == null);
        scanDbMenuItemDisabledProperty.setValue(messageTransferService == null || messageTransferService.getConfig() == null || messageTransferService.getConfig().getDbName() == null || messageTransferService.getConfig().getDbName().equals(""));
        generateMessagesMenuItemDisabledProperty.setValue(messageTransferService == null || messageTransferService.getConfig() == null || messageTransferService.getConfig().getDbName() == null || messageTransferService.getConfig().getDbName().equals(""));
        putMessagesMenuItemDisabledProperty.setValue(messageFinder == null || messageTransferService == null);
        transferMessagesMenuItemDisabledProperty.setValue(messageFinder == null || messageTransferService == null);
        cleanMessageRangeMenuItemDisabledProperty.setValue(messageFinder == null || messageTransferService == null);
        showHrdcddMsgsInPrjMenuItemDisabledProperty.setValue(fileItemsTableViewData == null || fileItemsTableViewData.isEmpty());

        connectMSSQLMenuItemDisabledProperty.setValue(false);
        connectOracleMenuItemDisabledProperty.setValue(false);
        connectMySQLMenuItemDisabledProperty.setValue(false);

        configConnectionMenuItemDisabledProperty.setValue(messageTransferService == null || messageTransferService.getConfig() == null);
    }

    private void updateConnectionDetails() {
        if (messageTransferService != null) {
            serverTypeProperty.setValue(messageTransferService.getConfig().getDbServerType().getName());
            serverHostProperty.setValue(messageTransferService.getConfig().getHostName());
            portNumberProperty.setValue(String.valueOf(messageTransferService.getConfig().getPort()));
            usernameProperty.setValue(messageTransferService.getConfig().getUserName());
            databaseProperty.setValue(messageTransferService.getConfig().getDbName());
            messageRangeProperty.setValue(messageTransferService.getMessageRange().toString());
        }

        if (emptyMessagesList != null) {
            emptyMessagesQuantityProperty.setValue(emptyMessagesList.size());
        }

        if (fileItemsTableViewData != null && fileItemsTableViewData.get() != null) {
            int quantity = 0;
            for (FileItem fileItem : fileItemsTableViewData.get()) {
                quantity += fileItem.getTotalMessagesQuantity();
            }
            totalHardcodedMessagesProperty.set(quantity);
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
                    Dialogs.showFileMessagesDialog(Collections.singletonList(rowItem));
                }
            });
            moreActions.setPrefWidth(80);
            moreActions.setMinWidth(moreActions.getPrefWidth());
            moreActions.setMaxWidth(moreActions.getPrefWidth());
//            moreActions.setVisible(false);

            fileItemsTableView.setEditable(true);
            fileItemsTableView.setFocusTraversable(false);
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
                    logger.info("Instantiating MessagesToTransfer list...");
                    prepareMessagesForTransfer();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.projectScanSucceeded"));
                statusBar.reset();
            }

            @Override
            protected void failed() {
                super.failed();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.projectScanFailed"));
                statusBar.reset();
            }
        };

        statusBar.setTask(task);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void scanForNewMessagesInDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.scanDB"), null, ResourceManager.getMessage("label.confirmation.scanDbForNewMessages"))) {
            if (checkTheMessageRangeToBeSet()) {
                Task task = new Task() {
                    List<Message> messages = null;
                    @Override
                    protected Object call() throws Exception {
                        logger.info("DB scan started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.scanDB"));
                        messages = messageTransferService.loadMessagesExcept(getUsedMessages());
//                        initEmptyMessages();
//                        Thread.sleep(3000); // FIXME: UNCOMMENT the 2 lines above and REMOVE this line after testing!!!
                        validate();
                        logger.info("DB scan completed.");
                        return null;
                    }

                    /** {@inheritDoc} */
                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        if (messages != null) {
                            Dialogs.showMessagesDialog(messages);
                        }
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
        Integer msgQuantity = Dialogs.showMsgQuantityPopup("Message Quantity", null, "Please specify the quantity of messages to generate.");
        if (msgQuantity != null) {
            if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"), null, ResourceManager.getMessage("label.confirmation.generateEmptyMessages"))) {
                generateEmptyMessages(msgQuantity);
//                Task task = new Task() {
//                    @Override
//                    protected Object call() throws Exception {
//                        logger.info("Message generation started.");
//                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"));
//                        messageTransferService.generateNewEmptyMessages(messageRange);
//                        initEmptyMessages();
//                        validate();
//                        logger.info("Message generation completed.");
//                        return null;
//                    }
//                };
//
//                Dialogs.showTaskProgressDialog(rootContainer.getScene().getWindow(), task, false);
//
//                Thread thread = new Thread(task);
//                thread.setDaemon(true);
//                thread.start();
            }
        }
    }

    @FXML
    private void putMessagesToFiles(ActionEvent event) throws Exception {
        throw new NullPointerException("Blah!");
//        try {
//            putMessagesToFiles(messageFinder.getMarkedFiles(), messageTransferService.loadMessages());
//        } catch (IOException e) {
//            logger.error(e);
//            Dialogs.showExceptionDialog("", "", e);
//        }
    }

    @FXML
    private void transferMessagesToDB(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("label.menuItem.edit.transferToDB"), null, ResourceManager.getMessage("label.confirmation.transferToDB"))) {
            if (totalHardcodedMessagesProperty.get() > emptyMessagesQuantityProperty.get()
                    && Dialogs.showConfirmPopup("Not enough empty messages in DB.", null, "Not enough of empty posts in the database, would you like to automatically adjust the difference by creating new ones?")) {
                generateEmptyMessages(totalHardcodedMessagesProperty.get() - emptyMessagesQuantityProperty.get());
            }
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    logger.info("Message transfer started.");
                    updateTitle(ResourceManager.getMessage("label.menuItem.edit.transferToDB"));
//                    updateMessage("Preparing messages for transfer..."); // TODO: Should be removed after testing.
//                    logger.info("Preparing messages for transfer..."); // TODO: Should be removed after testing.

                    updateMessage("Backup messages table...");
                    logger.info("Backup messages table started.");
                    messageTransferService.backupMessagesTable();
                    logger.info("Backup messages table completed.");

                    logger.info("DB Name: " + messageTransferService.getConfig().getDbName());
                    prepareMessagesForTransfer(); // TODO: Should be removed after testing.
                    StringBuilder transferredMessagesList = new StringBuilder();
//                    List<Message> messagesToTransfer = getMessagesToTransfer(); // TODO: Should be removed after testing.
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

                @Override
                protected void succeeded() {
                    super.succeeded();
                    try {
                        String confirmTitle = ResourceManager.getMessage("title.dialog.putToFiles");
                        String confirmContent = ResourceManager.getMessage("label.confirmation.putToFilesAfterTransferringToDB");
                        if (Dialogs.showConfirmPopup(confirmTitle, null, confirmContent)) {
                            putMessagesToFiles(messageFinder.getMarkedFiles(), messagesToTransfer);
                        }
                    } catch (IOException e) {
                        logger.error(e);
                    }
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
            if (checkTheMessageRangeToBeSet()) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        logger.info("Message cleaning started.");
                        updateTitle(ResourceManager.getMessage("label.menuItem.edit.removeFromDB"));
                        messageTransferService.removeMessagesExcept(getUsedMessages());
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

    @FXML
    private void backupMessagesTable(ActionEvent event) {
        if (Dialogs.showConfirmPopup(ResourceManager.getMessage("title.dialog.confirm"), null, ResourceManager.getMessage("label.confirmation.backupMessagesTable"))) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    logger.info("Backup messages table started.");
                    updateTitle("Backup messages table...");
                    messageTransferService.backupMessagesTable();
                    logger.info("Backup messages table completed.");
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
    private void restartApp(ActionEvent event) throws Exception {
        String title = "Restarting...";
        String content = "The application is going to restart.\nContinue?";
        ProcessIndicator graphic = new ProcessIndicator("images/icons/process/fs/step_1@2x.png", true);
        if (Dialogs.showConfirmPopup(graphic, title, null, content)) {
            Main.restart();
        }
    }

    @FXML
    private void exitApp(ActionEvent event) {
        logger.info("Application terminated by user.");
        Platform.exit();
    }

    @FXML
    private void connectToMSSQL(ActionEvent event) {
        createEditConnectionConfig(DBServerType.MSSQLServer);
    }

    @FXML
    private void connectToOracle(ActionEvent event) {
        createEditConnectionConfig(DBServerType.ORAServer);
    }

    @FXML
    private void connectToMySQL(ActionEvent event) {
        createEditConnectionConfig(DBServerType.MySQLServer);
    }

    @FXML
    private void configureConnection(ActionEvent event) {
        createEditConnectionConfig(null);
    }

    private void createEditConnectionConfig(DBServerType serverType) {
        try {
            ConnectionConfig config = null;
            if (serverType != null) {
                config = Dialogs.showNewConnectionPopup(serverType, rootContainer.getScene().getWindow());
            } else {
                config = Dialogs.showEditConnectionPopup(messageTransferService.getConfig(), rootContainer.getScene().getWindow());
            }
            if (config != null && config.getIsValid()) {
                logger.info("Connection config: " + config.toString());
                if (MessageTransferService.checkDbConnection(config)) {
                    messageTransferService = new MessageTransferService(config);
                    initEmptyMessages();
                    updateConnectionDetails();
                }
            }
        } catch (SQLException ex) {
            String content = ErrorCode.DB_NO_CONNECTION.getCode() + " - " + ErrorCode.DB_NO_CONNECTION.getDescription();
            Dialogs.showExceptionDialog(null, content, ex);
            logger.error(content, ex);
        }
        validate();
    }

    @FXML
    private void availableTagsList(ActionEvent event) {
        Dialogs.showAvailableTagsDialog(rootContainer.getScene().getWindow());
    }

    @FXML
    private void submitIssue(ActionEvent event) throws Exception {
        Dialogs.showWebBrowser(ResourceManager.getMessage("title.dialog.submitAnIssue"), ResourceManager.getParam("URL.GITHUB.ISSUE.NEW"));

/*
        String url = ResourceManager.getParam("URL.GITHUB.ISSUE.NEW");
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        if (os.contains("win")) {
            // this doesn't support showing urls in the form of "page.html#nameLink"
            rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.contains("mac")) {
            rt.exec( "open " + url);
        } else if (os.contains("nix") || os.contains("nux")) {
            // Do a best guess on unix until we get a platform independent way
            // Build a list of browsers to try, in this order.
            String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera","links","lynx"};

            // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
            StringBuffer cmd = new StringBuffer();
            for (int i = 0; i < browsers.length; i++) {
                cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
            }
            rt.exec(new String[] { "sh", "-c", cmd.toString() });
        } else {
            Dialogs.showWarningPopup("Uoops!", null, "It seems like something went wrong. Please proceed by the link below to submit your issue.\n" + ResourceManager.getParam("URL.GITHUB.ISSUE.NEW"));
        }
*/
    }

    @FXML
    private void aboutApp(ActionEvent event) {
        Dialogs.showAboutAppDialog(rootContainer.getScene().getWindow());
    }

    @FXML
    private void specifyNewMessageRange(ActionEvent event) {
//        messageTransferService.getConfig().setMessagesRange(Dialogs.showRangeDialog("Message Range", "Please define message range."));
        updateConnectionDetails();
    }

    @FXML
    private void showHardcodedMessages() {
        Dialogs.showFileMessagesDialog(fileItemsTableViewData.getValue());
    }

    /* ------------- /Main Menu Actions ------------- */


    /* ------------- Other methods ------------- */

    private void setAllFilesSelected(boolean isSelected) {
        for (FileItem item : fileItemsTableView.getItems()) {
            item.setIsSelected(isSelected);
        }
        validate();
    }

    private void generateEmptyMessages(int quantity) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                logger.info("Message generation started.");
                updateTitle(ResourceManager.getMessage("label.menuItem.edit.generateEmptyMessages"));
                updateMessage(ResourceManager.getMessage("label.gettingLastMsgId"));
                int lastMessageId = messageTransferService.loadLastMessageId();
                updateMessage(ResourceManager.getMessage("label.generatingNewMessages"));
                int from = lastMessageId + 1;
                int to = from + quantity;
                messageTransferService.generateNewEmptyMessages(new IntegerRange(from, to));
                logger.info("Message generation completed.");
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                initEmptyMessages();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.messageGenerationSucceeded"));
                statusBar.reset();
            }

            @Override
            protected void failed() {
                super.failed();
                initEmptyMessages();
                validate();
                updateConnectionDetails();
                logger.info(ResourceManager.getMessage("label.messageGenerationFailed"));
                statusBar.reset();
            }
        };

        statusBar.setTask(task);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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
                    logger.error("Error occurred in putMessagesToFiles method: ", ex);
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
        Set<Integer> usedMessages = new HashSet<>(0);
        try {
            logger.info("Searching for used message ids.");
            if (messageFinder != null) {
                usedMessages = messageFinder.findUsedMessages();
            }
            logger.info("Search completed.");
        } catch (IOException ex) {
            logger.error(ex);
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
                    messagesToTransfer.add(message);
                    emptyMessagesIterator.remove();
                }
            }
        }
    }

//    private List<Message> getMessagesToTransfer() {
//        messagesToTransfer = new ArrayList<>();
//        for (Message message : emptyMessagesList) {
//            if (!message.isEmpty()) {
//                messagesToTransfer.add(message);
//            }
//        }
//        return messagesToTransfer;
//    }

    private void initEmptyMessages() {
        if (!checkTheMessageRangeToBeSet()) {
            Dialogs.showWarningPopup(ResourceManager.getMessage("title.dialog.warning"), null, ResourceManager.getMessage("label.warning.invalidOrUndefinedMessageRange"));
        } else {
            emptyMessagesList = messageTransferService.loadEmptyMessages();
        }
        updateConnectionDetails();
    }

    private boolean checkTheMessageRangeToBeSet() {
        if (messageTransferService.getMessageRange() == null) {
            specifyNewMessageRange(new ActionEvent());
        }
        return messageTransferService.getMessageRange().isValid();
    }

//    private boolean checkDbConnection(ConnectionConfig config) {
//        boolean isReachable = false;
//        try {
//            isReachable = MessageTransferService.checkDbConnection(config);
//        } catch (SQLException ex) {
//            String content = ErrorCode.DB_NO_CONNECTION.getCode() + " - " + ErrorCode.DB_NO_CONNECTION.getDescription();
//            Dialogs.showExceptionDialog(null, content, ex);
//            logger.error(content, ex);
//        }
//        return isReachable;
//    }
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
