package arm.davsoft.msgman.interfaces;

import arm.davsoft.msgman.domains.Message;

import java.sql.SQLException;
import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 6:05 PM <br/>
 */
public interface Dao {
    void backupMessagesTable() throws SQLException;

    List<String> loadSchemaNames() throws SQLException;

    Integer loadLastMessageId() throws SQLException;

    List<Message> loadMessages(List<Object> params) throws SQLException;

    List<Message> loadMessagesExcept(List<Object> params) throws SQLException;

    List<Message> loadEmptyMessages(List<Object> params) throws SQLException;

    void generateNewEmptyMessages(List<Object> params) throws SQLException;

    void removeMessages(List<Object> params) throws SQLException;

    void removeMessagesExcept(List<Object> params) throws SQLException;

    void transferMessage(List<Object> params) throws SQLException;
}
