package saf.components;

/**
 * This interface serves as a family of type that will initialize
 * the style for some set of controls, like the workspace, for example.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public interface AppStyleArbiter {
    // THESE ARE COMMON STYLE CLASSES WE'LL USE
    public static final String CLASS_BORDERED_PANE = "bordered_pane";
    public static final String CLASS_EDIT_PANE = "edit_pane";
    public static final String CLASS_VIEW_PANE = "view_pane";
    
    public static final String CLASS_HEADING_LABEL = "heading_label";
    public static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    public static final String CLASS_PROMPT_LABEL = "prompt_label";
    public static final String CLASS_PROMPT_TEXT_FIELD = "prompt_text_field";
    public static final String CLASS_FILE_BUTTON = "file_button";
    
    public void initStyle();
}
