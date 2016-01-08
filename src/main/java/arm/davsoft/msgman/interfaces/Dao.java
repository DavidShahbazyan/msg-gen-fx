package arm.davsoft.msgman.interfaces;

import arm.davsoft.msgman.domains.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 6:05 PM <br/>
 */
public interface Dao {
    void backupMessagesTable() throws SQLException;

    List<String> loadSchemaNames() throws SQLException;

    List<Message> loadMessages(Map<String, Object> params) throws SQLException;

    List<Message> loadMessagesExcept(Map<String, Object> params) throws SQLException;

    List<Message> loadEmptyMessages(Map<String, Object> params) throws SQLException;

    void generateNewEmptyMessages(Map<String, Object> params) throws SQLException;

    void removeMessages(Map<String, Object> params) throws SQLException;

//    void removeMessagesExcept(Map<String, Object> params) throws SQLException;

    void removeMessagesExcept(List<Object> params) throws SQLException;

//    void transferMessage(Map<String, Object> params) throws SQLException;

    void transferMessage(List<Object> params) throws SQLException;
}
