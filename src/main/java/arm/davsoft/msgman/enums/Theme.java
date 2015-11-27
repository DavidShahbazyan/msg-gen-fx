package arm.davsoft.msgman.enums;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 11/19/15 <br/>
 * <b>Time:</b> 2:37 PM <br/>
 */
public enum Theme {
    LIGHT(1, "Winter", "light"),
    DARK(2, "Darkness", "dark"),
    ;

    private Integer id;
    private String name;
    private String styleName;

    Theme(Integer id, String name, String styleName) {
        this.id = id;
        this.name = name;
        this.styleName = styleName;
    }

    public Integer getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getStyleName() {
        return this.styleName;
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
