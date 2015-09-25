package arm.davsoft.msggen.implementations;

import arm.davsoft.msggen.interfaces.SQLQuery;
import arm.davsoft.msggen.utils.ResourceManager;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:24 PM <br/>
 */
public final class IDM_7_MSSQLQueryImpl implements SQLQuery {
    private static final IDM_7_MSSQLQueryImpl INSTANCE = new IDM_7_MSSQLQueryImpl();
    private static final String workingCopyId = ResourceManager.getSetting("workingCopyId");

    private static final String REMOVE_UNUSED_MESSAGES = "UPDATE DE_Messages SET MessageText = MessageID, IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID NOT IN (@@MessageIds) AND MessageID >= @@From and MessageID <= @@To";
    private static final String LOAD_UNUSED_MESSAGE_IDS = "SELECT DISTINCT MessageID FROM DE_Messages WHERE MessageID >= @@From AND MessageID <= @@To AND (CAST(MessageID AS VARCHAR(10)) = MessageText OR MessageText IS NULL OR MessageText = '')";
    private static final String UPDATE_MESSAGE = "UPDATE DE_Messages SET MessageText = N'@@Text', IsGlobal = 0, WorkingCopyID = " + workingCopyId + " WHERE MessageID = @@MessageId AND WorkingCopyID = " + workingCopyId;
    private static final String LOAD_MESSAGES = "SELECT MessageID, MessageText FROM DE_Messages WHERE CAST(MessageID AS VARCHAR(10)) != MessageText AND MessageID >= @@From AND MessageID <= @@To AND LanguageID = 1 AND WorkingCopyID = " + workingCopyId;
    private static final String IS_MESSAGE_EXIST = "SELECT MessageText FROM DE_Messages WHERE MessageText = N'@@Text'";
    private static final String IS_PROCEDURE_EXISTS = "IF EXISTS (SELECT name FROM sys.objects WHERE name = N'@@ProcName') BEGIN SELECT 'true' END ELSE BEGIN SELECT 'false' END";
    private static final String CREATE_EMPTY_MESSAGES = "EXEC dbo.up_DE_I_Message @WorkingCopyID = " + workingCopyId + ", @RangeStart = @@RangeStart, @RangeEnd = @@RangeEnd , @RangeMinValue = 110001";

    private IDM_7_MSSQLQueryImpl() {}

    public static IDM_7_MSSQLQueryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public String getRemoveUnusedMessages() {
        return REMOVE_UNUSED_MESSAGES;
    }

    @Override
    public String getLoadUnusedMessageIds() {
        return LOAD_UNUSED_MESSAGE_IDS;
    }

    @Override
    public String getUpdateMessage() {
        return UPDATE_MESSAGE;
    }

    @Override
    public String getLoadMessages() {
        return LOAD_MESSAGES;
    }

    @Override
    public String getIsMessageExist() {
        return IS_MESSAGE_EXIST;
    }

    @Override
    public String getIsProcedureExists() {
        return IS_PROCEDURE_EXISTS;
    }

    @Override
    public String getCreateEmptyMessages() {
        return CREATE_EMPTY_MESSAGES;
    }
}
