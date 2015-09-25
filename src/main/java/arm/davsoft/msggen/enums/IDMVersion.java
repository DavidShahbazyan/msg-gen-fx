package arm.davsoft.msggen.enums;

import arm.davsoft.msggen.utils.ResourceManager;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 2:27 PM <br/>
 */
public enum IDMVersion {
    IDM6(1, ResourceManager.getMessage("label.idm6.version.name")),
    IDM7(2, ResourceManager.getMessage("label.idm7.version.name"));

    private Integer id;
    private String title;

    IDMVersion(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
