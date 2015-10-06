package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.interfaces.ConnectionConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public final class DataSourceFactory {

    private DataSourceFactory() {
    }

    public DataSource createDataSource(ConnectionConfig connectionConfig) {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setTestConnectionOnCheckout(true);
            dataSource.setDriverClass(connectionConfig.getDbServerType().getDriver());
            dataSource.setJdbcUrl(connectionConfig.getJdbcUrl());
            dataSource.setUser(connectionConfig.getUserName());
            dataSource.setPassword(connectionConfig.getUserName());
            dataSource.setMaxPoolSize(10);
            dataSource.setMinPoolSize(5);
            dataSource.setInitialPoolSize(5);
            return dataSource;
        } catch (PropertyVetoException e) {
            Logger.getLogger(getClass()).error(e.getMessage());
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage());
        }
        return null;
    }

    public static DataSourceFactory getInstance() {
        return DatSourceFactoryHolder.INSTANCE;
    }

    private static final class DatSourceFactoryHolder {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}