package arm.davsoft.msggen.implementations;

import arm.davsoft.msggen.interfaces.ConnectionConfig;
import arm.davsoft.msggen.interfaces.Dao;
import arm.davsoft.msggen.interfaces.Service;
import arm.davsoft.msggen.utils.DaoFactory;

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
