package arm.davsoft.msgman.enums;

import arm.davsoft.msgman.interfaces.Selectable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Arrays;
import java.util.List;

/**
 * Holds the supported tags names with attributes.
 * <br/>
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/5/15 <br/>
 * <b>Time:</b> 10:25 PM <br/>
 */
public enum Tag implements Selectable {

//    ALL_SUPPORTED_ATTRIBUTES("", Arrays.asList("alt", "name", "value", "label", "title", "header", "footer", "message", "itemLabel", "headerText", "footerText", "emptyMessage", "requiredMessage")),

    A                       (1, "a",                     Arrays.asList("title")),
    IMG                     (2, "img",                   Arrays.asList("alt", "title")),
    SPAN                    (3, "span",                  Arrays.asList("title")),

    H_OUTPUT_LABEL          (4, "h:outputLabel",         Arrays.asList("value")),
    H_OUTPUT_TEXT           (5, "h:outputText",          Arrays.asList("value", "title")),
    H_INPUT_TEXT            (6, "h:inputText",           Arrays.asList("alt", "title", "label", "requiredMessage", "validatorMessage")),
    P_INPUT_TEXT            (7, "p:inputText",           Arrays.asList("alt", "title", "label", "requiredMessage", "validatorMessage")),
    H_INPUT_TEXT_AREA       (8, "h:inputTextarea",       Arrays.asList("title", "label", "requiredMessage", "validatorMessage")),
    P_INPUT_TEXT_AREA       (9, "p:inputTextarea",       Arrays.asList("title", "label", "requiredMessage", "validatorMessage")),
    P_SELECT_ONE_MENU       (10, "p:selectOneMenu",      Arrays.asList("requiredMessage")),
    P_CALENDAR              (11, "p:calendar",           Arrays.asList("requiredMessage")),
    H_COLUMN                (12, "h:column",             Arrays.asList("headerText", "footerText")),
    P_COLUMN                (13, "p:column",             Arrays.asList("headerText", "footerText")),
    F_SELECT_ITEM           (14, "f:selectItem",         Arrays.asList("itemLabel")),
    F_SELECT_ITEMS          (15, "f:selectItems",        Arrays.asList("itemLabel")),
    P_DIALOG                (16, "p:dialog",             Arrays.asList("header", "footer")),
    P_CONFIRM_DIALOG        (17, "p:confirmDialog",      Arrays.asList("header", "message")),
    DE_SUB_SECTION          (18, "de:subSection",        Arrays.asList("name", "title")),
    H_DATA_TABLE            (19, "h:dataTable",          Arrays.asList("emptyMessage")),
    P_DATA_TABLE            (20, "p:dataTable",          Arrays.asList("emptyMessage")),
    H_COMMAND_BUTTON        (21, "h:commandButton",      Arrays.asList("value")),
    P_COMMAND_BUTTON        (22, "p:commandButton",      Arrays.asList("value")),
    H_COMMAND_LINK          (23, "h:commandLink",        Arrays.asList("value")),
    P_COMMAND_LINK          (24, "p:commandLink",        Arrays.asList("value")),
    H_GRAPHIC_IMAGE         (25, "h:graphicImage",       Arrays.asList("alt", "title")),
    P_GRAPHIC_IMAGE         (26, "p:graphicImage",       Arrays.asList("alt", "title")),
    F_ATTRIBUTE             (27, "f:attribute",          Arrays.asList("value")),
    P_MENU_BUTTON           (28, "p:menuButton",         Arrays.asList("value")),
    UI_PARAM                (29, "ui:param",             Arrays.asList("value")),
    SIS_MULTILINGUAL_TEXT   (30, "sis:multilingualText", Arrays.asList("requiredMessage", "validatorMessage")),
    ;

    private Integer id;
    private String name;
    private List<String> attributesList;
    private BooleanProperty isSelectedProperty;

    Tag(Integer id, String name, List<String> attributesList) {
        this.id = id;
        this.name = name;
        this.attributesList = attributesList;
        isSelectedProperty = new SimpleBooleanProperty(false);
    }

    @Override
    public BooleanProperty isSelectedProperty() {
        return isSelectedProperty;
    }

    @Override
    public boolean getIsSelected() {
        return isSelectedProperty.getValue();
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        isSelectedProperty.setValue(isSelected);
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<String> getAttributesList() {
        return attributesList;
    }
}
