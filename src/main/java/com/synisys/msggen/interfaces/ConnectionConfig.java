package com.synisys.msggen.interfaces;

import com.synisys.msggen.enums.DBServerType;
import com.synisys.msggen.enums.IDMVersion;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.sql.DataSource;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:19 PM <br/>
 */
public interface ConnectionConfig extends Cloneable {
    boolean isMSSQLServer();

    boolean isMySQLServer();

    boolean isORAServer();

    boolean isIDM6Project();

    boolean isIDM7Project();

    IDMVersion getIdmVersion();

    DBServerType getDbServerType();

    SQLQuery getSqlQuery();

    DataSource getDataSource();

    String getJdbcUrl();

    ConnectionConfig clone();

    SimpleStringProperty getConnectionNameProperty();

    String getConnectionName();

    void setConnectionName(String connectionName);

    SimpleStringProperty getHostNameProperty();

    String getHostName();

    void setHostName(String hostName);

    SimpleIntegerProperty getPortProperty();

    Integer getPort();

    void setPort(Integer port);

    SimpleStringProperty getDbNameProperty();

    String getDbName();

    void setDbName(String dbName);

    SimpleStringProperty getSIDProperty();

    String getSID();

    void setSID(String SID);

    SimpleStringProperty getUserNameProperty();

    String getUserName();

    void setUserName(String userName);

    SimpleStringProperty getPasswordProperty();

    String getPassword();

    void setPassword(String password);
}
