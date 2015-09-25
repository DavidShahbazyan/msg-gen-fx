package arm.davsoft.msggen.interfaces;

import arm.davsoft.msggen.domains.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/12/15 <br/>
 * <b>Time:</b> 6:26 AM <br/>
 */
public interface Service {
    List<String> loadSchemaNames();

    List<Message> loadMessages(Range range);

    List<Message> loadEmptyMessages(Range range);

    void generateNewEmptyMessages(Range range) throws SQLException;

    void removeUnusedMessages(Range range, Set<Integer> exceptIds);

    void transferMessages(List<Message> messages);

    boolean transferMessage(Message message);
}
