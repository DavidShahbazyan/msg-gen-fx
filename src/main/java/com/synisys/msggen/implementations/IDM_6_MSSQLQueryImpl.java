package com.synisys.msggen.implementations;

import com.synisys.msggen.interfaces.SQLQuery;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 6:24 PM <br/>
 */
public final class IDM_6_MSSQLQueryImpl implements SQLQuery {
    private static final IDM_6_MSSQLQueryImpl INSTANCE = new IDM_6_MSSQLQueryImpl();

    private static final String REMOVE_UNUSED_MESSAGES = "UPDATE kb_am_Message SET sText = iMessageID, sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = NULL, iCategoryID = NULL, bIsGlobal = NULL WHERE iMessageID NOT IN (@@MessageIds) AND iMessageID >= @@From and iMessageID <= @@To";
    private static final String LOAD_UNUSED_MESSAGE_IDS = "SELECT DISTINCT iMessageID FROM kb_am_Message WHERE iMessageID >= @@From AND iMessageID <= @@To AND (CAST(iMessageID AS VARCHAR(10)) = sText OR sText IS NULL OR sText = '')";
    private static final String UPDATE_MESSAGE = "UPDATE kb_am_Message SET sText = N'@@Text', sComment = NULL, sDescription = 'Online DE', sUserName = NULL, iModuleID = 7, iCategoryID = 5, bIsGlobal = 0 WHERE iMessageID = @@MessageId";
    private static final String LOAD_MESSAGES = "SELECT iMessageID, sText FROM kb_am_Message WHERE CAST(iMessageID AS VARCHAR(10)) != sText and iMessageID >= @@From and iMessageID <= @@To and iLanguageID = 1";
    private static final String IS_MESSAGE_EXIST = "SELECT sText FROM kb_am_Message WHERE sText = N'@@Text'";
    private static final String IS_PROCEDURE_EXISTS = "IF EXISTS (SELECT name FROM sys.objects WHERE name = N'@@ProcName') BEGIN SELECT 'true' END ELSE BEGIN SELECT 'false' END";
    private static final String CREATE_EMPTY_MESSAGES = "EXEC DE_InsertMissedMessages @RangeStart = @@RangeStart, @RangeEnd = @@RangeEnd";

    private IDM_6_MSSQLQueryImpl() {}

    public static IDM_6_MSSQLQueryImpl getInstance() {
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
