import arm.davsoft.msgman.enums.DBServerType;
import arm.davsoft.msgman.enums.Tag;
import arm.davsoft.msgman.implementations.ConnectionConfigImpl;
import arm.davsoft.msgman.interfaces.ConnectionConfig;
import arm.davsoft.msgman.service.MessageTransferService;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/29/15 <br/>
 * <b>Time:</b> 8:34 PM <br/>
 */
public class MainTest {

    @Test
    public void dbConnectionTest() throws Exception {
        ConnectionConfig config = new ConnectionConfigImpl(DBServerType.MySQLServer);
        config.initConnectionConfig();
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

    @Test
    public void main() {
        boolean b = false;
        Tag tag = Enum.valueOf(Tag.class, "A");
        System.out.println(String.valueOf(Boolean.class.isInstance(b)));
    }
}
