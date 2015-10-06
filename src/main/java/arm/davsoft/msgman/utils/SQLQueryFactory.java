package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.IDMVersion;
import arm.davsoft.msgman.implementations.IDM_6_MSSQLQueryImpl;
import arm.davsoft.msgman.implementations.IDM_6_MySQLQueryImpl;
import arm.davsoft.msgman.implementations.IDM_7_MSSQLQueryImpl;
import arm.davsoft.msgman.implementations.IDM_7_MySQLQueryImpl;
import arm.davsoft.msgman.interfaces.SQLQuery;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 2:13 PM <br/>
 */
public final class SQLQueryFactory {
    private SQLQueryFactory() {}

    public static SQLQuery getSqlQuery(IDMVersion idmVersion, DBServerType dbServerType) {
        SQLQuery retVal = null;
        if (IDMVersion.IDM6.equals(idmVersion)) {
            if (DBServerType.MSSQLServer.equals(dbServerType)) {
                retVal = IDM_6_MSSQLQueryImpl.getInstance();
            } else if (DBServerType.ORAServer.equals(dbServerType)) {
                retVal = IDM_6_MSSQLQueryImpl.getInstance();
            } else if (DBServerType.MySQLServer.equals(dbServerType)) {
                retVal = IDM_6_MySQLQueryImpl.getInstance();
            }
        } else if (IDMVersion.IDM7.equals(idmVersion)) {
            if (DBServerType.MSSQLServer.equals(dbServerType)) {
                retVal = IDM_7_MSSQLQueryImpl.getInstance();
            } else if (DBServerType.ORAServer.equals(dbServerType)) {
                retVal = IDM_7_MSSQLQueryImpl.getInstance();
            } else if (DBServerType.MySQLServer.equals(dbServerType)) {
                retVal = IDM_7_MySQLQueryImpl.getInstance();
            }
        }
        return retVal;
    }
}
