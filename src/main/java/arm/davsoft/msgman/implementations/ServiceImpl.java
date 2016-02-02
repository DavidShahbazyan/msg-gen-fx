package arm.davsoft.msgman.implementations;

import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Dao;
import arm.davsoft.msgman.interfaces.Range;
import arm.davsoft.msgman.interfaces.Service;
import arm.davsoft.msgman.utils.DaoFactory;
import arm.davsoft.msgman.utils.ResourceManager;

import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:13 PM <br/>
 */
public abstract class ServiceImpl implements Service {
    protected final ConnectionConfig config;
    protected final Dao dao;
    protected final Range messageRange;

    public ServiceImpl(ConnectionConfig config) {
        this.config = config;
        this.dao = DaoFactory.getDao(config.getDbServerType(), config);
        this.messageRange = ResourceManager.getProjectMessageRange();
    }

    public ConnectionConfig getConfig() {
        return config;
    }

    public Dao getDao() {
        return dao;
    }

    public Range getMessageRange() {
        return messageRange;
    }

    public List<String> loadSchemaNames() {
        return null;
    }
}
