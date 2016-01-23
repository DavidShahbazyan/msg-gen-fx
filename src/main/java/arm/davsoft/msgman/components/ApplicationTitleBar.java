package arm.davsoft.msgman.components;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 10/27/15 <br/>
 * <b>Time:</b> 10:14 AM <br/>
 */
public class ApplicationTitleBar extends ToolBar {
    private Stage stage;
    private WindowButtons windowButtons;
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;

    /**
     * Creates an HBox layout with spacing = 0.
     */
    public ApplicationTitleBar(final Stage stage, String title) {
        this.stage = stage;
        this.windowButtons = new WindowButtons(stage);

        setId("mainToolBar");

        ImageView appTitle = new ImageView("/images/appLogo_light.png");
        appTitle.setFitHeight(20);
        appTitle.setFitWidth(appTitle.getFitHeight() * 1.320652174);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("titleText");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        getItems().addAll(appTitle, spacer, titleLabel, spacer1, this.windowButtons);
        initEvents();
    }

    private void initEvents() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    windowButtons.toggleMaximized();
                }
            }
        });
        // add window dragging
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseDragOffsetX = event.getSceneX();
                mouseDragOffsetY = event.getSceneY();
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!windowButtons.isMaximized()) {
                    stage.setX(event.getScreenX() - mouseDragOffsetX);
                    stage.setY(event.getScreenY() - mouseDragOffsetY);
                }
            }
        });
    }

    public WindowButtons getWindowButtons() {
        return windowButtons;
    }
}
