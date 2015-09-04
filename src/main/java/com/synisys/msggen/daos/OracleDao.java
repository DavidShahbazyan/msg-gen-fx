package com.synisys.msggen.daos;

import com.synisys.msggen.implementations.DaoImpl;
import com.synisys.msggen.interfaces.ConnectionConfig;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/17/15 <br/>
 * <b>Time:</b> 12:43 PM <br/>
 */
public class OracleDao extends DaoImpl {
    public OracleDao(ConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    /****** Below are ORAServer specific calls ******/
}
