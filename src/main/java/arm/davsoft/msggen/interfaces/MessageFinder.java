package arm.davsoft.msggen.interfaces;

import arm.davsoft.msggen.domains.FileItem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public interface MessageFinder {

    List<FileItem> getFilesList();

    void setFilesList(List<FileItem> filesList);

    List<String> getSelectedMessages();

    void findFileList() throws IOException, ParserConfigurationException, SAXException;

    void initSelectedMessages();

    /*message cleaner*/
    Set<Integer> findUsedMessages() throws IOException;

    void clearFileList();

    List<File> getMarkedFiles();

//    void putMessagesToFiles(List<File> filesList, List<Message> messages) throws IOException, ParserConfigurationException, SAXException, TransformerException;

//    void putMessagesFromDBToMainFolder(File mainFolder, List<Message> messages) throws IOException, ParserConfigurationException, SAXException, TransformerException;
}
