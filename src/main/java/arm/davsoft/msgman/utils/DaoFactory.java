package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.daos.MySQLDao;
import arm.davsoft.msgman.daos.OracleDao;
import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.Dao;
import arm.davsoft.msgman.daos.MSSQLDao;

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
