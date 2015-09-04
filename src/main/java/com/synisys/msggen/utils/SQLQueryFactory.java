package com.synisys.msggen.utils;

import com.synisys.msggen.enums.DBServerType;
import com.synisys.msggen.enums.IDMVersion;
import com.synisys.msggen.implementations.IDM_6_MSSQLQueryImpl;
import com.synisys.msggen.implementations.IDM_6_MySQLQueryImpl;
import com.synisys.msggen.implementations.IDM_7_MSSQLQueryImpl;
import com.synisys.msggen.implementations.IDM_7_MySQLQueryImpl;
import com.synisys.msggen.interfaces.SQLQuery;

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
