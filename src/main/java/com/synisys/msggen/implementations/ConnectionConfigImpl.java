package com.synisys.msggen.implementations;

import com.synisys.msggen.enums.DBServerType;
import com.synisys.msggen.enums.IDMVersion;
import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.interfaces.SQLQuery;
import com.synisys.msggen.utils.DataSourceFactory;
import com.synisys.msggen.utils.SQLQueryFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.sql.DataSource;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:19 PM <br/>
 */
public class ConnectionConfigImpl implements ConnectionConfig {
    private final IDMVersion idmVersion;
    private final DBServerType dbServerType;
    private final SQLQuery sqlQuery;

    private SimpleStringProperty connectionName = new SimpleStringProperty();
    private SimpleStringProperty hostName = new SimpleStringProperty();
    private SimpleIntegerProperty port = new SimpleIntegerProperty();
    private SimpleStringProperty dbName = new SimpleStringProperty();
    private SimpleStringProperty SID = new SimpleStringProperty();
    private SimpleStringProperty userName = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();

    public ConnectionConfigImpl(IDMVersion idmVersion, DBServerType dbServerType) {
        this.idmVersion = idmVersion;
        this.dbServerType = dbServerType;
        this.sqlQuery = SQLQueryFactory.getSqlQuery(idmVersion, dbServerType);
        this.port = new SimpleIntegerProperty(dbServerType.getDefaultPortNumber());
    }


    @Override
    public boolean isMSSQLServer() {
        return DBServerType.MSSQLServer.equals(this.dbServerType);
    }

    @Override
    public boolean isMySQLServer() {
        return DBServerType.MySQLServer.equals(this.dbServerType);
    }

    @Override
    public boolean isORAServer() {
        return DBServerType.ORAServer.equals(this.dbServerType);
    }

    @Override
    public boolean isIDM6Project() {
        return IDMVersion.IDM6.equals(this.idmVersion);
    }

    @Override
    public boolean isIDM7Project() {
        return IDMVersion.IDM7.equals(this.idmVersion);
    }

    @Override
    public IDMVersion getIdmVersion() {
        return idmVersion;
    }

    @Override
    public DBServerType getDbServerType() {
        return dbServerType;
    }

    @Override
    public SQLQuery getSqlQuery() {
        return sqlQuery;
    }

    @Override
    public DataSource getDataSource() {
        return DataSourceFactory.getInstance().createDataSource(this);
    }

    @Override
    public String getJdbcUrl() {
        String jdbcUrl = null;
        if (isMSSQLServer()) {
            jdbcUrl = String.format(dbServerType.getJdbcUrlPattern(), hostName.get(), port.get(), dbName.get() != null ? dbName.get() : "");
        } else if (isMySQLServer()) {
            jdbcUrl = String.format(dbServerType.getJdbcUrlPattern(), hostName.get(), port.get(), dbName.get() != null ? dbName.get() : "");
        } else if (isORAServer()) {
            jdbcUrl = String.format(dbServerType.getJdbcUrlPattern(), hostName.get(), port.get(), SID.get() != null ? SID.get() : "");
        }
        return jdbcUrl;
    }

    @Override
    public ConnectionConfig clone() {
        ConnectionConfigImpl config = new ConnectionConfigImpl(idmVersion, dbServerType);
        config.setConnectionName(connectionName.get());
        config.setHostName(hostName.get());
        config.setPort(port.get());
        config.setDbName(dbName.get());
        config.setSID(SID.get());
        config.setUserName(userName.get());
        config.setPassword(password.get());
        return config;
    }

    @Override
    public SimpleStringProperty getConnectionNameProperty() {
        return connectionName;
    }
    @Override
    public String getConnectionName() {
        return connectionName.get();
    }
    public void setConnectionName(String connectionName) {
        this.connectionName.set(connectionName);
    }

    @Override
    public SimpleStringProperty getHostNameProperty() {
        return hostName;
    }
    @Override
    public String getHostName() {
        return hostName.get();
    }
    public void setHostName(String hostName) {
        this.hostName.set(hostName);
    }

    @Override
    public SimpleIntegerProperty getPortProperty() {
        return port;
    }
    @Override
    public Integer getPort() {
        return port.get();
    }
    public void setPort(Integer port) {
        this.port.set(port);
    }

    @Override
    public SimpleStringProperty getDbNameProperty() {
        return dbName;
    }
    @Override
    public String getDbName() {
        return dbName.get();
    }
    public void setDbName(String dbName) {
        this.dbName.set(dbName);
    }

    @Override
    public SimpleStringProperty getSIDProperty() {
        return SID;
    }
    @Override
    public String getSID() {
        return SID.get();
    }
    public void setSID(String SID) {
        this.SID.set(SID);
    }

    @Override
    public SimpleStringProperty getUserNameProperty() {
        return userName;
    }
    @Override
    public String getUserName() {
        return userName.get();
    }
    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    @Override
    public SimpleStringProperty getPasswordProperty() {
        return password;
    }
    @Override
    public String getPassword() {
        return password.get();
    }
    public void setPassword(String password) {
        this.password.set(password);
    }

}
