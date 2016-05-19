package jcd.controller;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;
import jcd.data.DataManager;
import jcd.data.MakerState;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 * This class responds to interactions with other UI pose editing controls.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class JCDEditController {
    AppTemplate app;
        
    DataManager dataManager;
    
    public JCDEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
    }
    
    public void processSelectSelectionTool() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT); 
	
	// CHANGE THE STATE
	dataManager.setState(MakerState.SELECTING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processRemoveSelectedShape() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.removeSelectedShape();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processSelectRectangleToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MakerState.STARTING_CLASS);

	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processSelectEllipseToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(MakerState.STARTING_INTERFACE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processMoveSelectedShapeToBack() {
	dataManager.moveSelectedShapeToBack();
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processMoveSelectedShapeToFront() {
	dataManager.moveSelectedShapeToFront();
	app.getGUI().updateToolbarControls(false);
    }
        
    public void processSelectFillColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getFillColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentFillColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    public void processSelectOutlineColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getOutlineColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentOutlineColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}    
    }
    
    public void processSelectBackgroundColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setBackgroundColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    public void processSelectOutlineThickness() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processSnapshot() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("Pose.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }
}
