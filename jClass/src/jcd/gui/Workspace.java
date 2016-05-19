package jcd.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import static jcd.PropertyType.SELECTION_TOOL_TOOLTIP;
import static jcd.PropertyType.PLUS_ICON;
import static jcd.PropertyType.MINUS_ICON;
import jcd.controller.CanvasController;
import jcd.controller.JCDEditController;
import jcd.data.DataManager;
import jcd.data.DraggableClass;
import jcd.data.DraggableInterface;
import jcd.data.Method;
import jcd.data.MakerState;
import jcd.data.Variable;
import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import saf.ui.AppVariableMessageDialog;
import saf.ui.AppWarningMessageDialogSingleton;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_MAX_PANE = "max_pane";
    static final String CLASS_RENDER_CANVAS = "render_canvas";
    static final String CLASS_BUTTON = "button";
    static final String CLASS_EDIT_TOOLBAR = "edit_toolbar";
    static final String CLASS_EDIT_TOOLBAR_ROW = "edit_toolbar_row";
    //static final String CLASS_COLOR_CHOOSER_PANE = "color_chooser_pane";
    //static final String CLASS_COLOR_CHOOSER_CONTROL = "color_chooser_control";
    static final String EMPTY_TEXT = "";
    static final int BUTTON_TAG_WIDTH = 75;
    
    
    static final String TABLE_NAME = "Name";
    static final String TABLE_ACCESS = "Access";
    static final String TABLE_TYPE = "Type";
    static final String TABLE_STATIC = "Static";
    static final String TABLE_FINAL = "Final";
    static final String TABLE_ABSTRACT = "Abstract";
    
    static final String TABLE_RETURN = "Return";
    static final String TABLE_TYPE1 = "Type1";
    static final String TABLE_ARG1 = "Arg1";
    static final String TABLE_TYPE2 = "Type2";
    static final String TABLE_ARG2 = "Arg2";
    static final String TABLE_TYPE3 = "Type3";
    static final String TABLE_ARG3 = "Arg3";
    
    
    
    
    //ArrayList<Variable> tempVariables = new ArrayList<>();
    

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    
    Label classLabel, packageLabel, parentLabel, implementsLabel, variableLabel, methodLabel;
    // FIRST ROW
    VBox row1Box;
    HBox classInfo, packageInfo, parentInfo, implementInfo;
    TextField classText, packageText;
    ComboBox parents, implementsComboBox;
    
    
    Button selectionToolButton;
    Button removeButton;
    Button rectButton;
    Button ellipseButton;
    
    // SECOND ROW
    VBox row2Box;
    TableView<Variable> tableVariable;
    Button addVariableButton;
    Button removeVariableButton;
   
    TableColumn itemVariableNameColumn;
    TableColumn itemVariableTypeColumn;
    TableColumn itemVariableAccessColumn;
    TableColumn itemVariableStaticColumn;
    TableColumn itemVariableAbstractColumn;
    TableColumn itemVariableFinalColumn;
    
    // THIRD ROW
    VBox row3Box;
    TableView<Method> tableMethod;
    Button addMethodButton;
    Button removeMethodButton;
    
    TableColumn itemMethodNameColumn;
    TableColumn itemMethodReturnColumn;
    TableColumn itemMethodAccessColumn;
    TableColumn itemMethodStaticColumn;
    TableColumn itemMethodAbstractColumn;
    TableColumn itemMethodFinalColumn;
    
    
    TableColumn itemMethodType1Column;
    TableColumn itemMethodArg1Column;
    TableColumn itemMethodType2Column;
    TableColumn itemMethodArg2Column;
    TableColumn itemMethodType3Column;
    TableColumn itemMethodArg3Column;
    
    
    
   // Label backgroundColorLabel;
   // ColorPicker backgroundColorPicker;

    // FORTH ROW
    VBox row4Box;
    Label fillColorLabel;
    ColorPicker fillColorPicker;
    
    // FIFTH ROW
    VBox row5Box;
    Label outlineColorLabel;
    ColorPicker outlineColorPicker;
        
    // SIXTH ROW
    VBox row6Box;
    Label outlineThicknessLabel;
    Slider outlineThicknessSlider;
    
    // SEVENTH ROW
    HBox row7Box;
    Button snapshotButton;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING
    Pane canvas;
    ScrollPane wrapCanvas;
    
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    JCDEditController jcdEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();

	layoutGUI();
	setupHandlers();
    }
    
    public ColorPicker getFillColorPicker() {
	return null;
    }
    
    public ColorPicker getOutlineColorPicker() {
	return null;
    }
    
    public ColorPicker getBackgroundColorPicker() {
	return null;
    }
    
    public Slider getOutlineThicknessSlider() {
	return null;
    }
    
    private void layoutGUI() {
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox();

	// ROW 1
	//row1Box = new HBox();
        row1Box = new VBox();
       // row1Box.setMinSize(300, 300);
        row1Box.setAlignment(Pos.CENTER);
        row1Box.setPadding(new Insets(0, 10, 0, 10));
        
        
       
        classInfo = new HBox();
        packageInfo = new HBox();
        parentInfo = new HBox();
        implementInfo = new HBox();
        
        
        //row1Box.setHgap(5);
       // row1Box.setVgap(5);
       // row1Box.setGridLinesVisible(true);

       // ColumnConstraints column1 = new ColumnConstraints(100);
       // ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
       // column2.setHgrow(Priority.ALWAYS);
       // row1Box.getColumnConstraints().addAll(column1, column2);
        
       // RowConstraints row = new RowConstraints(20);
       // row1Box.getRowConstraints().add(row);

        classLabel = new Label("Class Name: ");
        classLabel.setMinWidth(200);
        classText = new TextField();
        packageLabel = new Label("Package Name: ");
        packageLabel.setMinWidth(200);
        packageText = new TextField();
        parentLabel = new Label("Parent: ");
        parentLabel.setMinWidth(200);
        parents = new ComboBox();
        parents.setMinWidth(150);
        
        implementsLabel = new Label("Implements: ");
        implementsLabel.setMinWidth(200);
        implementsComboBox = new ComboBox();
        implementsComboBox.setMinWidth(150);
        
        classInfo.getChildren().addAll(classLabel, classText);
        packageInfo.getChildren().addAll(packageLabel, packageText);
        parentInfo.getChildren().addAll(parentLabel, parents);
        implementInfo.getChildren().addAll(implementsLabel, implementsComboBox);

        
        row1Box.getChildren().addAll(classInfo,packageInfo,parentInfo,implementInfo);
        
        
        
	//selectionToolButton = gui.initChildButton(row1Box, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), true);
	//removeButton = gui.initChildButton(row1Box, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
	//rectButton = gui.initChildButton(row1Box, RECTANGLE_ICON.toString(), RECTANGLE_TOOLTIP.toString(), false);
	//ellipseButton = gui.initChildButton(row1Box, ELLIPSE_ICON.toString(), ELLIPSE_TOOLTIP.toString(), false);

	// ROW 2
	row2Box = new VBox();
        HBox headVariable = new HBox();
        
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefViewportWidth(300);
        scrollPane.setPrefViewportHeight(200);
        
        //Table for variable
        variableLabel = new Label("Variables: ");
        headVariable.getChildren().add(variableLabel);
        addVariableButton = gui.initChildButton(headVariable, PLUS_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        removeVariableButton = gui.initChildButton(headVariable, MINUS_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        
        
        tableVariable = new TableView();
//        tableVariable.setEditable(true);
//        TableColumn vName = new TableColumn("Name");
//        TableColumn vType = new TableColumn("Type");
//        TableColumn vStatic = new TableColumn("Static");
//        TableColumn vAccess = new TableColumn("Access");
//        tableVariable.getColumns().addAll(vName, vType, vStatic,vAccess);
        //table.setItems(data);
        
        // NOW SETUP THE TABLE COLUMNS
        itemVariableNameColumn = new TableColumn(TABLE_NAME);
        itemVariableTypeColumn = new TableColumn(TABLE_TYPE);
        itemVariableAccessColumn = new TableColumn(TABLE_ACCESS);
        //itemVariableAbstractColumn = new TableColumn(TABLE_ABSTRACT);
        itemVariableFinalColumn = new TableColumn(TABLE_FINAL);
        itemVariableStaticColumn = new TableColumn(TABLE_STATIC);
        
        // AND LINK THE COLUMNS TO THE DATA
        itemVariableNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnName"));
        itemVariableTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnType"));
        itemVariableAccessColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnAcess"));
       // itemVariableAbstractColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnAbstract"));
        itemVariableFinalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnFinal"));
        itemVariableStaticColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnStatic"));
        
        
        tableVariable.getColumns().add(itemVariableNameColumn);
        tableVariable.getColumns().add(itemVariableTypeColumn);
        tableVariable.getColumns().add(itemVariableAccessColumn);
        //tableVariable.getColumns().add(itemVariableAbstractColumn);
        tableVariable.getColumns().add(itemVariableFinalColumn);
        tableVariable.getColumns().add(itemVariableStaticColumn);
        
        
        //tableVariable.setItems(FXCollections.observableArrayList(tempVariables));
        
        
        scrollPane.setContent(tableVariable);
        row2Box.getChildren().addAll(headVariable, scrollPane);
        
	// ROW 3
	row3Box = new VBox();
        HBox headMethod = new HBox();

        ScrollPane mscrollPane = new ScrollPane();
        mscrollPane.setPrefViewportWidth(300);
        mscrollPane.setPrefViewportHeight(200);
        
        //Table for variable
        methodLabel = new Label("Methods: ");
        headMethod.getChildren().add(methodLabel);
        addMethodButton = gui.initChildButton(headMethod, PLUS_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        removeMethodButton = gui.initChildButton(headMethod, MINUS_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        
        tableMethod = new TableView();
        
        itemMethodNameColumn = new TableColumn(TABLE_NAME);
//        itemMethodTypeColumn = new TableColumn(TABLE_TYPE);
        itemMethodAccessColumn = new TableColumn(TABLE_ACCESS);
        itemMethodAbstractColumn = new TableColumn(TABLE_ABSTRACT);
        itemMethodStaticColumn = new TableColumn(TABLE_STATIC);
        itemMethodReturnColumn = new TableColumn(TABLE_RETURN);
        itemMethodFinalColumn = new TableColumn(TABLE_FINAL);
        
        itemMethodType1Column = new TableColumn(TABLE_TYPE1);
        itemMethodArg1Column = new TableColumn(TABLE_ARG1);
        itemMethodType2Column = new TableColumn(TABLE_TYPE2);
        itemMethodArg2Column = new TableColumn(TABLE_ARG2);
        itemMethodType3Column = new TableColumn(TABLE_TYPE3);
        itemMethodArg3Column = new TableColumn(TABLE_ARG3);
       
        // AND LINK THE COLUMNS TO THE DATA
        itemMethodNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnName"));
        itemMethodReturnColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnReturnType"));
        itemMethodAccessColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnAcess"));
        itemMethodAbstractColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnAbstract"));
        itemMethodStaticColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnStatic"));
        itemMethodFinalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("columnFinal"));
        
        itemMethodType1Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnType1"));
        itemMethodArg1Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnArg1"));
        itemMethodType2Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnType2"));
        itemMethodArg2Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnArg2"));
        itemMethodType3Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnType3"));
        itemMethodArg3Column.setCellValueFactory(new PropertyValueFactory<String, String>("columnArg3"));
        
        
        tableMethod.getColumns().add(itemMethodNameColumn);
        tableMethod.getColumns().add(itemMethodReturnColumn); 
        tableMethod.getColumns().add(itemMethodAccessColumn);
        tableMethod.getColumns().add(itemMethodAbstractColumn);
        tableMethod.getColumns().add(itemMethodStaticColumn);
        tableMethod.getColumns().add(itemMethodFinalColumn);
        
        tableMethod.getColumns().add(itemMethodType1Column);
        tableMethod.getColumns().add(itemMethodArg1Column);
        tableMethod.getColumns().add(itemMethodType2Column);
        tableMethod.getColumns().add(itemMethodArg2Column);
        tableMethod.getColumns().add(itemMethodType3Column);
        tableMethod.getColumns().add(itemMethodArg3Column);
        
        
        mscrollPane.setContent(tableMethod);
        row3Box.getChildren().addAll(headMethod, mscrollPane);
        
    
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);

	
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
        canvas.setMinSize(gui.getPrimaryScene().getWidth(), gui.getPrimaryScene().getHeight());
        
        //System.err.println(canvas.getMinWidth());
        
        //System.out.println(Region.USE_PREF_SIZE);
        //canvas.minHeight(gui.getPrimaryScene().getHeight());
        //canvas.minWidth(gui.getPrimaryScene().getWidth());
       // canvas.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        
        wrapCanvas = new ScrollPane(canvas);
        wrapCanvas.setPrefViewportWidth(gui.getPrimaryScene().getWidth());
        wrapCanvas.setPrefViewportHeight(gui.getPrimaryScene().getHeight());
        //wrapCanvas.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
        
        
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	DataManager data = (DataManager)app.getDataComponent();
        ArrayList<Node> observableData = new ArrayList<>();//(ArrayList<Node>) canvas.getChildren();
        
        for(int i=0; i<canvas.getChildren().size(); i++){
            observableData.add(canvas.getChildren().get(i));
        }
        
	data.setShapes(observableData);

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setRight(editToolbar);
	((BorderPane)workspace).setCenter(wrapCanvas);
        
       // editToolbar.setVisible(false);
        
        //System.out.println(gui.getPrimaryScene().getWidth());
       
       
     
        
        
    }
    
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    
    private void setupHandlers() {
	// MAKE THE EDIT CONTROLLER
	jcdEditController = new JCDEditController(app);
	
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
//	selectionToolButton.setOnAction(e->{
//	    poseEditController.processSelectSelectionTool();
//	});
//	removeButton.setOnAction(e->{
//	    poseEditController.processRemoveSelectedShape();
//	});
//	rectButton.setOnAction(e->{
//	    poseEditController.processSelectRectangleToDraw();
//	});
//	ellipseButton.setOnAction(e->{
//	    poseEditController.processSelectEllipseToDraw();
//	});
	/*
	moveToBackButton.setOnAction(e->{
	    poseEditController.processMoveSelectedShapeToBack();
	});
	moveToFrontButton.setOnAction(e->{
	    poseEditController.processMoveSelectedShapeToFront();
	});
        */
//
//	backgroundColorPicker.setOnAction(e->{
//	    poseEditController.processSelectBackgroundColor();
//	});
//	fillColorPicker.setOnAction(e->{ 
//	    poseEditController.processSelectFillColor();
//	});
//	outlineColorPicker.setOnAction(e->{
//	    poseEditController.processSelectOutlineColor();
//	});
//	outlineThicknessSlider.valueProperty().addListener(e-> {
//	    poseEditController.processSelectOutlineThickness();
//	});
//        
//	snapshotButton.setOnAction(e->{
//	    poseEditController.processSnapshot();
//	});
//	
	// MAKE THE CANVAS CONTROLLER	
	canvasController = new CanvasController(app);
        
        canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getSceneX(), (int)e.getSceneY());
           // System.out.println("setOnMouseDragged");
	});
        
        canvas.setOnMousePressed(e->{
            //DataManager dataManager = (DataManager)app.getDataComponent();
            //dataManager.unselectClass();
        
	    //canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
        
        canvas.setOnMouseMoved(e->{
	    canvasController.processCanvasMouseMoved((int)e.getSceneX(), (int)e.getSceneY());//(int)e.getX(), (int)e.getY());
            //System.out.println("setOnMouseMoved");
	});
        
        
        /*
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
            //System.out.println("setOnMouseReleased");
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
           // System.out.println("setOnMouseDragged");
	});
	canvas.setOnMouseExited(e->{
	    canvasController.processCanvasMouseExited((int)e.getX(), (int)e.getY());
           // System.out.println("setOnMouseExited");
	});
	canvas.setOnMouseMoved(e->{
	    canvasController.processCanvasMouseMoved((int)e.getX(), (int)e.getY());
            //System.out.println("setOnMouseMoved");
	});
        */
    }
    
    public Pane getCanvas() {
	return canvas;
    }
    
    public void setImage(ButtonBase button, String fileName) {
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + fileName;
        Image buttonImage = new Image(imagePath);
	
	// SET THE IMAGE IN THE BUTTON
        button.setGraphic(new ImageView(buttonImage));	
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
	
	// COLOR PICKER STYLE
	//fillColorPicker.getStyleClass().add(CLASS_BUTTON);
	//outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
	//backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);

        classLabel.getStyleClass().add("classLabel");
        packageLabel.getStyleClass().add("packageLabel");
        parentLabel.getStyleClass().add("parent");
    
//	backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	
	//row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	//row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	//row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	//row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
        //editToolbar.setVisible(false);
        try{
        //System.err.println("reload");
        resetComponent();
	DataManager dataManager = (DataManager)app.getDataComponent();
        
        ArrayList<Node> newLoadedShapes = dataManager.getShapes();
        canvas.getChildren().clear();
        
        for(int i=0; i<newLoadedShapes.size(); i++){
            canvas.getChildren().add(newLoadedShapes.get(i));
        }
        
        dataManager.drawLineExtends();
        dataManager.drawLineImplements();
        dataManager.drawLineAssociation();
        dataManager.drawLineAssociationFromMethod();
        
        for(int i=0; i<dataManager.getLines().size(); i++){
            canvas.getChildren().add(dataManager.getLines().get(i));
        }
        }catch(java.lang.IndexOutOfBoundsException e){
            System.out.println("IndexOutOfBoundsException");
        }
        
        
       // System.err.println("dataManager.getShapes().size() : " + dataManager.getShapes().size());
       // System.err.println("canvas.getChildren().size() : " + canvas.getChildren().size());
        //removeButton.setDisable(dataManager.getSelectedShape() == null);
	//backgroundColorPicker.setValue(dataManager.getBackgroundColor());
    }
    
     public void reloadWorkspace2() {
        //editToolbar.setVisible(false);
        try{
          //  System.err.println("reload2222");
           // resetComponent();
            DataManager dataManager = (DataManager)app.getDataComponent();

           ArrayList<Node> newLoadedShapes = dataManager.getShapes();
           canvas.getChildren().clear();

            for(int i=0; i<newLoadedShapes.size(); i++){
                canvas.getChildren().add(newLoadedShapes.get(i));
            }

           dataManager.drawLineExtends();
            dataManager.drawLineImplements();
           dataManager.drawLineAssociation();
           dataManager.drawLineAssociationFromMethod();
//
            for(int i=0; i<dataManager.getLines().size(); i++){
                canvas.getChildren().add(dataManager.getLines().get(i));
            }
        }catch(java.lang.IndexOutOfBoundsException e){
            //System.out.println("IndexOutOfBoundsException");
        }
        

    }
    
    public void loadSelectedShapeSettings(Node shape) {
	if (shape != null) {
	 //   Color fillColor = (Color)shape.getFill();
	 //   Color strokeColor = (Color)shape.getStroke();
	 //   double lineThickness = shape.getStrokeWidth();
//	    fillColorPicker.setValue(fillColor);
//	    outlineColorPicker.setValue(strokeColor);
//	    outlineThicknessSlider.setValue(lineThickness);	    
	}
    }

    
    /**
     * Add A NEW ClassBox 
     * 
     */
    @Override
    public void addClassWorkspace() {
        editToolbar.setVisible(true);
        DataManager dataManager = (DataManager)app.getDataComponent();
        
        
        dataManager.startNewClass((int) (Math.random()*50+1), (int) (Math.random()*50+1));
        
        //System.out.println("I'm gonna add class.");
    }
    
     /**
     * Add A NEW ClassBox 
     * 
     */
    @Override
    public void addInterfaceWorkspace() {
        editToolbar.setVisible(true);
        DataManager dataManager = (DataManager)app.getDataComponent();
        dataManager.startNewInterface((int) (Math.random()*50+1), (int) (Math.random()*200+1));
        
        //System.out.println("I'm gonna add class.");
    }
    
    @Override
    public void selectWorkspace() {
        
       DataManager dataManager = (DataManager)app.getDataComponent();
       dataManager.setState(MakerState.SELECTING_SHAPE);
        
      // System.out.println("I'm gonna select class.");
    }
    
    /**
     * Get information of selected shape.
     * 
     * 
     */
    
    
    @Override
    public void loadSelectClassWorkspace() {
        
       DataManager dataManager = (DataManager)app.getDataComponent();
       //dataManager.setState(PoseMakerState.SELECTING_SHAPE);
       
       if(dataManager.getSelectedShape() instanceof DraggableClass){
           DraggableClass temp = (DraggableClass) dataManager.getSelectedShape();
           classText.setText(temp.getTitle());
           classText.setOnAction(e ->{
               if(nameAvailableCheck(classText.getText())){
                   temp.setTitle(classText.getText());
                   dataManager.editSelectedShapeName(classText.getText());
               }else{
                   messagePopup("Warning","The Name is already used");
               }    
           });
           
           packageText.setText(temp.getPackageName());
           
           
           packageText.setOnAction(e ->{
               temp.setPackageName(packageText.getText());
           });
           
           parents.setValue(temp.getParentName());
           
           parents.setOnMousePressed(e ->{
                parents.getItems().clear();
                parentListUpdate(dataManager.getSelectedShapeName());
                ArrayList<String> parentValueList = parentListUpdate(dataManager.getSelectedShapeName());
                for(int i=0; i<parentValueList.size(); i++){
                    parents.getItems().add(parentValueList.get(i));
                }
            });
        
            parents.setOnAction(e ->{ 
             //   System.out.println("setOnAction " + parents.getValue());
            //    parents.getSelectionModel().clearSelection();
                 if(parents.getValue()!=null){
                    dataManager.editParent((String) parents.getValue());
                    reloadWorkspace2();
                 }else{
                     dataManager.editParent("");
                    reloadWorkspace2();
                 }
                 e.consume();
            });
            
            if(temp.getItsImplements().size()>0){
                implementsComboBox.setValue(temp.getItsImplements().get(0).getTitle());
            }else{
                 implementsComboBox.setValue("");
            }
           implementsComboBox.setOnMousePressed(e ->{
                implementsComboBox.getItems().clear();
                interfaceListUpdate(dataManager.getSelectedShapeName());
                ArrayList<String> parentValueList = interfaceListUpdate(dataManager.getSelectedShapeName());
                for(int i=0; i<parentValueList.size(); i++){
                    implementsComboBox.getItems().add(parentValueList.get(i));
                }
            });
        
            implementsComboBox.setOnAction(e ->{ 
             //   System.out.println("setOnAction " + parents.getValue());
            //    parents.getSelectionModel().clearSelection();
                 if(implementsComboBox.getValue()!=null){
                    dataManager.editImplement((String) implementsComboBox.getValue());
                    reloadWorkspace2();
                 }else{
                    dataManager.editImplement("");
                    reloadWorkspace2();
                 }
                 e.consume();
            });
            
            
            
            addVariableButton.setOnAction(e->{
                dataManager.addNewVariable();
            });
            
            removeVariableButton.setOnAction(e->{
                dataManager.removeSelectedVariable(tableVariable.getSelectionModel().getSelectedItem());
                reloadWorkspace();
            });
            
            addMethodButton.setOnAction(e->{
                dataManager.addNewMethod();
            });
            
            removeMethodButton.setOnAction(e->{
                dataManager.removeSelectedMethod(tableMethod.getSelectionModel().getSelectedItem());
                reloadWorkspace();
            });
            
            tableVariable.setItems(FXCollections.observableArrayList(temp.getVariables()));
            tableMethod.setItems(FXCollections.observableArrayList(temp.getMethods()));
           
       }
       else{
           DraggableInterface temp = (DraggableInterface) dataManager.getSelectedShape();
           classText.setText(temp.getTitle());
           classText.setOnAction(e ->{
               if(nameAvailableCheck(classText.getText())){
                   temp.setTitle(classText.getText());
                   dataManager.editSelectedShapeName(classText.getText());
               }else{
                    messagePopup("Warning","The Name is already used");
               }
           });
           packageText.setText(temp.getPackageName());
           packageText.setOnAction(e ->{
               temp.setPackageName(packageText.getText());
           });
           
           parents.setValue(temp.getParentName());
           
           parents.setOnMousePressed(e ->{
                parents.getItems().clear();
                parentListUpdate(dataManager.getSelectedShapeName());
                ArrayList<String> parentValueList = parentListUpdate(dataManager.getSelectedShapeName());
                for(int i=0; i<parentValueList.size(); i++){
                    parents.getItems().add(parentValueList.get(i));
                }
            });

            
            parents.setOnAction(e ->{ 
             //   System.out.println("setOnAction " + parents.getValue());
            //    parents.getSelectionModel().clearSelection();
                 if(parents.getValue()!=null){
                    dataManager.editParent((String) parents.getValue());
                    reloadWorkspace2();
                 }else{
                     dataManager.editParent("");
                    reloadWorkspace2();
                 }
                 e.consume();
            });
            
              implementsComboBox.setValue("");
            
           implementsComboBox.setOnMousePressed(e ->{
                implementsComboBox.setValue("");
            });
        
            implementsComboBox.setOnAction(e ->{ 
             implementsComboBox.setValue("");
            });
 
            addVariableButton.setOnAction(e->{
                dataManager.addNewVariable();
            });
            
            removeVariableButton.setOnAction(e->{
                dataManager.removeSelectedVariable(tableVariable.getSelectionModel().getSelectedItem());
                reloadWorkspace();
            });
            
            addMethodButton.setOnAction(e->{
                dataManager.addNewMethod();
            });
            
            removeMethodButton.setOnAction(e->{
                dataManager.removeSelectedMethod(tableMethod.getSelectionModel().getSelectedItem());
                reloadWorkspace();
            });
            
            tableVariable.setItems(FXCollections.observableArrayList(temp.getVariables()));
            tableMethod.setItems(FXCollections.observableArrayList(temp.getMethods()));
       }
       
       
     //  ObservableList<jcd.data.Variable> data = getInitialTableData(temp.getVariables().get(0));
       //data = new FXCollections.observableArrayList(temp.getVariables(),temp.getVariables());
       
     //  tableVariable.setItems(data);
//       tableMethod.setItems(data);
       
       
       
       
       
      //System.out.println("I'm gonna select class.");
    }
    
    
//    public ArrayList converToVariableArrayList(ObservableList target){
//        
//        ArrayList<Variable> temp = new ArrayList<>();
//        
//        
//        
//        
//        return 
//    }    
    
//    public ObservableList converToVariableObservableList(ArrayList target){
//        
//        ObservableList<Variable> temp = new ObservableList() {};
//        
//        
//        
//        
//        return temp;
//    }
    
//    private ObservableList getInitialTableData(jcd.data.Variable tempp) {
//
//        List list = new ArrayList();
//        list.add(tempp);
//       
//
//        ObservableList data = FXCollections.observableList(list);
//
//        return data;
//    }
//    
    /**
     * If class name field entered, it must be check whether the name is already used.
     * @param enteredName 
     */
    
    
    public boolean nameAvailableCheck(String enteredName){
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        boolean available = true;
        
        for(int i=0; i<dataManager.getShapes().size(); i++){
            if(dataManager.getShapes().get(i) instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) dataManager.getShapes().get(i);
                if(temp.getTitle().equalsIgnoreCase(enteredName)){
                    available = false;
                }
            }else{
                DraggableInterface temp = (DraggableInterface) dataManager.getShapes().get(i);
                if(temp.getTitle().equalsIgnoreCase(enteredName)){
                    available = false;
                }
            }
        }
        return available;
    }
    
    
    /**
     * Create parentNameList without own ClassName
     * @param itsName 
     */
    public ArrayList parentListUpdate(String itsName){
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        ArrayList<String> parentList = new ArrayList<>();
        
        for(int i=0; i<dataManager.getShapes().size(); i++){
            if(dataManager.getShapes().get(i) instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) dataManager.getShapes().get(i);
                if(!temp.getTitle().equalsIgnoreCase(itsName)){
                    parentList.add(temp.getTitle());
                }
            }else{
                DraggableInterface temp = (DraggableInterface) dataManager.getShapes().get(i);
                if(!temp.getTitle().equalsIgnoreCase(itsName)){
                    parentList.add(temp.getTitle());
                }
            }
        }
        
        return parentList;
       
    }
    
    public ArrayList interfaceListUpdate(String itsName){
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        ArrayList<String> parentList = new ArrayList<>();
        
        for(int i=0; i<dataManager.getShapes().size(); i++){
            if(dataManager.getShapes().get(i) instanceof DraggableInterface){
                DraggableInterface temp = (DraggableInterface) dataManager.getShapes().get(i);
                if(!temp.getTitle().equalsIgnoreCase(itsName)){
                    parentList.add(temp.getTitle());
                }
            }
        }
        
        return parentList;
       
    }
    
    
    
    /*
    	
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
	yesNoDialog.init(primaryStage);
    
    */
    
    public void messagePopup(String title, String message){
        AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
        //messageDialog.init(primaryStage);
        messageDialog.show(title,message);
    }
    
     public void variableTableReload(ArrayList<Variable> reload){
        tableVariable.setItems(FXCollections.observableArrayList(reload));
    }
     
     public void methodTableReload(ArrayList<Method> reload){
        tableMethod.setItems(FXCollections.observableArrayList(reload));
    }

    @Override
    public void resizeWorkspace() {
         DataManager dataManager = (DataManager)app.getDataComponent();
         dataManager.setState(MakerState.RESIZING_SHAPE);
         
         if(dataManager.getSelectedShape() instanceof DraggableClass){
           DraggableClass temp = (DraggableClass) dataManager.getSelectedShape();
           
           
       }
       else{
           DraggableInterface temp = (DraggableInterface) dataManager.getSelectedShape();
         
       }
    }

    @Override
    public void removeWorkspace() {
        DataManager dataManager = (DataManager)app.getDataComponent(); 
        dataManager.setState(MakerState.REMOVE_SHAPE);
        dataManager.removedParentReloadOthers();
        dataManager.removeSelectedShape();
        reloadWorkspace();
    }

    @Override
    public void undoWorkspace() {
        DataManager dataManager = (DataManager)app.getDataComponent(); 
        dataManager.setState(MakerState.UNDO);
        
        if(dataManager.getSelectedShape() instanceof DraggableClass){
           DraggableClass temp = (DraggableClass) dataManager.getSelectedShape();
           
           
       }
       else{
           DraggableInterface temp = (DraggableInterface) dataManager.getSelectedShape();
         
       }
    }

    @Override
    public void redoWorkspace() {
        DataManager dataManager = (DataManager)app.getDataComponent(); 
        dataManager.setState(MakerState.REDO);
        
        if(dataManager.getSelectedShape() instanceof DraggableClass){
           DraggableClass temp = (DraggableClass) dataManager.getSelectedShape();
           
           
       }
       else{
           DraggableInterface temp = (DraggableInterface) dataManager.getSelectedShape();
         
       }
    }
    
    public void photoExport(){
            
                //Get snapshot image
                WritableImage snapImage = canvas.snapshot(new SnapshotParameters(), null);
                 
                //display in new window
                ImageView snapView = new ImageView();
                snapView.setImage(snapImage);
                 
                StackPane snapLayout = new StackPane();
                snapLayout.getChildren().add(snapView);
                 
                Scene snapScene = new Scene(snapLayout, snapImage.getWidth(), snapImage.getHeight());
 
                Stage snapStage = new Stage();
                snapStage.setTitle("Snapshot");
                snapStage.setScene(snapScene);
  
                snapStage.show();
             
                String path_temp = "./work/";
                String fileName = "jClassPhoto.png";
                
                File file = new File(path_temp+fileName);
               // exportImg(path_temp);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(snapImage, null), "png", file);
                } catch (IOException e) {
                    
                }
            
    }
    
    
    public void resetComponent(){
        DataManager dataManager = (DataManager)app.getDataComponent(); 
        dataManager.setState(MakerState.NONE);
        
        dataManager.setSelectedShape(null);
        classText.clear();
        packageText.clear();
        
        if(parents.getItems()!=null)
            parents.getItems().clear();
        
        if(tableVariable.getItems()!=null)
            tableVariable.getItems().clear();
        
        if(tableMethod.getItems()!=null)
            tableMethod.getItems().clear();
        
        dataManager.clearLines();
    }
    
}
