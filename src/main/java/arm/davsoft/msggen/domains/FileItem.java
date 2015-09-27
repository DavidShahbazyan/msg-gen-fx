package arm.davsoft.msggen.domains;

import arm.davsoft.msggen.interfaces.Selectable;
import javafx.beans.property.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/22/15 <br/>
 * <b>Time:</b> 1:42 AM <br/>
 */
public class FileItem extends File implements Selectable {
    private String fileContent;
    private BooleanProperty isSelected;
    private StringProperty relativePath;
    private IntegerProperty selMessagesQuantity;
    private IntegerProperty totMessagesQuantity;
    private List<LineItem> lineItems = new ArrayList<>();

    public FileItem(File file, String relativePath) {
        super(file.getPath());
        this.isSelected = new SimpleBooleanProperty(false);
        this.relativePath = new SimpleStringProperty(relativePath);
        this.selMessagesQuantity = new SimpleIntegerProperty();
        this.totMessagesQuantity = new SimpleIntegerProperty();
    }

    public String getFileContent() {
        return fileContent;
    }
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
//    public void setLineItems(List<LineItem> lineItems) {
//        this.lineItems = lineItems;
//    }
    public FileItem setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
        return this;
    }

    /**
     *
     * @param projectPath    The project path defined in the main controller
     * @return The file path starting from the project's path
     */
    public String getPath(String projectPath) {
        String path = super.getPath();
        return path.substring(path.indexOf(projectPath));
    }

    public boolean getIsSelected() {
        return isSelected.get();
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
        updateLineItems();
    }

    public void updateLineItems() {
        for (LineItem lineItem : lineItems) {
            lineItem.setIsSelected(isSelected.get());
        }
        updateSelMessagesQuantity();
    }

    public String getRelativePath() {
        return relativePath.get();
    }

    public StringProperty relativePathProperty() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath.set(relativePath);
    }

    public boolean hasSelectedMessage() {
        for (LineItem lineItem : lineItems) {
            if (lineItem.getIsSelected()) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public void setSelected(boolean selected) {
//        for (LineItem lineItem : lineItems) {
//            lineItem.setSelected(selected);
//        }
//    }

    public int getTotalMessagesQuantity() {
        return lineItems.size();
    }

    private void updateMessagesSelTotProperties() {
//        this.selMessagesQuantity.setValue(getSelectedMessagesQuantity());
//        this.totMessagesQuantity.setValue(getTotalMessagesQuantity());
    }

    public int getSelMessagesQuantity() {
        return selMessagesQuantity.get();
    }
    public IntegerProperty selMessagesQuantityProperty() {
        return selMessagesQuantity;
    }
    public void setSelMessagesQuantity(int selMessagesQuantity) {
        this.selMessagesQuantity.set(selMessagesQuantity);
    }
    public void updateSelMessagesQuantity() {
        int counter = 0;
        for (LineItem lineItem : lineItems) {
            if (lineItem.getIsSelected()) {
                counter++;
            }
        }
        this.selMessagesQuantity.setValue(counter);
    }

    public int getTotMessagesQuantity() {
        return totMessagesQuantity.get();
    }
    public IntegerProperty totMessagesQuantityProperty() {
        return totMessagesQuantity;
    }
    public void setTotMessagesQuantity(int totMessagesQuantity) {
        this.totMessagesQuantity.set(totMessagesQuantity);
    }
    public void updateTotMessagesQuantity() {
        this.totMessagesQuantity.setValue(this.lineItems.size());
    }

}
