package saf.components;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * This abstract class provides the structure for workspace components in
 * our applications. Note that by doing so we make it possible
 * for customly provided descendent classes to have their methods
 * called from this framework.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public abstract class AppWorkspaceComponent implements AppStyleArbiter {
    // THIS IS THE WORKSPACE WHICH WILL BE DIFFERENT
    // DEPENDING ON THE CUSTOM APP USING THIS FRAMEWORK
    protected Pane workspace;
    
    // THIS DENOTES THAT THE USER HAS BEGUN WORKING AND
    // SO THE WORKSPACE IS VISIBLE AND USABLE
    protected boolean workspaceActivated;
    
    /**
     * When called this function puts the workspace into the window,
     * revealing the controls for editing work.
     * 
     * @param appPane The pane that contains all the controls in the
     * entire application, including the file toolbar controls, which
     * this framework manages, as well as the customly provided workspace,
     * which would be different for each app.
     */
    public void activateWorkspace(BorderPane appPane) {
        if (!workspaceActivated) {
            // PUT THE WORKSPACE IN THE GUI
            appPane.setCenter(workspace);
            workspaceActivated = true;
        }
    }
    
    /**
     * Mutator method for setting the custom workspace.
     * 
     * @param initWorkspace The workspace to set as the user
     * interface's workspace.
     */
    public void setWorkspace(Pane initWorkspace) { 
	workspace = initWorkspace; 
    }
    
    /**
     * Accessor method for getting the workspace.
     * 
     * @return The workspace pane for this app.
     */
    public Pane getWorkspace() { return workspace; }
    
    // THE DEFINITION OF THIS CLASS SHOULD BE PROVIDED
    // BY THE CONCRETE WORKSPACE
    public abstract void reloadWorkspace();
    
    public abstract void addClassWorkspace();

    public abstract void selectWorkspace();
    
    public abstract void loadSelectClassWorkspace();

    public abstract void addInterfaceWorkspace();

    public abstract void resizeWorkspace();

    public abstract void removeWorkspace();
    public abstract void undoWorkspace();

    public abstract void redoWorkspace();
    public abstract void photoExport();
}
