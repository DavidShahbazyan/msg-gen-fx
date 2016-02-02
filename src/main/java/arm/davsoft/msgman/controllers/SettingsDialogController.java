package arm.davsoft.msgman.controllers;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.components.CheckBoxToggleSwitch;
import arm.davsoft.msgman.enums.Tag;
import arm.davsoft.msgman.enums.Theme;
import arm.davsoft.msgman.utils.Dialogs;
import arm.davsoft.msgman.utils.ResourceManager;
import arm.davsoft.msgman.utils.Utils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.*;
import javafx.util.converter.NumberStringConverter;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 11/17/15 <br/>
 * <b>Time:</b> 1:40 AM <br/>
 */
public class SettingsDialogController implements Initializable {
    private final Logger logger = Main.LOGGER;

    private Properties customProps = ResourceManager.getSettings();
    private IntegerProperty workingCopyIdProperty;
    private IntegerProperty messageRangeFromProperty;
    private IntegerProperty messageRangeToProperty;
    private StringProperty messagePatternProperty;
    private StringProperty messageMarkerProperty;
    private BooleanProperty exportLogToFileProperty;
    private SimpleObjectProperty<Theme> uiThemeProperty;
//    private Theme uiTheme;

    @FXML
    private AnchorPane toolBarContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label breadCrumbs;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;


    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.workingCopyIdProperty = new SimpleIntegerProperty(Integer.valueOf(customProps.getProperty("workingCopyId")));
        this.messageRangeFromProperty = new SimpleIntegerProperty(Integer.valueOf(customProps.getProperty("messageRangeFrom")));
        this.messageRangeToProperty = new SimpleIntegerProperty(Integer.valueOf(customProps.getProperty("messageRangeTo")));
        this.messagePatternProperty = new SimpleStringProperty(customProps.getProperty("messagePattern"));
        this.messageMarkerProperty = new SimpleStringProperty(customProps.getProperty("messageMarker"));
        this.exportLogToFileProperty = new SimpleBooleanProperty(Boolean.valueOf(customProps.getProperty("exportLogToFile")));
        this.uiThemeProperty = new SimpleObjectProperty<>(Theme.getThemeById(Integer.valueOf(customProps.getProperty("uiTheme"))));
        this.btnCancel.setCancelButton(true);
        this.btnOk.setDefaultButton(true);

        TreeView<Tab> tabTreeView = new TreeView<>(new TreeItem<>());
        tabTreeView.setShowRoot(false);
        tabTreeView.setFocusTraversable(false);
        tabTreeView.getStyleClass().add("myTree");
        tabTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> changeTabTo(newValue.getValue()));
        List<Tab> tabs = Arrays.asList(getGeneralTab(), getProjectTab(), getLoggingTab());
        for (Tab tab : tabs) {
            tabTreeView.getRoot().getChildren().add(new TreeItem<>(tab));
        }
        toolBarContainer.getChildren().clear();
        toolBarContainer.getChildren().add(tabTreeView);
        AnchorPane.setTopAnchor(tabTreeView, (double) 0);
        AnchorPane.setRightAnchor(tabTreeView, (double) 0);
        AnchorPane.setBottomAnchor(tabTreeView, (double) 0);
        AnchorPane.setLeftAnchor(tabTreeView, (double) 0);

        changeTabTo(getGeneralTab());
    }

    @FXML
    private void saveAction(ActionEvent event) {
        saveSettings();
        closeDialog(event);
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        closeDialog(event);
    }

    private void changeTabTo(Tab tab) {
        this.breadCrumbs.setText(tab.getText());
        this.scrollPane.setContent(tab.getContent());
    }

    private void saveSettings() {
        try {
            customProps.setProperty("workingCopyId", "" + workingCopyIdProperty.get());
            customProps.setProperty("messagePattern", messagePatternProperty.get());
            customProps.setProperty("messageMarker", messageMarkerProperty.get());
            customProps.setProperty("exportLogToFile", "" + exportLogToFileProperty.get());
            customProps.setProperty("uiTheme", "" + uiThemeProperty.get().getId());
            customProps.setProperty("messageRangeFrom", "" + messageRangeFromProperty.get());
            customProps.setProperty("messageRangeTo", "" + messageRangeToProperty.get());
            StringBuilder sb = new StringBuilder();
            for (Tag tag : Tag.values()) {
                if (tag.getIsSelected()) {
                    if (!sb.toString().isEmpty()) {
                        sb.append(',');
                    }
                    sb.append(tag.name());
                }
            }
            customProps.setProperty("supportedTags", sb.toString());
            String path = ResourceManager.CUSTOM_SETTINGS_RESOURCE_FILE;
            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
            }
            FileOutputStream fos = new FileOutputStream(path);
            customProps.store(fos, "User defined settings - DO NOT CHANGE ANYTHING HERE!");
            fos.close();
            Dialogs.showInfoPopup(ResourceManager.getMessage("title.dialog.settings"), ResourceManager.getMessage("label.settingsRestartApp"));
        } catch (Exception ex) {
            Logger.getLogger(getClass()).error("Error occurred in showSettingsDialog method: ", ex);
        }
    }

    private void closeDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private Tab getGeneralTab() {
        Tab tab = new Tab("General", new VBox()) {
            @Override
            public String toString() {
                return getText();
            }
        };
        ((VBox) tab.getContent()).setPadding(new Insets(5));

        ComboBox<Theme> themeComboBox = new ComboBox<>(FXCollections.observableArrayList(Theme.values()).sorted((o1, o2) -> o1.getName().compareTo(o2.getName())));
        themeComboBox.valueProperty().bindBidirectional(uiThemeProperty);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        gridPane.add(new Label(ResourceManager.getMessage("label.theme")), 0, 0);
        gridPane.add(themeComboBox, 1, 0);

        Label lbl_logInfo = new Label(ResourceManager.getMessage("label.changesWillTakeEffectAfterAppRestart"));
        lbl_logInfo.getStyleClass().add("warning");
        gridPane.add(lbl_logInfo, 0, 1, 2, 1);

        TreeView<String> tagTreeView = new TreeView<>(new CheckBoxTreeItem<>(ResourceManager.getMessage("label.supportedTags")));
        tagTreeView.setShowRoot(false);
        tagTreeView.setFocusTraversable(false);
        tagTreeView.setPrefHeight(300);
        tagTreeView.setMinHeight(tagTreeView.getPrefHeight());
        tagTreeView.setMaxHeight(tagTreeView.getPrefHeight());
        tagTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        List<Tag> supportedTags = Utils.getSupportedTags();
        for (Tag tag : Tag.values()) {
            CheckBoxTreeItem<String> tagTreeItem = new CheckBoxTreeItem<>(tag.getName());
            tagTreeItem.selectedProperty().bindBidirectional(tag.isSelectedProperty());
            if (supportedTags.contains(tag)) {
                tagTreeItem.setSelected(true);
            }
            tagTreeView.getRoot().getChildren().add(tagTreeItem);
        }

        Label lbl_supportedTags = new Label(ResourceManager.getMessage("label.supportedTags"));
        gridPane.add(lbl_supportedTags, 0, 2);
        gridPane.add(tagTreeView, 1, 2);

        GridPane.setValignment(lbl_supportedTags, VPos.TOP);

//        for (Node node : gridPane.getChildren()) {
//            GridPane.setValignment(node, VPos.TOP);
//        }

        Region separator = new Region();
        VBox.setVgrow(separator, Priority.ALWAYS);

        ((VBox) tab.getContent()).getChildren().addAll(gridPane);
        return tab;
    }

    private Tab getProjectTab() {
        Tab tab = new Tab("Project", new VBox()) {
            @Override
            public String toString() {
                return getText();
            }
        };
        ((VBox) tab.getContent()).setPadding(new Insets(5));

        Label lblWorkingCopyId = new Label(ResourceManager.getMessage("label.workingCopyId"));
        TextField txtWorkingCopyId = new TextField();
        txtWorkingCopyId.textProperty().bindBidirectional(workingCopyIdProperty, new NumberStringConverter("####"));

        Label lblMessagePatterns = new Label(ResourceManager.getMessage("label.messagePatterns"));
        lblMessagePatterns.getStyleClass().add("bold");

//        Label lblMsgMarkerPattern = new Label(ResourceManager.getMessage("label.find"));
//        TextField txtMsgMarkerPattern = new TextField();
//        txtMsgMarkerPattern.textProperty().bindBidirectional(messageMarkerProperty);

        Label lblMessageRange = new Label(ResourceManager.getMessage("label.messageRange"));
        lblMessageRange.getStyleClass().add("bold");

        Label lblMessageRangeFrom = new Label(ResourceManager.getMessage("label.from"));
        TextField txtMessageRangeFrom = new TextField();
        txtMessageRangeFrom.textProperty().bindBidirectional(messageRangeFromProperty, new NumberStringConverter("######"));

        Label lblMessageRangeTo = new Label(ResourceManager.getMessage("label.to"));
        TextField txtMessageRangeTo = new TextField();
        txtMessageRangeTo.textProperty().bindBidirectional(messageRangeToProperty, new NumberStringConverter("######"));

        Label lblFindMsgPattern = new Label(ResourceManager.getMessage("label.replace"));
        TextField txtFindMsgPattern = new TextField();
        txtFindMsgPattern.textProperty().bindBidirectional(messagePatternProperty);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        gridPane.add(lblWorkingCopyId, 0, 0);
        gridPane.add(txtWorkingCopyId, 1, 0);

        gridPane.add(lblMessagePatterns, 0, 1, 2, 1);

//        gridPane.add(lblMsgMarkerPattern, 0, 2);
//        gridPane.add(txtMsgMarkerPattern, 1, 2);

        gridPane.add(lblFindMsgPattern, 0, 3);
        gridPane.add(txtFindMsgPattern, 1, 3);

        gridPane.add(lblMessageRange, 0, 4, 2, 1);
        gridPane.addRow(5, lblMessageRangeFrom, txtMessageRangeFrom);
        gridPane.addRow(6, lblMessageRangeTo, txtMessageRangeTo);

        Region separator = new Region();
        VBox.setVgrow(separator, Priority.ALWAYS);

        ((VBox) tab.getContent()).getChildren().addAll(gridPane);
        return tab;
    }

    private Tab getLoggingTab() {
        Tab tab = new Tab("Logging", new VBox()) {
            @Override
            public String toString() {
                return getText();
            }
        };
        ((VBox) tab.getContent()).setPadding(new Insets(5));

        CheckBox checkExportLogToFile = new CheckBoxToggleSwitch();
        checkExportLogToFile.selectedProperty().bindBidirectional(exportLogToFileProperty);
        checkExportLogToFile.setFocusTraversable(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        gridPane.add(new Label(ResourceManager.getMessage("label.exportLogToFile")), 0, 0);
        gridPane.add(checkExportLogToFile, 1, 0);

        Label lbl_logInfo = new Label(ResourceManager.getMessage("label.changesWillTakeEffectAfterAppRestart"));
        lbl_logInfo.getStyleClass().add("warning");
        gridPane.add(lbl_logInfo, 0, 1, 2, 1);

        Region separator = new Region();
        VBox.setVgrow(separator, Priority.ALWAYS);

        ((VBox) tab.getContent()).getChildren().addAll(gridPane);
        return tab;
    }

}
