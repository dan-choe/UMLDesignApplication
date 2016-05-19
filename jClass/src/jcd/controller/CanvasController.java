package jcd.controller;


import jcd.data.DataManager;
import jcd.data.MakerState;
import static jcd.data.MakerState.*;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 * This class responds to interactions with the rendering surface.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class CanvasController {
    AppTemplate app;
    
    public CanvasController(AppTemplate initApp) {
	app = initApp;
    }
    
    public void processCanvasMouseExited(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
        /*
	if (dataManager.isInState(MakerState.DRAGGING_SHAPE)) {
	    
	}
	else if (dataManager.isInState(MakerState.SIZING_SHAPE)) {
	    
	}
        */
    }
    
    public void processCanvasMousePress(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(SELECTING_SHAPE)) {
	    // SELECT THE TOP SHAPE
//	  
//	    // AND START DRAGGING IT
//	    if (shape != null) {
//		scene.setCursor(Cursor.MOVE);
//		dataManager.setState(PoseMakerState.DRAGGING_SHAPE);
//		app.getGUI().updateToolbarControls(false);
//	    }
//	    else {
//		scene.setCursor(Cursor.DEFAULT);
//		dataManager.setState(DRAGGING_NOTHING);
//		app.getWorkspaceComponent().reloadWorkspace();
//	    }
	}
	else if (dataManager.isInState(MakerState.STARTING_CLASS)) {
	    //dataManager.startNewClass(x, y);
	}
	else if (dataManager.isInState(MakerState.STARTING_INTERFACE)) {
	    //dataManager.startNewEllipse(x, y);
	}
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
       // System.out.println("processCanvasMousePress : "+ dataManager.getState());
    }
    
    public void processCanvasMouseMoved(int x, int y) {
        DataManager dataManager = (DataManager)app.getDataComponent();
        dataManager.setCurrentPoints(x, y);
	//Workspace workspace = (Workspace)app.getWorkspaceComponent();
	//workspace.setDebugText("(" + x + "," + y + ")");
        
        //System.err.println("(" + x + "," + y + ")");
    }
    
    public void processCanvasMouseDragged(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
        dataManager.setCurrentPoints(x, y);
        
	if (dataManager.isInState(RESIZING_SHAPE)) {
            dataManager.setCurrentPoints(x, y);
	    //Draggable newDraggableShape = (Draggable)dataManager.getNewShape();
	    //newDraggableShape.size(x, y);
            //System.err.println("processCanvasMouseDragged : "+ SIZING_SHAPE);
	}
        /*
	else if (dataManager.isInState(DRAGGING_SHAPE)) {
	    //Draggable selectedDraggableShape = (Draggable)dataManager.getSelectedShape();
	    //selectedDraggableShape.drag(x, y);
	    //app.getGUI().updateToolbarControls(false);
            //System.err.println(DRAGGING_SHAPE +"  " + x +"  " + y);
	}
        */
        
    }
    
    public void processCanvasMouseRelease(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(RESIZING_SHAPE)) {
	    dataManager.selectSizedShape();
	    app.getGUI().updateToolbarControls(false);
	}
        /*
	else if (dataManager.isInState(MakerState.DRAGGING_SHAPE)) {
	    dataManager.setState(SELECTING_SHAPE);
	    Scene scene = app.getGUI().getPrimaryScene();
	    scene.setCursor(Cursor.DEFAULT);
	    app.getGUI().updateToolbarControls(false);
	}
	else if (dataManager.isInState(MakerState.DRAGGING_NOTHING)) {
	    dataManager.setState(SELECTING_SHAPE);
	}
        */
        System.err.println("processCanvasMouseRelease : "+ dataManager.getState());
    }
}
