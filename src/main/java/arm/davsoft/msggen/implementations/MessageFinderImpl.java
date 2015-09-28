package arm.davsoft.msggen.implementations;

import arm.davsoft.msggen.domains.FileItem;
import arm.davsoft.msggen.domains.LineItem;
import arm.davsoft.msggen.enums.FileType;
import arm.davsoft.msggen.interfaces.MessageFinder;
import arm.davsoft.msggen.utils.MessagesHandler;
import arm.davsoft.msggen.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageFinderImpl implements MessageFinder {
    private final File projectDir;
    private final StringProperty projectPathProperty;
    private List<FileItem> filesList = new ArrayList<>();
    private Set<Integer> usedMessageIds = new HashSet<>();
    private List<String> selectedMessages;

    public MessageFinderImpl(String projectPath) {
        this.projectDir = new File(projectPath);
        this.projectPathProperty = new SimpleStringProperty(projectPath);
    }

    public String getProjectPath() {
        return projectPathProperty.get();
    }

    public StringProperty projectPathPropertyProperty() {
        return projectPathProperty;
    }

    @Override
    public List<FileItem> getFilesList() {
        return filesList;
    }

    @Override
    public void setFilesList(List<FileItem> filesList) {
        this.filesList = filesList;
    }

    @Override
    public List<String> getSelectedMessages() {
        return selectedMessages;
    }

    @Override
    public void findFileList() throws IOException, ParserConfigurationException, SAXException {
        MessageGenerationVisitor messageGenerationVisitor = new MessageGenerationVisitor();
        Path path = projectDir.toPath();
        this.filesList = new ArrayList<>();
        Files.walkFileTree(path, messageGenerationVisitor);
    }

    private FileItem currentFileTags(File file) throws IOException, ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        InputStream inStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        FileItem fileItem = new FileItem(file, file.getPath().substring(projectPathProperty.get().length()));
//        fileItem.setFileName(file.getName());
        fileItem.setFileContent(stringBuilder.toString());
        MessagesHandler messagesHandler = new MessagesHandler(fileItem);
        saxParser.parse(file.getPath(), messagesHandler);
        return messagesHandler.getFileItem();
    }


    @Override
    public void initSelectedMessages() {
        selectedMessages = new ArrayList<>();
        for (FileItem file : filesList) {
            for (LineItem lineItem : file.getLineItems()) {
                if (lineItem.getIsSelected()) {
                    selectedMessages.add(lineItem.getValue());
                }
            }
        }
    }


    /*message cleaner*/
    @Override
    public Set<Integer> findUsedMessages() throws IOException {
        usedMessageIds = new HashSet<>();
        MessageCleanerVisitor messageCleanerVisitor = new MessageCleanerVisitor();
        Files.walkFileTree(projectDir.toPath(), messageCleanerVisitor);
        return usedMessageIds;
    }

    @Override
    public void clearFileList() {
        this.filesList = new ArrayList<>();
    }

    public List<File> getMarkedFiles() {
        List<File> fileItems = new ArrayList<>();
        for (FileItem item : this.filesList) {
            if (item.getIsSelected()) {
                fileItems.add(item);
            }
        }
        return fileItems;
    }

//    @Override
//    public void putMessagesToFiles(List<File> filesList, List<Message> messages) throws IOException, ParserConfigurationException, SAXException, TransformerException {
//        for (File file : filesList) {
//            replaceFileTags(file, messages);
//        }
//    }

//    @Override
//    public void putMessagesFromDBToMainFolder(File mainFolder, List<Message> messages) throws IOException, ParserConfigurationException, SAXException, TransformerException {
//        for (File child : mainFolder.listFiles()) {
//            if (!child.isDirectory() && Utils.isFileType(child, FileType.XHTML)) {
//                replaceFileTags(child, messages);
//            }
//            if (child.isDirectory()) {
//                putMessagesFromDBToMainFolder(child, messages);
//            }
//        }
//    }

    public List<File> getProjectFilesList() throws IOException {
        List<File> retVal = new ArrayList<>();
        File[] files = projectDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory() && Utils.isFileType(file, FileType.XHTML)) {
                    retVal.add(file);
                }
            }
        }
        return retVal;
    }

//    private String replaceMessages(List<Message> messages, String value) {
//        String values[] = value.split(ResourceManager.getParam("REGEXP.PATTERN.EL.EXPRESSION"));
//        for (String v : values) {
//            Iterator<Message> messageIterator = messages.iterator();
//            while (messageIterator.hasNext()) {
//                Message message = messageIterator.next();
//                if (v.equals(message.getText())) {
//                    value = value.replace(v, "#{messages[" + message.getId().toString() + "]}");
//                    messageIterator.remove();
//                    break;
//                }
//            }
//        }
//        return value;
//    }

//    private void processElement(Element element, List<Message> messages, List<String> attributesList) {
//        String value = "";
//        for (String attribute : attributesList) {
//            if (element.hasAttribute(attribute) && !"".equals(value = element.getAttribute(attribute))) {
//                element.setAttribute(attribute, replaceMessages(messages, value));
//            }
//        }
//    }

//    private void replaceFileTags(File file, List<Message> messages) throws IOException, SAXException, ParserConfigurationException, TransformerException {
//        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//        Document document = documentBuilder.parse(file.getPath());
//        NodeList nodeList = document.getDocumentElement().getChildNodes();
//        Deque<Node> nodeQueue = new LinkedList<>();
//        for (int i = 0; i <= nodeList.getLength(); ++i) {
//            nodeQueue.add(nodeList.item(i));
//        }
////        String value = "";
//        while (!nodeQueue.isEmpty()) {
//            Node currentNode = nodeQueue.poll();
//            if (currentNode != null) {
//                if (currentNode instanceof Element) {
//                    Element element = (Element) currentNode;
//                    for (Tag tag : Tag.values()) {
//                        if (tag.getName().equals(element.getNodeName())) {
//                            processElement(element, messages, tag.getAttributesList());
//                        }
//                    }
//// ATTENTION !!! DO NOT REMOVE THE COMMENTED BLOCK BELOW !!!!!!!!
////                } else if (currentNode instanceof Text) {
////                    Text text = (Text) currentNode;
////                    value = text.getNodeValue();
////                    value = replaceMessages(messages, value);
////                    text.setNodeValue(value);
////                    continue;
//                }
//
//                if (currentNode.getChildNodes() != null) {
//                    for (int i = 0; i < currentNode.getChildNodes().getLength(); ++i) {
//                        nodeQueue.add(currentNode.getChildNodes().item(i));
//                    }
//                }
//            }
//        }
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(document);
//        StreamResult result = new StreamResult(file);
//        transformer.transform(source, result);
//    }

   /*inner   classes*/

    private class MessageGenerationVisitor implements FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            File file = path.toFile();
            try {
                if (Utils.isFileType(file, FileType.XHTML) && !file.isDirectory()) {
                    FileItem fileItem = currentFileTags(file);
                    filesList.add(fileItem);
                }
            } catch (ParserConfigurationException | SAXException e) {
                Logger.getLogger(getClass()).error("Error occurred in visitFile method: ", e);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return null;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

    }


    private class MessageCleanerVisitor implements FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

            File file = path.toFile();
            if (Utils.isFileType(file, FileType.XHTML) && !file.isDirectory()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                usedMessageIds.addAll(Utils.findMessagesInFile(bufferedReader));
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return null;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
