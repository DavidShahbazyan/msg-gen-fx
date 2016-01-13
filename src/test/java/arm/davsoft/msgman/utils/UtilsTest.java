package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.enums.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 12/4/15 <br/>
 * <b>Time:</b> 2:40 AM <br/>
 */
public class UtilsTest {

    @Test
    public void testConcatStrings() throws Exception {
        try {
            Utils.concatStrings(new ArrayList<>(0));
            Utils.concatStrings(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        Assert.assertEquals("Incorrect return value.", "a, b", Utils.concatStrings(Arrays.asList("a", "b")));
    }

    @Test
    public void testConcatStrings1() throws Exception {
        try {
            Utils.concatStrings(new ArrayList<>(0), "");
            Utils.concatStrings(new ArrayList<>(0), null);
            Utils.concatStrings(null, "");
            Utils.concatStrings(null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        Assert.assertEquals("Incorrect return value.", "a|b", Utils.concatStrings(Arrays.asList("a", "b"), "|"));
    }

    @Test
    public void testJoinIntegers() throws Exception {

    }

    @Test
    public void testToJsonArray() throws Exception {

    }

    @Test
    public void testFindMessagesInFile() throws Exception {

    }

    @Test
    public void testIsFileType() throws Exception {

    }

    @Test
    public void testWriteStringToFile() throws Exception {

    }

    @Test
    public void testExportMessagesToFile() throws Exception {

    }

    @Test
    public void testGetSupportedTags() throws Exception {
        List<Tag> supportedTags = Utils.getSupportedTags();
        System.out.println(supportedTags.size());
    }
}