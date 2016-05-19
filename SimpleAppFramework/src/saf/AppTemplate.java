package saf;

import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppGUI;
import saf.components.AppWorkspaceComponent;
import saf.components.AppFileComponent;
import saf.components.AppDataComponent;
import saf.components.AppComponentsBuilder;
import java.net.URL;
import javafx.application.Application;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static saf.settings.AppPropertyType.APP_CSS;
import static saf.settings.AppPropertyType.APP_PATH_CSS;
import static saf.settings.AppPropertyType.APP_TITLE;
import static saf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.PROPERTIES_LOAD_ERROR_TITLE;
import static saf.settings.AppStartupConstants.PATH_DATA;
import static saf.settings.AppStartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import static saf.settings.AppStartupConstants.SIMPLE_APP_PROPERTIES_FILE_NAME;
import static saf.settings.AppStartupConstants.WORKSPACE_PROPERTIES_FILE_NAME;
import saf.ui.AppMethodMessageDialog;
import saf.ui.AppWarningMessageDialogSingleton;
import saf.ui.AppVariableMessageDialog;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * This is the framework's JavaFX application. It provides the start method
 * that begins the program initialization, including the setup of all file
 * toolbar controls and a Scall to initialize the customly provided workspace
 * component.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public abstract class AppTemplate extends Application {

    // THIS CLASS USES A COMPONENT HIERARCHY DESIGN PATTERN, MEANING IT
    // HAS OBJECTS THAT CAN BE SWAPPED OUT FOR OTHER COMPONENTS
    
    // FIRST THERE IS THE COMPONENT FOR MANAGING CUSTOM APP DATA
    AppDataComponent dataComponent;
    
    // THEN THE COMPONENT FOR MANAGING CUSTOM FILE I/O
    AppFileComponent fileComponent;

    // AND THEN THE COMPONENT FOR THE GUI WORKSPACE
    AppWorkspaceComponent workspaceComponent;
    
    // THIS IS THE APP'S FULL JavaFX GUI. NOTE THAT ALL APPS WOULD
    // SHARE A COMMON UI EXCEPT FOR THE CUSTOM WORKSPACE
    AppGUI gui;
    
    // THIS METHOD MUST BE OVERRIDDEN WHERE THE CUSTOM BUILDER OBJECT
    // WILL PROVIDE THE CUSTOM APP COMPONENTS
    public abstract AppComponentsBuilder makeAppBuilderHook();
    
    // COMPONENT ACCESSOR METHODS
    public AppDataComponent getDataComponent() { return dataComponent; }
    public AppFileComponent getFileComponent() { return fileComponent; }
    public AppWorkspaceComponent getWorkspaceComponent() { return workspaceComponent; }
    public AppGUI getGUI() { return gui; }

    /**
     * This is where our Application begins its initialization, it will create
     * the WPM_GUI and initialize all of its components.
     *
     * @param primaryStage This application's window.
     */
    @Override
    public void start(Stage primaryStage) {
	// LET'S START BY INITIALIZING OUR DIALOGS
	AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
	messageDialog.init(primaryStage);
        //////////////////////////////////////////////////////////////
        AppWarningMessageDialogSingleton warningDialog = AppWarningMessageDialogSingleton.getSingleton();
	warningDialog.init(primaryStage);
        
        AppVariableMessageDialog variableDialog = AppVariableMessageDialog.getSingleton();
        variableDialog.init(primaryStage);
        
        AppMethodMessageDialog methodDialog = AppMethodMessageDialog.getSingleton();
        methodDialog.init(primaryStage);
        //////////////////////////////////////////////////////////////
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
	yesNoDialog.init(primaryStage);
	
	PropertiesManager props = PropertiesManager.getPropertiesManager();

	try {
	    // LOAD APP PROPERTIES, BOTH THE BASIC UI STUFF FOR THE FRAMEWORK
	    // AND THE CUSTOM UI STUFF FOR THE WORKSPACE
	    boolean success = loadProperties(SIMPLE_APP_PROPERTIES_FILE_NAME)
		    && loadProperties(WORKSPACE_PROPERTIES_FILE_NAME);
	    
	    if (success) {
		String appTitle = props.getProperty(APP_TITLE);

		// GET THE CUSTOM BUILDER, AND USE IT TO INIT THE COMPONENTS
		AppComponentsBuilder builder = makeAppBuilderHook();
		fileComponent = builder.buildFileComponent();
		dataComponent = builder.buildDataComponent();

		// AND NOW THAT THE COMPONENTS HAVE BEEN INSTANTIATED
		// WE CAN INITIALIZE THE GUI
		gui = new AppGUI(primaryStage, appTitle, this);
		workspaceComponent = builder.buildWorkspaceComponent();
		
		// NOW INIT ALL THE STYLE
		initStylesheet();
		gui.initStyle();
		workspaceComponent.initStyle();
	    } 
	}catch (Exception e/*IOException ioe*/) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
	}
    }
    
    /**
     * This function sets up the stylesheet to be used for specifying all
     * style for this application. Note that it does not attach CSS style
     * classes to controls, that must be done separately.
     */
    public void initStylesheet() {
	// SELECT THE STYLESHEET
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
	URL stylesheetURL = getClass().getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
	getGUI().getPrimaryScene().getStylesheets().add(stylesheetPath);	
    }

    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @param propertiesFileName The XML file containing properties to be
     * loaded in order to initialize the UI.
     * 
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties(String propertiesFileName) {
	    PropertiesManager props = PropertiesManager.getPropertiesManager();
	try {
	    // LOAD THE SETTINGS FOR STARTING THE APP
	    props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
	    props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
	    return true;
	} catch (InvalidXMLFileFormatException ixmlffe) {
	    // SOMETHING WENT WRONG INITIALIZING THE XML FILE
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
	    return false;
	}
    }
}
