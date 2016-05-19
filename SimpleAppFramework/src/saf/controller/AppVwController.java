package saf.controller;

import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppGUI;
import saf.components.AppFileComponent;
import saf.components.AppDataComponent;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppPropertyType.NEW_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.NEW_COMPLETED_TITLE;
import static saf.settings.AppPropertyType.NEW_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.NEW_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static saf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 * This class provides the event programmed responses for the file controls
 * that are provided by this framework.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class AppVwController {
    // HERE'S THE APP
    AppTemplate app;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    boolean saved;
    
    // THIS IS THE FILE FOR THE WORK CURRENTLY BEING WORKED ON
    File currentWorkFile;

    /**
     * This constructor just keeps the app for later.
     * 
     * @param initApp The application within which this controller
     * will provide file toolbar responses.
     */
    public AppVwController(AppTemplate initApp) {
        // NOTHING YET
        saved = true;
        app = initApp;
    }
    
    /**
     * This method marks the appropriate variable such that we know
     * that the current Work has been edited since it's been saved.
     * The UI is then updated to reflect this.
     * 
     * @param gui The user interface editing the Work.
     */
    public void markAsEdited(AppGUI gui) {
        // THE WORK IS NOW DIRTY
        saved = false;
        
        // LET THE UI KNOW
        gui.updateToolbarControls(saved);
    }

    public void handleZoomInRequest() {
    System.out.println("saf.controller.AppEditController.handleZoomInRequest()");
    }
    
    public void handleZoomOutRequest() {
        System.out.println("saf.controller.AppEditController.handleZoomOutRequest()");
    }
    
    public void handleGridRequest() {
        System.out.println("saf.controller.AppEditController.handleGridRequest()");
    }
    
    public void handleSnapRequest() {
        System.out.println("saf.controller.AppEditController.handleSnapRequest()");
    }
}
