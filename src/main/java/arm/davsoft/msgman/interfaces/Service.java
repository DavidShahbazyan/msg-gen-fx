package arm.davsoft.msgman.interfaces;

import arm.davsoft.msgman.domains.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/12/15 <br/>
 * <b>Time:</b> 6:26 AM <br/>
 */
public interface Service {
    void backupMessagesTable() throws SQLException;

    List<String> loadSchemaNames();
    Integer loadLastMessageId();

    List<Message> loadMessages();
    List<Message> loadMessages(Range range);

    List<Message> loadMessagesExcept(Set<Integer> exceptIds);
    List<Message> loadMessagesExcept(Range range, Set<Integer> exceptIds);

    List<Message> loadEmptyMessages();
    List<Message> loadEmptyMessages(Range range);

    void generateNewEmptyMessages() throws SQLException;
    void generateNewEmptyMessages(Range range) throws SQLException;

    void removeMessages(Set<Integer> messageIds);
    void removeMessages(Range range, Set<Integer> messageIds);

    void removeMessagesExcept(Set<Integer> exceptIds);
    void removeMessagesExcept(Range range, Set<Integer> exceptIds);

    void transferMessages(List<Message> messages);

    boolean transferMessage(Message message);
}
