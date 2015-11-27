package arm.davsoft.msgman.enums;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/4/15 <br/>
 * <b>Time:</b> 7:00 PM <br/>
 */
public enum ErrorCodes {
    LOG4J_PROP_MISSING("4041", "Log4j property file not found."),
    UNCAUGHT_EXCEPTION("5001", "Uncaught exception occurred."),
    ;

    private String code;
    private String description;

    ErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }
    public String getDescription() {
        return this.description;
    }
}
