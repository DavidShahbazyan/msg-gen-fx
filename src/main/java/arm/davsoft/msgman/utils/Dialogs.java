package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.domains.FileItem;
import arm.davsoft.msgman.domains.IntegerRange;
import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.enums.IDMVersion;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.utils.dialogs.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
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
        ConnectionConfigDialog dialog = new ConnectionConfigDialog(config, ownerWindow);
        return dialog.getConfig();
    }

    public static void showInfoPopup(String title, String content) {
        showInfoPopup(title, null, content);
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

    public static IDMVersion showIDMVersionPopup() {
        String title = ResourceManager.getMessage("title.dialog.idmVersion");
        String header = ResourceManager.getMessage("label.pleaseChooseIdmVersion");
        String content = ResourceManager.getMessage("label.idmVersion");
        return showChoicePopup(title, header, content, Arrays.asList(IDMVersion.values()));
    }

    public static <T> T showSchemaNamesPopup(List<T> choiceList) {
        String title = ResourceManager.getMessage("title.dialog.schemaName");
        String header = ResourceManager.getMessage("label.pleaseChooseSchemaName");
        String content = ResourceManager.getMessage("label.schemaName");
        return showChoicePopup(title, header, content, choiceList);
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

    public static void showErrorDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ResourceManager.getMessage("title.dialog.error"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showExceptionDialog(String header, String content, Throwable throwable) {
        ExceptionDialog.create(header, content, throwable).showAndWait();
    }

    public static void showTaskProgressDialog(Window ownerWindow, Task task, boolean showTaskMessage) {
        final Stage dialog = new Stage();
        task.setOnSucceeded(event -> dialog.close());
        task.setOnCancelled(event -> dialog.close());
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerWindow);
        dialog.titleProperty().bind(task.titleProperty());
//        dialog.setTitle(ResourceManager.getMessage("title.dialog.processing"));
        dialog.setOnCloseRequest(event -> Logger.getLogger(Dialogs.class).info(ResourceManager.getMessage("notification.task.terminatedByUser")));

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("dark");

        Label label = new Label(ResourceManager.getMessage("label.pleaseWaitWhile"));
        Label taskMessage = new Label();
        taskMessage.textProperty().bind(task.messageProperty());

        Button cancelButton = new Button(ResourceManager.getMessage("label.button.cancel"));
        cancelButton.setOnAction(event -> {
            task.cancel();
            Logger.getLogger(Dialogs.class).info(ResourceManager.getMessage("notification.task.terminatedByUser"));
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
        dialogScene.getStylesheets().add(ResourceManager.getUIThemeStyle());
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
        ButtonType generateButtonType = new ButtonType(ResourceManager.getMessage("label.button.ok"), ButtonBar.ButtonData.OK_DONE);
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
            loginButton.setDisable(newValue.trim().isEmpty() || toTextField.getText().trim().isEmpty() || Integer.valueOf(newValue.trim()).compareTo(Integer.valueOf(toTextField.getText().trim())) > 0);
        });

        toTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(fromTextField.getText().trim().isEmpty() || newValue.trim().isEmpty() || Integer.valueOf(fromTextField.getText().trim()).compareTo(Integer.valueOf(newValue.trim())) > 0);
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

    public static void showMessagesDialog(List<Message> messages) {
        MessagesDialog.create(messages).show();
    }

    public static void showFileMessagesDialog(List<FileItem> fileItems) {
        FileMessagesDialog.create(fileItems).show();
    }

    public static void showSettingsDialog(Window ownerWindow) {
        SettingsDialog.create(ownerWindow).show();
    }

    public static void showAboutAppDialog(Window ownerWindow) {
        AboutAppDialog.create(ownerWindow).show();
    }
}
