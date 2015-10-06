package arm.davsoft.msgman.interfaces;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:23 PM <br/>
 */
public interface SQLQuery {
    String getRemoveUnusedMessages();

    String getLoadUnusedMessageIds();

    String getUpdateMessage();

    String getLoadMessages();

    String getIsMessageExist();

    String getIsProcedureExists();

    String getCreateEmptyMessages();
}
