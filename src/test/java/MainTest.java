import com.synisys.msggen.enums.DBServerType;
import com.synisys.msggen.enums.IDMVersion;
import com.synisys.msggen.implementations.ConnectionConfigImpl;
import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.service.MessageTransferService;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/29/15 <br/>
 * <b>Time:</b> 8:34 PM <br/>
 */
public class MainTest {

    @Test
    public void dbConnectionTest() throws Exception {
        ConnectionConfig config = new ConnectionConfigImpl(IDMVersion.IDM6, DBServerType.MySQLServer);
        config.setHostName("127.0.0.1");
        config.setUserName("root");
        config.setPassword("root");
        MessageTransferService service = new MessageTransferService(config);
        List<String> schemaNames = service.loadSchemaNames();
        System.out.println(schemaNames);
        if (schemaNames == null || schemaNames.size() == 0) {
            fail();
        }
    }
}
