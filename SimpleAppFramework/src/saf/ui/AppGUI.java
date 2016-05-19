package saf.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import saf.controller.AppFileController;
import saf.controller.AppEditController;
import saf.controller.AppVwController;
import saf.AppTemplate;
import static saf.settings.AppPropertyType.*;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import saf.components.AppStyleArbiter;

/**
 * This class provides the basic user interface for this application,
 * including all the file controls, but not including the workspace,
 * which would be customly provided for each app.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class AppGUI implements AppStyleArbiter {
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    protected AppFileController fileController;
    protected AppEditController editController;
    protected AppVwController vwController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI
    protected BorderPane appPane;
    protected FlowPane toolbarPane;
    
    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    protected FlowPane fileToolbarPane;
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button saveAsButton;
    protected Button exitButton;
    protected Button photoButton;
    protected Button codeButton;
    
    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    protected FlowPane editToolbarPane;
    protected Button selectButton;
    protected Button resizeButton;
    protected Button addClassButton;
    protected Button addInterfaceButton;
    protected Button removeButton;
    protected Button undoButton;
    protected Button redoButton;
    
    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    protected FlowPane viewToolbarPane;
    protected Button zoomInButton;
    protected Button zoomOutButton;
    protected CheckBox gridButton;
    protected CheckBox snapButton;
    
    // HERE ARE OUR DIALOGS
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    protected String appTitle;
    
    /**
     * This constructor initializes the file toolbar for use.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param initAppTitle The title of this application, which
     * will appear in the window bar.
     * 
     * @param app The app within this gui is used.
     */
    public AppGUI(  Stage initPrimaryStage, 
		    String initAppTitle, 
		    AppTemplate app){
	// SAVE THESE FOR LATER
	primaryStage = initPrimaryStage;
	appTitle = initAppTitle;
        toolbarPane = new FlowPane();     
        // INIT THE TOOLBAR
        initFileToolbar(app);
        initEditToolbar(app);
        initVwToolbar(app);
		
        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();
    }
    
    /**
     * Accessor method for getting the application pane, within which all
     * user interface controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() { return appPane; }
    
    /**
     * Accessor method for getting this application's primary stage's,
     * scene.
     * 
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() { return primaryScene; }
    
    /**
     * Accessor method for getting this application's window,
     * which is the primary stage within which the full GUI will be placed.
     * 
     * @return This application's primary stage (i.e. window).
     */    
    public Stage getWindow() { return primaryStage; }

    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateEditToolbarControls(String st) {
        
        String status = st;
        
        //System.out.println("2" + status);
        
        if(status.equals("edited")){
            addClassButton.setDisable(false);
            addInterfaceButton.setDisable(false);
            saveButton.setDisable(false);
            saveAsButton.setDisable(false);
            photoButton.setDisable(false);
            codeButton.setDisable(false);
            selectButton.setDisable(false);
            resizeButton.setDisable(false);
            removeButton.setDisable(false);
        }else if(status.equals("saved")){
            saveButton.setDisable(true);
            saveAsButton.setDisable(true);
            photoButton.setDisable(true);
            codeButton.setDisable(true);
        }else if(status.equals("new")){
            selectButton.setDisable(false);
            addClassButton.setDisable(false);
            addInterfaceButton.setDisable(false);
            saveButton.setDisable(true);
            saveAsButton.setDisable(true);
            photoButton.setDisable(true);
            codeButton.setDisable(true);
        }else if(status.equals("select")){
            removeButton.setDisable(false);
            resizeButton.setDisable(false);
            addClassButton.setDisable(false);
            addInterfaceButton.setDisable(false);
            saveButton.setDisable(false);
            saveAsButton.setDisable(false);
            photoButton.setDisable(false);
            codeButton.setDisable(false);
        }else if(status.equals("resize")){
            addClassButton.setDisable(false);
            addInterfaceButton.setDisable(false);
            saveButton.setDisable(false);
            saveAsButton.setDisable(false);
            photoButton.setDisable(false);
            codeButton.setDisable(false);
        }

	
	
 
        
//        // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
//    protected FlowPane editToolbarPane;
//    protected Button selectButton;
//    protected Button resizeButton;
//    protected Button addClassButton;
//    protected Button addInterfaceButton;
//    protected Button removeButton;
//    protected Button undoButton;
//    protected Button redoButton;
//    
        

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }
    
    public void updateToolbarControls(boolean saved) {
        
        //System.out.println("1");
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);
        saveAsButton.setDisable(saved);
        photoButton.setDisable(false);
        codeButton.setDisable(false);
        
        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
	newButton.setDisable(false);
        loadButton.setDisable(false);
	exitButton.setDisable(false);
        
        //updateEditToolbarControls("new");

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /****************************************************************************/
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /****************************************************************************/
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar(AppTemplate app) {
        fileToolbarPane = new FlowPane();
        fileToolbarPane.setHgap(4);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbarPane,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadButton = initChildButton(fileToolbarPane,	LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveButton = initChildButton(fileToolbarPane,	SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        saveAsButton = initChildButton(fileToolbarPane,	SAVEAS_ICON.toString(),	    SAVEAS_TOOLTIP.toString(),	true);
        photoButton = initChildButton(fileToolbarPane,	PHOTO_ICON.toString(),	    PHOTO_TOOLTIP.toString(),	true);
        codeButton = initChildButton(fileToolbarPane,	CODE_ICON.toString(),	    CODE_TOOLTIP.toString(),	true);
        exitButton = initChildButton(fileToolbarPane,	EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);

	// AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        newButton.setOnAction(e -> {
            fileController.handleNewRequest();
            
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
           
        });
        saveButton.setOnAction(e -> {
            fileController.handleSaveRequest();
        });
        saveAsButton.setOnAction(e -> {
            fileController.handleSaveAsRequest();
        });
        photoButton.setOnAction(e -> {
            fileController.handlePhotoRequest();
        });
        codeButton.setOnAction(e -> {
            fileController.handleCodeRequest();
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });
        
        toolbarPane.getChildren().add(fileToolbarPane);
    }
    
    private void initEditToolbar(AppTemplate app) {
        editToolbarPane = new FlowPane();
        editToolbarPane.setHgap(4);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        selectButton = initChildButton(editToolbarPane,	SELECT_ICON.toString(),	    NEW_TOOLTIP.toString(),	true);
        resizeButton = initChildButton(editToolbarPane,	RESIZE_ICON.toString(),	    LOAD_TOOLTIP.toString(),	true);
        addClassButton = initChildButton(editToolbarPane,	ADDC_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        addInterfaceButton = initChildButton(editToolbarPane,	ADDI_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        removeButton = initChildButton(editToolbarPane,	REMOVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        undoButton = initChildButton(editToolbarPane,	UNDO_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        redoButton = initChildButton(editToolbarPane,	REDO_ICON.toString(),	    EXIT_TOOLTIP.toString(),	true);

	// AND NOW SETUP THEIR EVENT HANDLERS
        editController = new AppEditController(app);
        
        
        
        selectButton.setOnAction(e -> {
            editController.handleSelectRequest();
        });
        resizeButton.setOnAction(e -> {
            editController.handleResizeRequest();
        });
        addClassButton.setOnAction(e -> {
            editController.handleClassRequest();
        });
        addInterfaceButton.setOnAction(e -> {
            editController.handleInterfaceRequest();
        });
        removeButton.setOnAction(e -> {
            editController.handleRemoveRequest();
        });
        undoButton.setOnAction(e -> {
            editController.handleUndoRequest();
        });
        redoButton.setOnAction(e -> {
            editController.handleRedoRequest();
        });
        
        toolbarPane.getChildren().add(editToolbarPane);
    }
    
    
    private void initVwToolbar(AppTemplate app) {
        viewToolbarPane = new FlowPane();
        viewToolbarPane.setHgap(4);
         
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        zoomInButton = initChildButton(viewToolbarPane,	ZOOMIN_ICON.toString(),	    NEW_TOOLTIP.toString(),	true);
        zoomOutButton = initChildButton(viewToolbarPane,	ZOOMOUT_ICON.toString(),	    LOAD_TOOLTIP.toString(),	true);
        gridButton = new CheckBox("Grid");
        snapButton = new CheckBox("Snap");
        gridButton.setDisable(true);
        snapButton.setDisable(true);

	// AND NOW SETUP THEIR EVENT HANDLERS
        vwController = new AppVwController(app);
        zoomInButton.setOnAction(e -> {
            vwController.handleZoomInRequest();
        });
        zoomOutButton.setOnAction(e -> {
            vwController.handleZoomOutRequest();
        });
        gridButton.setOnAction(e -> {
            vwController.handleGridRequest();
        });
        snapButton.setOnAction(e -> {
            vwController.handleSnapRequest();
        });
        
        viewToolbarPane.getChildren().addAll(gridButton,snapButton);
        toolbarPane.getChildren().add(viewToolbarPane);
    }
    
   

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);

        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(toolbarPane);
        
        primaryScene = new Scene(appPane);
        
        // SET THE APP ICON
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW AND OPEN THE WINDOW
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
    
    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    /**
     * This function specifies the CSS style classes for the controls managed
     * by this framework.
     */
    @Override
    public void initStyle() {
	toolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        fileToolbarPane.getStyleClass().add(CLASS_EDIT_PANE);
        editToolbarPane.getStyleClass().add(CLASS_EDIT_PANE);
        viewToolbarPane.getStyleClass().add(CLASS_VIEW_PANE);
        
	newButton.getStyleClass().add(CLASS_FILE_BUTTON);
	loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
	saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
	exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
}
