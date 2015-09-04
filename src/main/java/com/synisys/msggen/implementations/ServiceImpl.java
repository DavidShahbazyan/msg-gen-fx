package com.synisys.msggen.implementations;

import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.interfaces.Dao;
import com.synisys.msggen.interfaces.Service;
import com.synisys.msggen.utils.DaoFactory;

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
