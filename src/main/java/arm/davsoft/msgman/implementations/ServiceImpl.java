package arm.davsoft.msgman.implementations;

import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Dao;
import arm.davsoft.msgman.interfaces.Service;
import arm.davsoft.msgman.utils.DaoFactory;

import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:13 PM <br/>
 */
public abstract class ServiceImpl implements Service {
    protected final ConnectionConfig config;
    protected final Dao dao;

    public ServiceImpl(ConnectionConfig config) {
        this.config = config;
        this.dao = DaoFactory.getDao(config.getDbServerType(), config);
    }

    public ConnectionConfig getConfig() {
        return config;
    }

    public Dao getDao() {
        return dao;
    }

    public List<String> loadSchemaNames() {
        return null;
    }
}
