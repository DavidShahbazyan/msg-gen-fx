package arm.davsoft.msgman.interfaces;

import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.IDMVersion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import javax.sql.DataSource;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/11/15 <br/>
 * <b>Time:</b> 1:19 PM <br/>
 */
public interface ConnectionConfig extends Cloneable {
    void initConnectionConfig();

    boolean isMSSQLServer();
    boolean isMySQLServer();
    boolean isORAServer();
    boolean isIDM6Project();
    boolean isIDM7Project();

    ObjectProperty<IDMVersion> getIdmVersionProperty();
    IDMVersion getIdmVersion();
    void setIdmVersion(IDMVersion idmVersion);

    DBServerType getDbServerType();

    SQLQuery getSqlQuery();

    DataSource getDataSource();
    void updateDataSource();

    String getJdbcUrl();

    ConnectionConfig clone();

    StringProperty getConnectionNameProperty();
    String getConnectionName();
    void setConnectionName(String connectionName);

    boolean isNew();
    void setIsNew(boolean isNew);

    StringProperty getHostNameProperty();
    String getHostName();
    void setHostName(String hostName);
    boolean isHostNameEmpty();

    IntegerProperty getPortProperty();
    Integer getPort();
    void setPort(Integer port);
    boolean isPortEmpty();

    StringProperty getDbNameProperty();
    String getDbName();
    void setDbName(String dbName);
    boolean isDbNameEmpty();

    StringProperty getSIDProperty();
    String getSID();
    void setSID(String SID);
    boolean isSIDEmpty();

    StringProperty getUserNameProperty();
    String getUserName();
    void setUserName(String userName);
    boolean isUserNameEmpty();

    StringProperty getPasswordProperty();
    String getPassword();
    void setPassword(String password);
    boolean isPasswordEmpty();

    boolean getIsValid();
    BooleanProperty isValidProperty();
    void setIsValid(boolean isValid);
    void validate();

}
