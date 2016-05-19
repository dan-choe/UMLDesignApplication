package saf.settings;

/**
 * This class provides a set of constants useful for setting up the app.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class AppStartupConstants
{
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static final String SIMPLE_APP_PROPERTIES_FILE_NAME = "simple_app_properties.xml";
    public static final String WORKSPACE_PROPERTIES_FILE_NAME = "workspace_properties.xml";
    public static final String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static final String FILE_PROTOCOL = "file:";
    public static final String PATH_DATA = "./data/";
    public static final String PATH_WORK = "./work/";
    public static final String PATH_IMAGES = "./images/";
    public static final String PATH_EMPTY = ".";
  
    // ERRO MESSAGE ASSOCIATED WITH PROPERTIES FILE LOADING ERRORS
    public static String PROPERTIES_FILE_ERROR_MESSAGE = "Error Loading properties.xml";

    // ERROR DIALOG CONTROL
    public static String CLOSE_BUTTON_LABEL = "Close";
}