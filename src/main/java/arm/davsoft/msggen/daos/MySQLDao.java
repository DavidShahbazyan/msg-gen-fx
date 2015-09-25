package arm.davsoft.msggen.daos;

import arm.davsoft.msggen.interfaces.ConnectionConfig;
import arm.davsoft.msggen.implementations.DaoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/17/15 <br/>
 * <b>Time:</b> 12:43 PM <br/>
 */
public class MySQLDao extends DaoImpl {
    public MySQLDao(ConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    /****** Below are MySQLServer specific calls ******/

    @Override
    public List<String> loadSchemaNames() throws SQLException {
        List<String> schemaNames = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                schemaNames.add(resultSet.getString("TABLE_CAT"));
            }
        } finally {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
        }
        return schemaNames;
    }

}
