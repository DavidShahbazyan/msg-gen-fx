package com.synisys.msggen;

import com.synisys.msggen.utils.ResourceManager;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/25/15 <br/>
 * <b>Time:</b> 12:25 AM <br/>
 */
public class MainPreloader extends Preloader {
    ProgressBar bar;
    ProgressIndicator ind;
    Stage stage;
    Label applicationName;
    Label creators;

    private Scene createPreloaderScene() {
//        ind = new ProgressIndicator();
//        ind.setPrefSize(5, 5);

        bar = new ProgressBar();
        bar.setPrefHeight(3);

        Reflection reflection = new Reflection();
        reflection.setTopOpacity(0.3);

//        InnerShadow innerShadow = new InnerShadow();
//        innerShadow.setColor(Color.WHITE);

//        Blend blend = new Blend();
//        blend.setMode(BlendMode.MULTIPLY);
//        blend.setBottomInput(reflection);
//        blend.setTopInput(innerShadow);

        applicationName = new Label(ResourceManager.getParam("APPLICATION.NAME") + " " + ResourceManager.getParam("APPLICATION.VERSION"));
        applicationName.setStyle("-fx-font-size: 25px");
        applicationName.setEffect(reflection);

        creators = new Label(ResourceManager.getParam("APPLICATION.CREATORS"));
        creators.setStyle("-fx-font-size: 12px");
//        creators.setEffect(reflection);

        ImageView splash = new ImageView("/images/splashScreen.png");
        AnchorPane p = new AnchorPane(splash, bar, applicationName, creators);
        AnchorPane.setTopAnchor(splash, (double) 0);

        AnchorPane.setRightAnchor(splash, (double) 0);
        AnchorPane.setBottomAnchor(splash, (double) 0);
        AnchorPane.setLeftAnchor(splash, (double) 0);

//        AnchorPane.setTopAnchor(ind, (double) 5);
//        AnchorPane.setRightAnchor(ind, (double) 5);

        AnchorPane.setRightAnchor(bar, (double) 0);
        AnchorPane.setBottomAnchor(bar, (double) 0);
        AnchorPane.setLeftAnchor(bar, (double) 0);

        AnchorPane.setLeftAnchor(applicationName, (double) 20);
        AnchorPane.setTopAnchor(applicationName, (double) 110);

        AnchorPane.setRightAnchor(creators, (double) 20);
        AnchorPane.setBottomAnchor(creators, (double) 20);

        p.setStyle("-fx-background-color: transparent;");
        return new Scene(p);
    }

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.getScene().getStylesheets().add("/css/style.css");
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
