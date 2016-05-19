/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AppEditController {
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
    public AppEditController(AppTemplate initApp) {
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

    public void handleSelectRequest() {
        //System.out.println("saf.controller.AppEditController.handleSelectRequest()");
        app.getWorkspaceComponent().selectWorkspace();
        app.getGUI().updateEditToolbarControls("select");
    }
    
    public void handleResizeRequest() {
      //  System.out.println("saf.controller.AppEditController.handleResizeRequest()");
        app.getWorkspaceComponent().resizeWorkspace();
        app.getGUI().updateEditToolbarControls("resize");
    }
    
    public void handleClassRequest() {
       // System.out.println("saf.controller.AppEditController.handleClassRequest()");
        app.getWorkspaceComponent().addClassWorkspace();
        app.getGUI().updateEditToolbarControls("edited");
       //app.getWorkspaceComponent().reloadWorkspace();	
    }
    
    public void handleInterfaceRequest() {
       // System.out.println("saf.controller.AppEditController.handleInterfaceRequest()");
        
        app.getWorkspaceComponent().addInterfaceWorkspace();
        app.getGUI().updateEditToolbarControls("edited");
    }
    
    public void handleRemoveRequest() {
       // System.out.println("saf.controller.AppEditController.handleRemoveRequest()");
        app.getWorkspaceComponent().removeWorkspace();
        app.getGUI().updateEditToolbarControls("edited");
    }
    
    public void handleUndoRequest() {
       // System.out.println("saf.controller.AppEditController.handleUndoRequest()");
        app.getWorkspaceComponent().undoWorkspace();
        app.getGUI().updateEditToolbarControls("edited");
    }
    
    public void handleRedoRequest() {
       // System.out.println("saf.controller.AppEditController.handleRedoRequest()");
        app.getWorkspaceComponent().redoWorkspace();
        app.getGUI().updateEditToolbarControls("edited");
    }

}
