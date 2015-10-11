package arm.davsoft.msgman.utils;

import javafx.scene.image.Image;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/10/15 <br/>
 * <b>Time:</b> 1:15 AM <br/>
 */
public final class ResourceManager {
    private static final String DELIMITER = " ";
    private static final String MESSAGES_RESOURCE_FILE = "properties/messages.properties";
    private static final String PARAMS_RESOURCE_FILE = "properties/params.properties";
    private static final String DEFAULT_SETTINGS_RESOURCE_FILE = "properties/settings.properties";
    private static final String CUSTOM_SETTINGS_RESOURCE_FILE = "./settings.properties";

//    private static final ResourceBundle messagesResource = ResourceBundle.getBundle(MESSAGES_RESOURCE_FILE);
//    private static final ResourceBundle paramsResource = ResourceBundle.getBundle(PARAMS_RESOURCE_FILE);
//    private static final ResourceBundle settingsResource = ResourceBundle.getBundle(SETTINGS_RESOURCE_FILE);
    private static PropertiesConfiguration messagesResourceConfig = null;
    private static PropertiesConfiguration paramsResourceConfig = null;
    private static PropertiesConfiguration settingsResourceConfig = null;

    static {
        try {
            messagesResourceConfig = new PropertiesConfiguration(MESSAGES_RESOURCE_FILE);
            messagesResourceConfig.setEncoding("UTF-8");

            paramsResourceConfig = new PropertiesConfiguration(PARAMS_RESOURCE_FILE);

            String settingsResourceFile = DEFAULT_SETTINGS_RESOURCE_FILE;
            if (Files.exists(Paths.get(CUSTOM_SETTINGS_RESOURCE_FILE)) && !Files.isDirectory(Paths.get(CUSTOM_SETTINGS_RESOURCE_FILE))) {
                settingsResourceFile = CUSTOM_SETTINGS_RESOURCE_FILE;
            }
            settingsResourceConfig = new PropertiesConfiguration(settingsResourceFile);
            settingsResourceConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException ex) {
            Logger.getLogger(ResourceManager.class).error(ex);
        }
    }

    private ResourceManager() {}

    public static ResourceBundle getBundle(String baseName) {
        return ResourceBundle.getBundle(baseName);
    }

    public static List<Object> getParamList(String paramId) {
        return paramsResourceConfig.getList(paramId);
    }

    public static String getMessage(String... messageIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageIds.length; i++) {
            stringBuilder.append(messagesResourceConfig.getString(messageIds[i]));
            if (i != messageIds.length - 1) {
                stringBuilder.append(DELIMITER);
            }
        }
        return stringBuilder.toString();
    }

    public static String getParam(String paramName) { return paramsResourceConfig.getString(paramName); }
    public static String getSetting(String settingName) { return settingsResourceConfig.getString(settingName); }
    public static String getUIThemeStyle() { return "css/" + getSetting("uiTheme") + ".css"; }
    public static Image getAppLogo() { return new Image("images/appLogo.png"); }
    public static Image getDavsoftLogo() { return new Image("images/davsoftLogo.png"); }
    public static URL getScene(String sceneName) { return ResourceManager.class.getResource("/screens/" + sceneName); }
}
