package arm.davsoft.msggen.daos;

import arm.davsoft.msggen.implementations.DaoImpl;
import arm.davsoft.msggen.interfaces.ConnectionConfig;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/17/15 <br/>
 * <b>Time:</b> 12:43 PM <br/>
 */
public class OracleDao extends DaoImpl {
    public OracleDao(ConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    /****** Below are ORAServer specific calls ******/
}
