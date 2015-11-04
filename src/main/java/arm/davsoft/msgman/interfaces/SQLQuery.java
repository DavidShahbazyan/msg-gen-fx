package arm.davsoft.msgman.interfaces;

import java.util.Date;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:23 PM <br/>
 */
public interface SQLQuery {
    String tableSuffix = "_" + new Date().getTime();

    String getBackupMessagesTable();

    String getRemoveMessagesExcept();

    String getRemoveMessages();

    String getLoadUnusedMessageIds();

    String getUpdateMessage();

    String getLoadMessages();

    String getLoadMessagesExcept();

    String getIsMessageExist();

    String getIsProcedureExists();

    String getCreateEmptyMessages();
}
