package arm.davsoft.msgman;

import arm.davsoft.msgman.enums.ErrorCode;
import arm.davsoft.msgman.utils.AppSpecUncaughtExceptionHandler;
import arm.davsoft.msgman.utils.FXMLFactory;
import arm.davsoft.msgman.utils.ResourceManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/20/15 <br/>
 * <b>Time:</b> 1:46 AM <br/>
 */
public class Main extends Application {
    public static final Logger LOGGER = Logger.getLogger(Main.class);
    private static Stage primaryStage;

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new AppSpecUncaughtExceptionHandler());
        try {
            launch(args);
            LOGGER.info("---------------------- Ending the Application ----------------------");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void init() {
        try {
            setUserAgentStylesheet(ResourceManager.getUIThemeStyle());
            Thread.sleep(3000);
            initLogger();
//            LOGGER.info("Application init completed!");
            LOGGER.info("--------------------- Starting the Application ---------------------");
        } catch (Exception ex) {
            LOGGER.error("Error occurred in main init method: ", ex);
            Platform.exit();
        }
    }

    private void initLogger() throws IOException {
        String loggingFileName = "logs/messageManager-" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + ".log";
        Properties props = new Properties();
        InputStream input = getClass().getResourceAsStream("/properties/log4j.properties");
        if (input == null) {
            throw new FileNotFoundException(ErrorCode.LOG4J_PROP_MISSING.getCode());
        }
        props.load(input);
        if (Boolean.valueOf(ResourceManager.getSetting("exportLogToFile"))) {
            props.setProperty("log4j.appender.fileAppender.File", loggingFileName);
        }
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
//        LOGGER.info("Logging has been successfully initialized.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Thread.currentThread().setUncaughtExceptionHandler(new AppSpecUncaughtExceptionHandler());

//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setResources(ResourceManager.getBundle("properties/messages"));
//        fxmlLoader.setResources(ResourceBundle.getBundle("properties/messages", Locale.ENGLISH));
//        fxmlLoader.setResources(ResourceBundle.getBundle("properties/messages", Locale.CHINESE));
//        Parent root = fxmlLoader.load(this.getClass().getResource("/screens/mainScreen.fxml").openStream());

        primaryStage.setTitle(ResourceManager.getParam("APPLICATION.NAME"));
        primaryStage.getIcons().add(ResourceManager.getAppLogoDark());
        primaryStage.setScene(new Scene(FXMLFactory.getFXMLParent("mainScreen.fxml"), 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setOnCloseRequest(event -> {
            LOGGER.info("Application terminated by user.");
            Platform.exit();
        });
        primaryStage.setFullScreenExitHint("");

//        primaryStage.getScene().getStylesheets().add(ResourceManager.getUIThemeStyle());
        primaryStage.getScene().setFill(Color.web("DDF", 0.75));
        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.requestFocus();
    }

    public static void restart() throws Exception {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		    /* is it a jar file? */
        if (!currentJar.getName().endsWith(".jar")) return;

		    /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();

        LOGGER.info("Application is restarting.");
        Platform.exit();
    }

    private void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
