package arm.davsoft.msgman.enums;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/4/15 <br/>
 * <b>Time:</b> 7:00 PM <br/>
 */
public enum ErrorCodes {
    LOG4J_PROP_MISSING("Error: 111."),
    ;

    private String errorMessage;

    ErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
