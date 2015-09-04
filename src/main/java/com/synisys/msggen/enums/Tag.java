package com.synisys.msggen.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Holds the supported tags names with attributes.
 * <br/>
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/5/15 <br/>
 * <b>Time:</b> 10:25 PM <br/>
 */
public enum Tag {

//    ALL_SUPPORTED_ATTRIBUTES("", Arrays.asList("alt", "name", "value", "label", "title", "header", "footer", "message", "itemLabel", "headerText", "footerText", "emptyMessage", "requiredMessage")),

    A                   ("a",               Arrays.asList("title")),
    IMG                 ("img",             Arrays.asList("alt", "title")),
    SPAN                ("span",            Arrays.asList("title")),

    H_OUTPUT_LABEL      ("h:outputLabel",   Arrays.asList("value")),
    H_OUTPUT_TEXT       ("h:outputText",    Arrays.asList("value", "title")),
    H_INPUT_TEXT        ("h:inputText",     Arrays.asList("alt", "title", "label", "requiredMessage", "validatorMessage")),
    P_INPUT_TEXT        ("p:inputText",     Arrays.asList("alt", "title", "label", "requiredMessage", "validatorMessage")),
    H_INPUT_TEXT_AREA   ("h:inputTextarea", Arrays.asList("title", "label", "requiredMessage", "validatorMessage")),
    P_INPUT_TEXT_AREA   ("p:inputTextarea", Arrays.asList("title", "label", "requiredMessage", "validatorMessage")),
    P_SELECT_ONE_MENU   ("p:selectOneMenu", Arrays.asList("requiredMessage")),
    P_CALENDAR          ("p:calendar",      Arrays.asList("requiredMessage")),
    H_COLUMN            ("h:column",        Arrays.asList("headerText", "footerText")),
    P_COLUMN            ("p:column",        Arrays.asList("headerText", "footerText")),
    F_SELECT_ITEM       ("f:selectItem",    Arrays.asList("itemLabel")),
    F_SELECT_ITEMS      ("f:selectItems",   Arrays.asList("itemLabel")),
    P_DIALOG            ("p:dialog",        Arrays.asList("header", "footer")),
    P_CONFIRM_DIALOG    ("p:confirmDialog", Arrays.asList("header", "message")),
    DE_SUB_SECTION      ("de:subSection",   Arrays.asList("name", "title")),
    H_DATA_TABLE        ("h:dataTable",     Arrays.asList("emptyMessage")),
    P_DATA_TABLE        ("p:dataTable",     Arrays.asList("emptyMessage")),
    H_COMMAND_BUTTON    ("h:commandButton", Arrays.asList("value")),
    P_COMMAND_BUTTON    ("p:commandButton", Arrays.asList("value")),
    H_GRAPHIC_IMAGE     ("h:graphicImage",  Arrays.asList("alt", "title")),
    P_GRAPHIC_IMAGE     ("p:graphicImage",  Arrays.asList("alt", "title")),
    F_ATTRIBUTE         ("f:attribute",     Arrays.asList("value")),
    P_MENU_BUTTON       ("p:menuButton",    Arrays.asList("value")),
    UI_PARAM            ("ui:param",        Arrays.asList("value")),
    ;

    private String name;
    private List<String> attributesList;

    Tag(String name, List<String> attributesList) {
        this.name = name;
        this.attributesList = attributesList;
    }

    public String getName() {
        return name;
    }
    public List<String> getAttributesList() {
        return attributesList;
    }
}
