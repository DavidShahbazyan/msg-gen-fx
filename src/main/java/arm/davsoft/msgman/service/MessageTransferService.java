package arm.davsoft.msgman.service;

import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.utils.Utils;
import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.implementations.ServiceImpl;
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
    public List<Message> loadMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@From", range.getFrom());
            params.put("@@To", range.getTo());
            messages = dao.loadMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
    }

    @Override
    public List<Message> loadEmptyMessages(Range range) {
        List<Message> messages = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@From", range.getFrom());
            params.put("@@To", range.getTo());
            messages = dao.loadEmptyMessages(params);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
        }
        return messages;
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
    public void removeUnusedMessages(Range range, Set<Integer> exceptIds) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("@@RangeStart", range.getFrom());
            params.put("@@RangeEnd", range.getTo());
            params.put("@@MessageIds", Utils.join(exceptIds));
            dao.removeUnusedMessages(params);
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
            Map<String, Object> params = new HashMap<>();
            params.put("@@MessageId", message.getId());
            params.put("@@Text", message.getText());
            dao.transferMessage(params);
            success = true;
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(getClass()).error(ex);
        }
        return success;
    }
}
