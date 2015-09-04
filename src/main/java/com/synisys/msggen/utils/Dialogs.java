package com.synisys.msggen.utils;

import com.synisys.msggen.domains.IntegerRange;
import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.interfaces.Range;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/5/15 <br/>
 * <b>Time:</b> 10:25 PM <br/>
 */
public final class Dialogs {
    private Dialogs() {}

    public static Window getParentWindow(Node node) {
        return node.getScene().getWindow();
    }

    public static File getUserHomeDir() { return new File(System.getProperty("user.home")); }

    public static ConnectionConfig showConnectionPopup(Window ownerWindow, ConnectionConfig config) {
        ConnectionConfig configClone = config.clone();

        final boolean processCancelled[] = {false};

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerWindow);
        dialog.setTitle(ResourceManager.getMessage("title.dialog.configureConnection"));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);
        vBox.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);

        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setPrefColumns(2);

        Label lblHostName = new Label(ResourceManager.getMessage("label.hostNameIPAddress"));
        TextField txtHostName = new TextField();
        txtHostName.textProperty().bindBidirectional(configClone.getHostNameProperty());

        Label lblPort = new Label(ResourceManager.getMessage("label.port"));
        TextField txtPort = new TextField();
        txtPort.textProperty().bindBidirectional(configClone.getPortProperty(), new NumberStringConverter("####"));

        Label lblSID = new Label(ResourceManager.getMessage("label.serviceNameSID"));
        TextField txtSID = new TextField();
        txtSID.textProperty().bindBidirectional(configClone.getSIDProperty());

        Label lblUserName = new Label(ResourceManager.getMessage("label.userName"));
        TextField txtUserName = new TextField();
        txtUserName.textProperty().bindBidirectional(configClone.getUserNameProperty());

        Label lblPassword = new Label(ResourceManager.getMessage("label.password"));
        PasswordField txtPassword = new PasswordField();
        txtPassword.textProperty().bindBidirectional(configClone.getPasswordProperty());


        tilePane.getChildren().add(lblHostName);
        tilePane.getChildren().add(txtHostName);

        if (configClone.isORAServer() || configClone.isMySQLServer()) {
            tilePane.getChildren().add(lblPort);
            tilePane.getChildren().add(txtPort);
        }

        if (configClone.isORAServer()) {
            tilePane.getChildren().add(lblSID);
            tilePane.getChildren().add(txtSID);
        }

        tilePane.getChildren().add(lblUserName);
        tilePane.getChildren().add(txtUserName);
        tilePane.getChildren().add(lblPassword);
        tilePane.getChildren().add(txtPassword);

        Button okButton = new Button(ResourceManager.getMessage("label.button.ok"));
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> {
            processCancelled[0] = false;
            dialog.close();
        });

        Button cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));
        cancelButton.setOnAction(event -> {
            processCancelled[0] = true;
            dialog.close();
        });

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(cancelButton, okButton);

        vBox.getChildren().addAll(tilePane, buttonBar);

        Scene dialogScene = new Scene(vBox);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.showAndWait();

        if (processCancelled[0]) {
            configClone = null;
        }

        return configClone;
    }

    public static void showInfoPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public static void showWarningPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public static boolean showConfirmPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(header);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.YES;
    }

    public static <T> T showChoicePopup(String title, String header, String content, List<T> choiceList) {
        T retVal = null;
        ChoiceDialog<T> dialog = new ChoiceDialog<>(choiceList.get(0), choiceList);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<T> result = dialog.showAndWait();
        if (!result.equals(Optional.<T>empty())) {
            retVal = result.get();
        }
        return retVal;
    }

    public static void showExceptionDialog(String header, String content, Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ResourceManager.getMessage("title.dialog.error"));
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label(ResourceManager.getMessage("label.errorStackTraceWas"));

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static void showTaskProgressDialog(Window ownerWindow, Task task, boolean showTaskMessage) {
        final Stage dialog = new Stage();
        task.setOnSucceeded(event -> dialog.close());
        task.setOnCancelled(event -> dialog.close());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerWindow);
        dialog.titleProperty().bind(task.titleProperty());
//        dialog.setTitle(ResourceManager.getMessage("title.dialog.processing"));
        dialog.setOnCloseRequest(event -> Logger.getLogger(Dialogs.class).info("Task terminated by user."));

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(ResourceManager.getMessage("label.pleaseWaitWhile"));
        Label taskMessage = new Label();
        taskMessage.textProperty().bind(task.messageProperty());

        Button cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));
        cancelButton.setOnAction(event -> {
            task.cancel();
            Logger.getLogger(Dialogs.class).info("Task terminated by user.");
        });

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().add(cancelButton);

        VBox dialogVBox = new VBox();
        dialogVBox.setFillWidth(true);
        dialogVBox.setSpacing(5);
        dialogVBox.setPadding(new Insets(5));
        dialogVBox.setPrefSize(300, VBox.USE_COMPUTED_SIZE);
        dialogVBox.getChildren().add(label);
        if (showTaskMessage) {
            dialogVBox.getChildren().add(taskMessage);
        }
        dialogVBox.getChildren().add(progressBar);
        dialogVBox.getChildren().add(buttonBar);

        Scene dialogScene = new Scene(dialogVBox);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
    }

    public static Range showRangeDialog(String title, String header) {
        final Range[] retVal = {null};

        Dialog<Range> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        IntegerProperty
                fromFieldProperty = new SimpleIntegerProperty(),
                toFieldProperty = new SimpleIntegerProperty();

//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType generateButtonType = new ButtonType(ResourceManager.getMessage("label.button.generate"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField fromTextField = new TextField();
        fromTextField.textProperty().bindBidirectional(fromFieldProperty, new NumberStringConverter());

        TextField toTextField = new TextField();
        toTextField.textProperty().bindBidirectional(toFieldProperty, new NumberStringConverter());

        grid.add(new Label("From:"), 0, 0);
        grid.add(fromTextField, 1, 0);
        grid.add(new Label("To:"), 0, 1);
        grid.add(toTextField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(generateButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        fromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty() || toTextField.getText().trim().isEmpty());
        });

        toTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(fromTextField.getText().trim().isEmpty() || newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> fromTextField.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                return new IntegerRange(fromFieldProperty.get(), toFieldProperty.get());
            }
            return null;
        });

        Optional<Range> result = dialog.showAndWait();

        result.ifPresent(range -> retVal[0] = range);

        return retVal[0];
    }

    public static void showSettingsDialog(Window ownerWindow) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerWindow);
        dialog.setTitle(ResourceManager.getMessage("title.dialog.settings"));

        IntegerProperty workingCopyIdProperty = new SimpleIntegerProperty(Integer.valueOf(ResourceManager.getSetting("workingCopyId")));
        StringProperty messagePatternProperty = new SimpleStringProperty(ResourceManager.getSetting("message.pattern"));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);
        vBox.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);

        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setPrefColumns(2);

        Label lblWorkingCopyId = new Label(ResourceManager.getMessage("label.workingCopyId"));
        TextField txtWorkingCopyId = new TextField();
        txtWorkingCopyId.textProperty().bindBidirectional(workingCopyIdProperty, new NumberStringConverter("####"));

        Label lblMessagePattern = new Label(ResourceManager.getMessage("label.messagePattern"));
        TextField txtMessagePattern = new TextField();
        txtMessagePattern.textProperty().bindBidirectional(messagePatternProperty);

        tilePane.getChildren().add(lblWorkingCopyId);
        tilePane.getChildren().add(txtWorkingCopyId);

        tilePane.getChildren().add(lblMessagePattern);
        tilePane.getChildren().add(txtMessagePattern);

        Button okButton = new Button(ResourceManager.getMessage("label.button.ok"));
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> {
            try {
                PropertiesConfiguration config = new PropertiesConfiguration("properties/settings.properties");
                config.setProperty("workingCopyId", "" + workingCopyIdProperty.get());
                config.setProperty("message.pattern", messagePatternProperty.get());
                config.save();
            } catch (Exception ex) {
                Logger.getLogger(Dialogs.class).error("Error occurred in showSettingsDialog method: ", ex);
            }
            dialog.close();
        });

        Button cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));
        cancelButton.setOnAction(event -> {
            dialog.close();
            Logger.getLogger(Dialogs.class).info("Terminated by user.");
        });

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(cancelButton, okButton);

        vBox.getChildren().addAll(tilePane, buttonBar);

        Scene dialogScene = new Scene(vBox);
        dialog.setScene(dialogScene);
        dialog.getScene().getStylesheets().add("css/style.css");
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    public static void showAboutAppDialog(Window ownerWindow) {
        String title = ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.VERSION");

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNIFIED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerWindow);
        stage.setResizable(false);
        stage.setTitle(ResourceManager.getMessage("title.dialog.about") + " " + title);

        Reflection reflection = new Reflection();

        VBox root = new VBox();

        HBox hBox = new HBox(10);

        ImageView appLogo = new ImageView("/images/mg_logo.png");
        appLogo.setEffect(reflection);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5));

        Label appTitle = new Label(title);

        TextArea textArea = new TextArea(ResourceManager.getMessage("label.aboutTheApp"));
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);
        textArea.setWrapText(true);

        vBox.getChildren().addAll(appTitle, textArea);

        hBox.getChildren().addAll(appLogo, vBox);

        Button okButton = new Button(ResourceManager.getMessage("label.button.ok"));
        okButton.setOnAction(event -> stage.close());

        root.getChildren().addAll(hBox, okButton);

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("css/style.css");

        stage.setScene(scene);
        stage.showAndWait();
    }
}
