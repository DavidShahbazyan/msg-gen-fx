package arm.davsoft.msgman.utils;

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

    public static String concatStrings(Collection<Object> strings, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : strings) {
            stringBuilder.append(o).append(delimiter);
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static String joinIntegers(Collection<Integer> items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer item : items) {
            stringBuilder.append(item).append(",");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
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

    public static void savePropertiesToFile(String fileName, Properties properties) throws IOException {
        File file = new File(fileName);
        OutputStream out = new FileOutputStream(file);
        properties.store(out, "This is an optional header comment string");
        out.close();
    }
}
