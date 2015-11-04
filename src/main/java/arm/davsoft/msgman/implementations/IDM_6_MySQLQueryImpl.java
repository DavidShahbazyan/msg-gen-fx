package arm.davsoft.msgman.implementations;

import arm.davsoft.msgman.interfaces.SQLQuery;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:24 PM <br/>
 */
public final class IDM_6_MySQLQueryImpl implements SQLQuery {
    private static final IDM_6_MySQLQueryImpl INSTANCE = new IDM_6_MySQLQueryImpl();

    private static final String BACKUP_MESSAGES_TABLE = "SELECT * INTO dbo.kb_am_Message" + tableSuffix + " FROM kb_am_Message";
    private static final String REMOVE_MESSAGES = "UPDATE kb_am_Message SET sText = iMessageID, sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = NULL, iCategoryID = NULL, bIsGlobal = NULL WHERE iMessageID IN (@@MessageIds) AND iMessageID >= @@RangeStart and iMessageID <= @@RangeEnd";
    private static final String REMOVE_MESSAGES_EXCEPT = "UPDATE kb_am_Message SET sText = iMessageID, sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = NULL, iCategoryID = NULL, bIsGlobal = NULL WHERE iMessageID NOT IN (@@MessageIds) AND iMessageID >= @@RangeStart and iMessageID <= @@RangeEnd";
//    private static final String REMOVE_UNUSED_MESSAGES = "UPDATE kb_am_Message SET sText = iMessageID, sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = NULL, iCategoryID = NULL, bIsGlobal = NULL WHERE iMessageID NOT IN (@@MessageIds) AND iMessageID >= @@RangeStart and iMessageID <= @@RangeEnd";
    private static final String LOAD_UNUSED_MESSAGE_IDS = "SELECT DISTINCT iMessageID FROM kb_am_Message WHERE iMessageID >= @@RangeStart AND iMessageID <= @@RangeEnd AND (CAST(iMessageID AS CHAR(10)) = sText OR sText IS NULL OR sText = '')";
    private static final String UPDATE_MESSAGE = "UPDATE kb_am_Message SET sText = N'@@Text', sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = 7, iCategoryID = 5, bIsGlobal = 0 WHERE iMessageID = @@MessageId";
    private static final String LOAD_MESSAGES = "SELECT iMessageID, sText FROM kb_am_Message WHERE CAST(iMessageID AS CHAR(10)) != sText AND iMessageID >= @@RangeStart AND iMessageID <= @@RangeEnd AND iLanguageID = 1";
    private static final String LOAD_MESSAGES_EXCEPT = "SELECT iMessageID, sText FROM kb_am_Message WHERE CAST(iMessageID AS CHAR(10)) != sText AND iMessageID NOT IN (@@MessageIds) AND iMessageID >= @@RangeStart AND iMessageID <= @@RangeEnd AND iLanguageID = 1";
    private static final String IS_MESSAGE_EXIST = "SELECT sText FROM kb_am_Message WHERE sText = N'@@Text'";
    private static final String IS_PROCEDURE_EXISTS = "IF EXISTS (SELECT name FROM sys.objects WHERE name = N'@@ProcName') BEGIN SELECT 'true' END ELSE BEGIN SELECT 'false' END";
    private static final String CREATE_EMPTY_MESSAGES = "CALL DE_InsertMissedMessages (@@RangeStart, @@RangeEnd)";

    private IDM_6_MySQLQueryImpl() {}

    public static IDM_6_MySQLQueryImpl getInstance() {
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
