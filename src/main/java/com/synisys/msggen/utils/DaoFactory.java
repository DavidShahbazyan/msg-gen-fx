package com.synisys.msggen.utils;

import com.synisys.msggen.daos.MySQLDao;
import com.synisys.msggen.daos.OracleDao;
import com.synisys.msggen.daos.MSSQLDao;
import com.synisys.msggen.enums.DBServerType;
import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.interfaces.Dao;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 2:13 PM <br/>
 */
public final class DaoFactory {
    private DaoFactory() {}

    public static Dao getDao(DBServerType dbServerType, ConnectionConfig config) {
        switch (dbServerType) {
            case MSSQLServer:
                return new MSSQLDao(config);
            case ORAServer:
                return new OracleDao(config);
            case MySQLServer:
                return new MySQLDao(config);
            default:
                return null;
        }
    }
}
