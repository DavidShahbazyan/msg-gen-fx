package com.synisys.msggen.utils;

import com.synisys.msggen.domains.FileItem;
import com.synisys.msggen.domains.LineItem;
import com.synisys.msggen.enums.Tag;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public class MessagesHandler extends DefaultHandler {

    private final FileItem fileItem;

    public MessagesHandler(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public FileItem getFileItem() {
        return fileItem;
    }

    @Override
    public void startElement(String uri, String localName, String rawName, Attributes attributes) throws SAXException {
        for (Tag tag : Tag.values()) {
            if (rawName.equalsIgnoreCase(tag.getName())) {
                for (String attribute : tag.getAttributesList()) {
                    processValue(attributes.getValue(attribute));
                }
            }
        }
    }

    private void processValue(String value) {
        if (value != null) {
            String values[] = value.split(ResourceManager.getParam("REGEXP.PATTERN.EL.EXPRESSION"));
            for (String v : values) {
                Pattern pattern = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
                Matcher matcher = pattern.matcher(v);
                if (matcher.find() && !isBooleanValue(value) && !isOnlyDigitValue(value)) {
                    LineItem lineItem = new LineItem();
                    lineItem.setValue(v);
                    fileItem.getLineItems().add(lineItem);
                }
            }
        }
    }

    private boolean isBooleanValue(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    private boolean isOnlyDigitValue(String value) {
        return value.matches("\\d+");
    }

//    @Override
//    public void characters(char[] ch, int start, int length) throws SAXException {
//        String content = new String(Arrays.copyOfRange(ch, start, start + length));
//        String values[] = content.split(AppConstants.EL_EXPRESSION_REGEXP_PATTERN.pattern());
//        for (String v : values) {
//            Pattern pattern = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
//            Matcher matcher = pattern.matcher(v);
//            if (matcher.find()) {
//                LineItem lineItem = new LineItem();
//                lineItem.setValue(v);
//                fileItem.getLinesList().add(lineItem);
//            }
//        }
//    }
}
