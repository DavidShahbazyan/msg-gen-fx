package arm.davsoft.msgman.service;

import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.implementations.ServiceImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.utils.Utils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:14 PM <br/>
 */
public class MessageTransferService extends ServiceImpl {

    public MessageTransferService(ConnectionConfig config) {
        super(config);
    }

    @Override
    public void backupMessagesTable() throws SQLException {
        try {
            dao.backupMessagesTable();
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            throw ex;
        }
    }

    @Override
    public List<String> loadSchemaNames() {
        List<String> schemaNames = new ArrayList<>();
        try {
            schemaNames = dao.loadSchemaNames();
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return schemaNames;
    }

    @Override
    public List<Message> loadMessages() {
        return loadMessages(messageRange);
    }

    @Override
    public List<Message> loadMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            messages = dao.loadMessages(Arrays.asList(range.getFrom(), range.getTo()));
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public List<Message> loadMessagesExcept(Set<Integer> exceptIds) {
        return loadMessagesExcept(messageRange, exceptIds);
    }

    @Override
    public List<Message> loadMessagesExcept(Range range, Set<Integer> exceptIds) {
        List<Message> messages = new ArrayList<>();
        try {
            if (exceptIds != null && !exceptIds.isEmpty()) {
                messages = dao.loadMessagesExcept(Arrays.asList(range.getFrom(), range.getTo(), Utils.joinIntegers(exceptIds)));
            } else {
                messages = loadMessages();
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public List<Message> loadEmptyMessages() {
        return loadEmptyMessages(messageRange);
    }

    @Override
    public List<Message> loadEmptyMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            messages = dao.loadEmptyMessages(Arrays.asList(range.getFrom(), range.getTo()));
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public void generateNewEmptyMessages() throws SQLException {
        generateNewEmptyMessages(messageRange);
    }
    
    @Override
    public void generateNewEmptyMessages(Range range) throws SQLException {
        try {
            dao.generateNewEmptyMessages(Arrays.asList(range.getFrom(), range.getTo()));
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            throw ex;
        }
    }

    @Override
    public void removeMessages(Set<Integer> messageIds) {
        removeMessages(messageRange, messageIds);
    }

    @Override
    public void removeMessages(Range range, Set<Integer> messageIds) {
        try {
            dao.removeMessages(Arrays.asList(Utils.joinIntegers(messageIds), range.getFrom(), range.getTo()));
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
    }

    @Override
    public void removeMessagesExcept(Set<Integer> exceptIds) {
        removeMessagesExcept(messageRange, exceptIds);
    }

    @Override
    public void removeMessagesExcept(Range range, Set<Integer> exceptIds) {
        try {
            dao.removeMessagesExcept(Arrays.asList(Utils.joinIntegers(exceptIds), range.getFrom(), range.getTo()));
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
    }

    @Override
    public void transferMessages(List<Message> messages) {
        for (Message message : messages) {
            transferMessage(message);
        }
    }

    @Override
    public boolean transferMessage(Message message) {
        boolean success;
        try {
            dao.transferMessage(Arrays.asList(message.getText(), message.getId()));
            success = true;
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(getClass()).error(ex);
        }
        return success;
    }
}
