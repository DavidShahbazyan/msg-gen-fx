package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.enums.Tag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/17/15 <br/>
 * <b>Time:</b> 11:53 PM <br/>
 */
public class FileProcessor {
    private static String replaceMessages(List<Message> messages, String value) {
        String values[] = value.split(ResourceManager.getParam("REGEXP.PATTERN.EL.EXPRESSION"));
        for (String v : values) {
            Iterator<Message> messageIterator = messages.iterator();
            while (messageIterator.hasNext()) {
                Message message = messageIterator.next();
                if (v.equals(message.getText())) {
                    String replacement = String.format(ResourceManager.getSetting("messagePattern"), message.getId().toString());
                    value = value.replace(v, replacement);
                    messageIterator.remove();
                    break;
                }
            }
        }
        return value;
    }

    private static void processElement(Element element, List<Message> messages, List<String> attributesList) {
        String value = "";
        for (String attribute : attributesList) {
            if (element.hasAttribute(attribute) && !"".equals(value = element.getAttribute(attribute))) {
                element.setAttribute(attribute, replaceMessages(messages, value));
            }
        }
    }

    public static void replaceFileTags(File file, List<Message> messages) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file.getPath());
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        Deque<Node> nodeQueue = new LinkedList<>();
        for (int i = 0; i <= nodeList.getLength(); ++i) {
            nodeQueue.add(nodeList.item(i));
        }
//        String value = "";
        while (!nodeQueue.isEmpty()) {
            Node currentNode = nodeQueue.poll();
            if (currentNode != null) {
                if (currentNode instanceof Element) {
                    Element element = (Element) currentNode;
                    for (Tag tag : Utils.getSupportedTags()) {
                        if (tag.getName().equals(element.getNodeName())) {
                            processElement(element, messages, tag.getAttributesList());
                        }
                    }
// ATTENTION !!! DO NOT REMOVE THE COMMENTED BLOCK BELOW !!!!!!!!
//                } else if (currentNode instanceof Text) {
//                    Text text = (Text) currentNode;
//                    value = text.getNodeValue();
//                    value = replaceMessages(messages, value);
//                    text.setNodeValue(value);
//                    continue;
                }

                if (currentNode.getChildNodes() != null) {
                    for (int i = 0; i < currentNode.getChildNodes().getLength(); ++i) {
                        nodeQueue.add(currentNode.getChildNodes().item(i));
                    }
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}
