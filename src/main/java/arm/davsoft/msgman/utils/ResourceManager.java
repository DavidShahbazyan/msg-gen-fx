package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.domains.IntegerRange;
import arm.davsoft.msgman.enums.Theme;
import arm.davsoft.msgman.interfaces.Range;
import javafx.scene.image.Image;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
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
    private static final String DEFAULT_SETTINGS_RESOURCE_FILE = "/properties/settings.properties";
    public static final String CUSTOM_SETTINGS_RESOURCE_FILE = "./.settings";

//    private static final ResourceBundle messagesResource = ResourceBundle.getBundle(MESSAGES_RESOURCE_FILE);
//    private static final ResourceBundle paramsResource = ResourceBundle.getBundle(PARAMS_RESOURCE_FILE);
//    private static final ResourceBundle settingsResource = ResourceBundle.getBundle(SETTINGS_RESOURCE_FILE);
    private static PropertiesConfiguration messagesResourceConfig = null;
    private static PropertiesConfiguration paramsResourceConfig = null;
//    private static PropertiesConfiguration settingsResourceConfig = null;

    private static Properties defaultAppProps = null;
    private static Properties customAppProps = null;

    static {
        try {
            messagesResourceConfig = new PropertiesConfiguration(MESSAGES_RESOURCE_FILE);
            messagesResourceConfig.setEncoding("UTF-8");

            paramsResourceConfig = new PropertiesConfiguration(PARAMS_RESOURCE_FILE);

//            String settingsResourceFile = DEFAULT_SETTINGS_RESOURCE_FILE;
//            if (Files.exists(Paths.get(CUSTOM_SETTINGS_RESOURCE_FILE)) && !Files.isDirectory(Paths.get(CUSTOM_SETTINGS_RESOURCE_FILE))) {
//                settingsResourceFile = CUSTOM_SETTINGS_RESOURCE_FILE;
//            }
//            settingsResourceConfig = new PropertiesConfiguration(settingsResourceFile);
//            settingsResourceConfig.setReloadingStrategy(new FileChangedReloadingStrategy());

            defaultAppProps = new Properties();
            InputStream dIS = ResourceManager.class.getResourceAsStream(DEFAULT_SETTINGS_RESOURCE_FILE);
            defaultAppProps.load(dIS);
            dIS.close();

            customAppProps = new Properties(defaultAppProps);
            if (Files.exists(Paths.get(CUSTOM_SETTINGS_RESOURCE_FILE))) {
                InputStream cIS = new FileInputStream(CUSTOM_SETTINGS_RESOURCE_FILE);
                customAppProps.load(cIS);
                cIS.close();
            }
        } catch (Exception ex) {
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
//    public static String getSetting(String settingName) { return settingsResourceConfig.getString(settingName); }
    public static String getSetting(String settingName) { return customAppProps.getProperty(settingName); }
    public static Properties getSettings() { return customAppProps; }
    public static String getUIThemeStyle() { return "css/" + Theme.getThemeById(Integer.valueOf(getSetting("uiTheme"))).getStyleName() + ".css"; }
    public static Image getAppLogoDark() { return new Image("images/appLogo_dark.png"); }
    public static Image getAppLogoLight() { return new Image("images/appLogo_light.png"); }
    public static Image getDavSoftLogo() { return new Image("images/davsoftLogo.png"); }
    public static URL getScene(String sceneName) { return ResourceManager.class.getResource("/screens/" + sceneName); }
    public static Range getProjectMessageRange() { return new IntegerRange(Integer.parseInt(getSetting("messageRangeFrom")), Integer.parseInt(getSetting("messageRangeTo"))); }

}
