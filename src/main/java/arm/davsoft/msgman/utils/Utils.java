package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.domains.Message;
import arm.davsoft.msgman.enums.FileType;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public class Utils {

    public static String concatStrings(Collection<String> strings) {
        return concatStrings(strings, ", ");
    }

    public static <T extends Object> String concatStrings(Collection<T> strings, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<T> iterator = strings.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            stringBuilder.append(item);
            if (iterator.hasNext()) {
                stringBuilder.append(delimiter);
            }
        }
        return stringBuilder.toString();
    }

    public static String joinIntegers(Collection<Integer> items) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> integerIterator = items.iterator();
        while (integerIterator.hasNext()) {
            Integer item = integerIterator.next();
            stringBuilder.append(item);
            if (integerIterator.hasNext()) {
                stringBuilder.append(',');
            }
        }
        return stringBuilder.toString();
    }

    public static String toJsonArray(List<String> items) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        int itemsCount = items.size();
        for (int i = 0; i < itemsCount - 1; i++) {
            stringBuilder.append("\"").append(items.get(i)).append("\",");
        }
        stringBuilder.append("\"").append(items.get(itemsCount - 1)).append("\"");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static Set<Integer> findMessagesInFile(BufferedReader bufferedReader) throws IOException {
        Set<Integer> messages = new HashSet<>();
        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = Pattern.compile(ResourceManager.getParam("REGEXP.PATTERN.MESSAGE")).matcher(line);
                while (matcher.find()) {
                    String messageId = matcher.group(2);
                    messages.add(Integer.valueOf(messageId));
                }
            }
        } finally {
            bufferedReader.close();
        }
        return messages;
    }

    public static boolean isFileType(File file, FileType... supportedFileTypes) {
        for (FileType fileType : supportedFileTypes) {
            if (file.getName().endsWith(fileType.getExtension())) {
                return true;
            }
        }
        return false;
    }

    public static void writeStringToFile(String fileName, String fileContent) {
        Writer wr = null;
        try {
            wr = new FileWriter(new File(fileName));
            wr.write(fileContent);
            wr.close();
        } catch (IOException ex) {
            Main.LOGGER.error("Error occurred in writeStringToFile method: ", ex);
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException ex) {
                    Main.LOGGER.error("Error occurred in writeStringToFile method: ", ex);
                }
            }
        }

    }

//    public static void exportLineItemsToFile(List<LineItem> lineItems) throws IOException {
//        File file = new File("MessagesFromProject_" + new Date().getTime() + ".export");
//        Writer wr = new FileWriter(file);
//        for (LineItem lineItem : lineItems) {
//            wr.write(lineItem.getValue());
//        }
//        wr.close();
//    }

    public static void exportMessagesToFile(List<Message> messages) throws IOException {
        String fileName = "MessagesFromDB_" + new Date().getTime() + ".export";
        StringBuilder fileContent = new StringBuilder();
        for (Message message : messages) {
            fileContent.append('[')
                    .append(message.getId()).append(" , ")
                    .append(message.getText()).append(']');
        }
        writeStringToFile(fileName, fileContent.toString());
    }
}
