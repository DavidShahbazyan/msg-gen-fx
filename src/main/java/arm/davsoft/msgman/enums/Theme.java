package arm.davsoft.msgman.enums;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 11/19/15 <br/>
 * <b>Time:</b> 2:37 PM <br/>
 */
public enum Theme {
    LIGHT(1, "Light", "css/themes/light.css"),
    WINTER(2, "Winter", "css/themes/winter.css"),
    DARKNESS(3, "Darkness", "css/themes/darkness.css"),
    ;

    private Integer id;
    private String name;
    private String stylePath;

    Theme(Integer id, String name, String stylePath) {
        this.id = id;
        this.name = name;
        this.stylePath = stylePath;
    }

    public Integer getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getStylePath() {
        return this.stylePath;
    }



    public static Theme getThemeById(Integer themeId) {
        for (Theme theme : values()) {
            if (theme.getId().equals(themeId)) {
                return theme;
            }
        }
        return LIGHT;
    }

    @Override
    public String toString() {
        return getName();
    }
}
