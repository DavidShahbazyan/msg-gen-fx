package arm.davsoft.msgman.service;

import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.implementations.ServiceImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.utils.Utils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

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
        return loadMessages(config.getMessagesRange());
    }

    @Override
    public List<Message> loadMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            messages = dao.loadMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public List<Message> loadMessagesExcept(Set<Integer> exceptIds) {
        return loadMessagesExcept(config.getMessagesRange(), exceptIds);
    }

    @Override
    public List<Message> loadMessagesExcept(Range range, Set<Integer> exceptIds) {
        List<Message> messages = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            if (exceptIds != null && !exceptIds.isEmpty()) {
                params.put("@@MessageIds", Utils.joinIntegers(exceptIds));
                messages = dao.loadMessagesExcept(params);
            } else {
                messages = dao.loadMessages(params);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public List<Message> loadEmptyMessages() {
        return loadEmptyMessages(config.getMessagesRange());
    }

    @Override
    public List<Message> loadEmptyMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            messages = dao.loadEmptyMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public void generateNewEmptyMessages() throws SQLException {
        generateNewEmptyMessages(config.getMessagesRange());
    }
    
    @Override
    public void generateNewEmptyMessages(Range range) throws SQLException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            dao.generateNewEmptyMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            throw ex;
        }
    }

    @Override
    public void removeMessages(Set<Integer> messageIds) {
        removeMessages(config.getMessagesRange(), messageIds);
    }

    @Override
    public void removeMessages(Range range, Set<Integer> messageIds) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            params.put("@@MessageIds", Utils.joinIntegers(messageIds));
            dao.removeMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
    }

    @Override
    public void removeMessagesExcept(Set<Integer> exceptIds) {
        removeMessagesExcept(config.getMessagesRange(), exceptIds);
    }

    @Override
    public void removeMessagesExcept(Range range, Set<Integer> exceptIds) {
        try {
            List<Object> params = Arrays.asList(Utils.joinIntegers(exceptIds), range.getFrom(), range.getTo());
            dao.removeMessagesExcept(params);
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
            List<Object> params = Arrays.asList(message.getText(), message.getId());
            dao.transferMessage(params);
            success = true;
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(getClass()).error(ex);
        }
        return success;
    }
}
