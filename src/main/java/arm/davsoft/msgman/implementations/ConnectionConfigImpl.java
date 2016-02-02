package arm.davsoft.msgman.implementations;

import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.IDMVersion;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.interfaces.SQLQuery;
import arm.davsoft.msgman.utils.DataSourceFactory;
import arm.davsoft.msgman.utils.SQLQueryFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import javafx.beans.property.*;

import javax.sql.DataSource;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:19 PM <br/>
 */
public class ConnectionConfigImpl implements ConnectionConfig {
    private final DBServerType dbServerType;
    private ObjectProperty<IDMVersion> idmVersion = new SimpleObjectProperty<>(IDMVersion.IDM6);
    private SQLQuery sqlQuery;

    private DataSource dataSource;

    private boolean isNew;
    private StringProperty connectionName = new SimpleStringProperty();
    private StringProperty hostName = new SimpleStringProperty();
    private IntegerProperty port = new SimpleIntegerProperty();
    private StringProperty dbName = new SimpleStringProperty();
    private StringProperty SID = new SimpleStringProperty();
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private BooleanProperty isValid = new SimpleBooleanProperty();

    public ConnectionConfigImpl(DBServerType dbServerType) {
        this.isNew = true;
        this.dbServerType = dbServerType;
        initConnectionConfig();
    }

    @Override public void initConnectionConfig() {
        this.sqlQuery = SQLQueryFactory.getSqlQuery(idmVersion.get(), dbServerType);
        this.port = new SimpleIntegerProperty(dbServerType.getDefaultPortNumber());
    }

    @Override public boolean isMSSQLServer() { return DBServerType.MSSQLServer.equals(this.dbServerType); }
    @Override public boolean isMySQLServer() { return DBServerType.MySQLServer.equals(this.dbServerType); }
    @Override public boolean isORAServer() { return DBServerType.ORAServer.equals(this.dbServerType); }

    @Override public boolean isIDM6Project() { return IDMVersion.IDM6.equals(this.idmVersion.get()); }
    @Override public boolean isIDM7Project() { return IDMVersion.IDM7.equals(this.idmVersion.get()); }

    @Override public ObjectProperty<IDMVersion> getIdmVersionProperty() { return idmVersion; }
    @Override public IDMVersion getIdmVersion() {
        return idmVersion.get();
    }
    @Override public void setIdmVersion(IDMVersion idmVersion) {
        this.idmVersion.set(idmVersion);
    }

    @Override public DBServerType getDbServerType() { return dbServerType; }

    @Override public SQLQuery getSqlQuery() { return sqlQuery; }

    @Override
    public DataSource getDataSource() {
        // Double check for data source not to be null
        if (this.dataSource == null) {
            this.dataSource = DataSourceFactory.getInstance().createDataSource(this);
        }
        return dataSource;
    }

    @Override
    public void updateDataSource() {
        ((ComboPooledDataSource) getDataSource()).setJdbcUrl(getJdbcUrl());
        ((ComboPooledDataSource) getDataSource()).setUser(getUserName());
        ((ComboPooledDataSource) getDataSource()).setPassword(getUserName());
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
        ConnectionConfigImpl config = new ConnectionConfigImpl(dbServerType);
        config.setIdmVersion(idmVersion.get());
        config.setConnectionName(connectionName.get());
        config.setHostName(hostName.get());
        config.setPort(port.get());
        config.setDbName(dbName.get());
        config.setSID(SID.get());
        config.setUserName(userName.get());
        config.setPassword(password.get());
        config.setIsValid(isValid.get());
        return config;
    }

    @Override public StringProperty getConnectionNameProperty() { return connectionName; }
    @Override public String getConnectionName() { return connectionName.get(); }
    @Override public void setConnectionName(String connectionName) { this.connectionName.set(connectionName); }

    @Override public boolean isNew() { return isNew; }
    @Override public void setIsNew(boolean isNew) { this.isNew = isNew; }

    @Override public StringProperty getHostNameProperty() { return hostName; }
    @Override public String getHostName() { return hostName.get(); }
    @Override public void setHostName(String hostName) { this.hostName.set(hostName); }
    @Override public boolean isHostNameEmpty() {
        return this.hostName == null || this.hostName.get() == null || this.hostName.get().trim().equals("");
    }

    @Override public IntegerProperty getPortProperty() { return port; }
    @Override public Integer getPort() { return port.get(); }
    @Override public void setPort(Integer port) { this.port.set(port); }
    @Override public boolean isPortEmpty() { return this.port == null; }

    @Override public StringProperty getDbNameProperty() { return dbName; }
    @Override public String getDbName() { return dbName.get(); }
    @Override public void setDbName(String dbName) { this.dbName.set(dbName); }
    @Override public boolean isDbNameEmpty() {
        return this.dbName == null || this.dbName.get() == null || this.dbName.get().trim().equals("");
    }

    @Override public StringProperty getSIDProperty() { return SID; }
    @Override public String getSID() { return SID.get(); }
    @Override public void setSID(String SID) { this.SID.set(SID); }
    @Override public boolean isSIDEmpty() {
        return this.SID == null || this.SID.get() == null || this.SID.get().trim().equals("");
    }

    @Override public StringProperty getUserNameProperty() { return userName; }
    @Override public String getUserName() { return userName.get(); }
    @Override public void setUserName(String userName) { this.userName.set(userName); }
    @Override public boolean isUserNameEmpty() {
        return this.userName == null || this.userName.get() == null || this.userName.get().trim().equals("");
    }

    @Override public StringProperty getPasswordProperty() { return password; }
    @Override public String getPassword() { return password.get(); }
    @Override public void setPassword(String password) { this.password.set(password); }
    @Override public boolean isPasswordEmpty() {
        return this.password == null || this.password.get() == null || this.password.get().trim().equals("");
    }

    @Override public boolean getIsValid() { return isValid.get(); }
    @Override public BooleanProperty isValidProperty() { return isValid; }
    @Override public void setIsValid(boolean isValid) { this.isValid.set(isValid); }
    @Override public void validate() {
        boolean valid = false;
        if (isMSSQLServer()) {
            valid = !isHostNameEmpty() && !isDbNameEmpty() && !isUserNameEmpty() && !isPasswordEmpty();
        } else if (isORAServer()) {
            valid = !isHostNameEmpty() && !isPortEmpty() && !isSIDEmpty() && !isUserNameEmpty() && !isPasswordEmpty();
        } else if (isMySQLServer()) {
            valid = !isHostNameEmpty() && !isPortEmpty() && !isDbNameEmpty() && !isUserNameEmpty() && !isPasswordEmpty();
        }
        isValid.set(valid);
    }

}
