package com.synisys.msggen.interfaces;

import com.synisys.msggen.domains.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 6:05 PM <br/>
 */
public interface Dao {
    List<String> loadSchemaNames() throws SQLException;

    List<Message> loadMessages(Map<String, Object> params) throws SQLException;

    List<Message> loadEmptyMessages(Map<String, Object> params) throws SQLException;

    void generateNewEmptyMessages(Map<String, Object> params) throws SQLException;

    void removeUnusedMessages(Map<String, Object> params) throws SQLException;

    void transferMessage(Map<String, Object> params) throws SQLException;
}
