package arm.davsoft.msgman.implementations;

import arm.davsoft.msgman.interfaces.SQLQuery;
import arm.davsoft.msgman.utils.ResourceManager;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:24 PM <br/>
 */
public final class IDM_7_MySQLQueryImpl implements SQLQuery {
    private static final IDM_7_MySQLQueryImpl INSTANCE = new IDM_7_MySQLQueryImpl();
    private static final String workingCopyId = ResourceManager.getSetting("workingCopyId");

    private static final String BACKUP_MESSAGES_TABLE = "SELECT * INTO dbo.DE_Messages" + tableSuffix + " FROM DE_Messages";
    private static final String REMOVE_MESSAGES = "UPDATE DE_Messages SET MessageText = MessageID, IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID IN (?) AND MessageID >= ? and MessageID <= ?";
    private static final String REMOVE_MESSAGES_EXCEPT = "UPDATE DE_Messages SET MessageText = MessageID, IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID NOT IN (?) AND MessageID >= ? and MessageID <= ?";
    //    private static final String REMOVE_UNUSED_MESSAGES = "UPDATE DE_Messages SET MessageText = MessageID, IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID NOT IN (@@MessageIds) AND MessageID >= @@RangeStart and MessageID <= @@RangeEnd";
    private static final String LOAD_UNUSED_MESSAGE_IDS = "SELECT DISTINCT MessageID FROM DE_Messages WHERE MessageID >= ? AND MessageID <= ? AND (CAST(MessageID AS CHAR(10)) = MessageText OR MessageText IS NULL OR MessageText = '')";
    private static final String UPDATE_MESSAGE = "UPDATE DE_Messages SET MessageText = ?, IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID = ? AND WorkingCopyID = " + workingCopyId;
//    private static final String LOAD_MESSAGES = "SELECT MessageID, MessageText FROM DE_Messages WHERE CAST(MessageID AS CHAR(10)) != MessageText AND MessageID >= ? AND MessageID <= ? AND LanguageID = 1 AND WorkingCopyID = " + workingCopyId;
    private static final String LOAD_MESSAGES = "SELECT MessageID, MessageText FROM DE_Messages WHERE MessageID >= ? AND MessageID <= ? AND CAST(MessageID AS CHAR(10)) != MessageText AND WorkingCopyID = " + workingCopyId + " GROUP BY MessageID, MessageText";
    private static final String LOAD_MESSAGES_EXCEPT = "SELECT MessageID, MessageText FROM DE_Messages WHERE MessageID >= ? AND MessageID <= ? AND MessageID NOT IN (?) AND CAST(MessageID AS CHAR(10)) != MessageText AND WorkingCopyID = " + workingCopyId + " GROUP BY MessageID, MessageText";
    private static final String IS_MESSAGE_EXIST = "SELECT MessageText FROM DE_Messages WHERE MessageText = N'@@Text'";
    private static final String IS_PROCEDURE_EXISTS = "IF EXISTS (SELECT name FROM sys.objects WHERE name = N'@@ProcName') BEGIN SELECT 'true' END ELSE BEGIN SELECT 'false' END";
    private static final String CREATE_EMPTY_MESSAGES = "EXEC dbo.up_DE_I_Message @WorkingCopyID = " + workingCopyId + ", @RangeStart = ?, @RangeEnd = ?, @RangeMinValue = 110001";

    private IDM_7_MySQLQueryImpl() {}

    public static IDM_7_MySQLQueryImpl getInstance() {
        return INSTANCE;
    }

    @Override public String getBackupMessagesTable() { return BACKUP_MESSAGES_TABLE; }

    @Override public String getRemoveMessages() { return REMOVE_MESSAGES; }

    @Override public String getRemoveMessagesExcept() { return REMOVE_MESSAGES_EXCEPT; }

    @Override public String getLoadUnusedMessageIds() { return LOAD_UNUSED_MESSAGE_IDS; }

    @Override public String getUpdateMessage() { return UPDATE_MESSAGE; }

    @Override public String getLoadMessages() { return LOAD_MESSAGES; }

    @Override public String getLoadMessagesExcept() { return LOAD_MESSAGES_EXCEPT; }

    @Override public String getIsMessageExist() { return IS_MESSAGE_EXIST; }

    @Override public String getIsProcedureExists() { return IS_PROCEDURE_EXISTS; }

    @Override public String getCreateEmptyMessages() { return CREATE_EMPTY_MESSAGES; }
}
