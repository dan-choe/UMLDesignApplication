package jcd.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Shape;

import static jcd.data.MakerState.*;
//import static jcd.data.PoseMakerState.SELECTING_SHAPE;
//import static jcd.data.PoseMakerState.SIZING_SHAPE;

import jcd.gui.Workspace;
import jcd.gui.YesNoCancelDialog;
import saf.components.AppDataComponent;
import saf.AppTemplate;
import saf.ui.AppMethodMessageDialog;
import saf.ui.AppVariableMessageDialog;
import saf.ui.AppWarningMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class DataManager implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    // THESE ARE THE SHAPES TO DRAW
    ArrayList<Node> shapes; //ObservableList
    ArrayList<Relationship> lines;
    
    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Node newShape;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedShape;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;
    
    double currentX = 0;
    double currentY = 0;

    // CURRENT STATE OF THE APP
    MakerState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;
    
//    ScheduleItemDialog sid;
//    MessageDialog messageDialog;
//    YesNoCancelDialog yesNoCancelDialog;
    

    public DataManager() throws Exception {
	// KEEP THE APP FOR LATER
	//app = initApp;
        //shapes = new ObservableList<>();
	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedShape = null;
        
        shapes = new ArrayList<>();
        lines = new ArrayList<>();

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
    }
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;

	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedShape = null;
        
        shapes = new ArrayList<>();
        lines = new ArrayList<>();
	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
        
        
       // Dialog sample = new Dialog();
        //sample.show();
      // messageDialog = new MessageDialog(app.getGUI().getWindow(), "close");
//        sid = new ScheduleItemDialog(app.getGUI().getWindow(), messageDialog);
//        yesNoCancelDialog = new YesNoCancelDialog(app.getGUI().getWindow());
    }
    
    public void clearLines(){
        lines.clear();
    }
    
    public void drawLineAssociation(){
  //System.err.println("shapes.size() " + shapes.size());
        for(int i=0; i<shapes.size(); i++){
            Node temp = shapes.get(i);
            
            HashMap<String, String> checkDrawLine = new HashMap<String, String>();
            
            if(temp instanceof DraggableClass){
                DraggableClass tempClass = (DraggableClass) temp;
                
               // if(!tempClass.getParentName().equalsIgnoreCase("")){
                    ArrayList<Variable> itsVariables = tempClass.getVariables();
                    for(int j=0; j<itsVariables.size(); j++){
                        Variable eachVariable = itsVariables.get(j);
                        
                        if(!tempClass.getParentName().equals(eachVariable.getName())){
                            
                            if(!checkDrawLine.containsKey(eachVariable.getName())){
                                checkDrawLine.put(eachVariable.getType(), eachVariable.getType());
                                
                                Node parent = findParentObject(eachVariable.getType());
                                
                                if(parent!=null){
                        
                                    if(parent instanceof DraggableClass){
                                        DraggableClass parentClass = (DraggableClass) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                    }else{
                                        DraggableInterface parentClass = (DraggableInterface) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        lines.add(path);

                                    }
                            }else{
                                  //not existed type - add class box / but java api
                                  if(!eachVariable.getType().equals("String")
                                          &&!eachVariable.getType().equalsIgnoreCase("int")
                                          &&!eachVariable.getType().equalsIgnoreCase("double")
                                          &&!eachVariable.getType().equalsIgnoreCase("boolean")
                                          ){
                                  
                                        DraggableClass tempNew = new DraggableClass();
                                        tempNew.setLayoutX(Math.random()*40+5);
                                        tempNew.setLayoutY(0);
                                        tempNew.setStartX(tempNew.getLayoutX());
                                        tempNew.setStartY(tempNew.getLayoutY());
                                        tempNew.setTitle(eachVariable.getType());
                                        addClass(tempNew);
                                        
                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(tempNew.layoutXProperty().add(tempNew.widthProperty().subtract(tempNew.width)));
                                        path.setToBoxYDoubleBinding(tempNew.layoutYProperty().add(tempNew.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(tempNew.startX+tempNew.gettWidth());
                                        path.setToY(tempNew.startY+tempNew.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                        //set association

                                        //temp.setParentName(variableDialog.getType());
                                 
                                  }
                                  
                                  
                                }
                            
                        }
                        
                        
                        }

                    }
            } // if(temp instanceof DraggableClass)
            else{
                
                DraggableInterface tempClass = (DraggableInterface) temp;
                
               // if(!tempClass.getParentName().equalsIgnoreCase("")){
                    ArrayList<Variable> itsVariables = tempClass.getVariables();
                    for(int j=0; j<itsVariables.size(); j++){
                        Variable eachVariable = itsVariables.get(j);
                        
                        if(!tempClass.getParentName().equals(eachVariable.getName())){
                            
                            if(!checkDrawLine.containsKey(eachVariable.getName())){
                                checkDrawLine.put(eachVariable.getType(), eachVariable.getType());
                                
                                Node parent = findParentObject(eachVariable.getType());
                                
                                if(parent!=null){
                        
                                    if(parent instanceof DraggableClass){
                                        DraggableClass parentClass = (DraggableClass) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                    }else{
                                        DraggableInterface parentClass = (DraggableInterface) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        lines.add(path);

                                    }
                            }else{
                                  //not existed type - add class box / but java api
                                  if(!eachVariable.getType().equals("String")
                                          &&!eachVariable.getType().equalsIgnoreCase("int")
                                          &&!eachVariable.getType().equalsIgnoreCase("double")
                                          &&!eachVariable.getType().equalsIgnoreCase("boolean")
                                          ){
                                  
                                        DraggableClass tempNew = new DraggableClass();
                                        tempNew.setLayoutX(Math.random()*40+5);
                                        tempNew.setLayoutY(0);
                                         tempNew.setStartX(tempNew.getLayoutX());
                                        tempNew.setStartY(tempNew.getLayoutY());
                                        tempNew.setTitle(eachVariable.getType());
                                        addClass(tempNew);
                                        
                                        Relationship path = new Relationship(tempClass.getTitle(), eachVariable.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(tempNew.layoutXProperty().add(tempNew.widthProperty().subtract(tempNew.width)));
                                        path.setToBoxYDoubleBinding(tempNew.layoutYProperty().add(tempNew.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(tempNew.startX+tempNew.gettWidth());
                                        path.setToY(tempNew.startY+tempNew.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                        //set association

                                        //temp.setParentName(variableDialog.getType());
                                 
                                  }
                                  
                                  
                                }
                            
                        }
                        
                        
                        }

                    }
                
                
                
            } // else 
        } //for           
             
    }
    
    
    public void drawLineAssociationFromMethod(){
        
        //System.out.println("shapes.size() " + shapes.size());
        
        for(int i=0; i<shapes.size(); i++){
            Node temp = shapes.get(i);
            //System.out.println("shapes.size() " + shapes.size());
            HashMap<String, String> checkDrawLine = new HashMap<String, String>();
            
            if(temp instanceof DraggableClass){
                DraggableClass tempClass = (DraggableClass) temp;
                
               // if(!tempClass.getParentName().equalsIgnoreCase("")){
                    ArrayList<Method> itsMethods = tempClass.getMethods();
                    for(int j=0; j<itsMethods.size(); j++){
                        Method eachMethod = itsMethods.get(j);
                        
                        if(!tempClass.getParentName().equals(eachMethod.getReturnType())){
                            
                            if(!checkDrawLine.containsKey(eachMethod.getReturnType())){
                                checkDrawLine.put(eachMethod.getReturnType(), eachMethod.getReturnType());
                                
                                Node parent = findParentObject(eachMethod.getReturnType());
                                
                                if(parent!=null){
                        
                                    if(parent instanceof DraggableClass){
                                        DraggableClass parentClass = (DraggableClass) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getReturnType(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                    }else{
                                        DraggableInterface parentClass = (DraggableInterface) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getReturnType(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        lines.add(path);

                                    }
                            }else{
                                  //not existed type - add class box / but java api
                                  if(!eachMethod.getReturnType().equals("String")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("int")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("double")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("boolean")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("void")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("")){
                                  
                                        DraggableClass tempNew = new DraggableClass();
                                        tempNew.setLayoutX(Math.random()*40+5);
                                        tempNew.setLayoutY(0);
                                         tempNew.setStartX(tempNew.getLayoutX());
                                        tempNew.setStartY(tempNew.getLayoutY());
                                        tempNew.setTitle(eachMethod.getReturnType());
                                        addClass(tempNew);
                                        
                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getName(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(tempNew.layoutXProperty().add(tempNew.widthProperty().subtract(tempNew.width)));
                                        path.setToBoxYDoubleBinding(tempNew.layoutYProperty().add(tempNew.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(tempNew.startX+tempNew.gettWidth());
                                        path.setToY(tempNew.startY+tempNew.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                        //set association

                                        //temp.setParentName(variableDialog.getType());
                                 
                                  }
                                  
                                  
                                }
                            
                        }
                        
                        
                        }

                    }
            } // if(temp instanceof DraggableClass)
            else{
                
                DraggableInterface tempClass = (DraggableInterface) temp;
                
               // if(!tempClass.getParentName().equalsIgnoreCase("")){
                    ArrayList<Method> itsMethods = tempClass.getMethods();
                    for(int j=0; j<itsMethods.size(); j++){
                        Method eachMethod = itsMethods.get(j);
                        
                        if(!tempClass.getParentName().equals(eachMethod.getName())){
                            
                            if(!checkDrawLine.containsKey(eachMethod.getReturnType())){
                                checkDrawLine.put(eachMethod.getReturnType(), eachMethod.getReturnType());
                                
                                Node parent = findParentObject(eachMethod.getReturnType());
                                
                                if(parent!=null){
                        
                                    if(parent instanceof DraggableClass){
                                        DraggableClass parentClass = (DraggableClass) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getReturnType(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                    }else{
                                        DraggableInterface parentClass = (DraggableInterface) parent;

                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getReturnType(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                                        path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(parentClass.startX+parentClass.gettWidth());
                                        path.setToY(parentClass.startY+parentClass.gettHeight());
                                        path.createLine();
                                        lines.add(path);

                                    }
                            }else{
                                  //not existed type - add class box / but java api
                                  if(!eachMethod.getReturnType().equals("String")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("int")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("double")
                                          &&!eachMethod.getReturnType().equalsIgnoreCase("boolean")
                                          ){
                                  
                                        DraggableClass tempNew = new DraggableClass();
                                        tempNew.setLayoutX(Math.random()*40+5);
                                        tempNew.setLayoutY(0);
                                         tempNew.setStartX(tempNew.getLayoutX());
                                        tempNew.setStartY(tempNew.getLayoutY());
                                        tempNew.setTitle(eachMethod.getReturnType());
                                        addClass(tempNew);
                                        
                                        Relationship path = new Relationship(tempClass.getTitle(), eachMethod.getReturnType(), "association");

                                        path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                                        path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                                        path.setToBoxXDoubleBinding(tempNew.layoutXProperty().add(tempNew.widthProperty().subtract(tempNew.width)));
                                        path.setToBoxYDoubleBinding(tempNew.layoutYProperty().add(tempNew.heightProperty()));

                                        path.setFromX(tempClass.startX);
                                        path.setFromY(tempClass.startY);
                                        path.setToX(tempNew.startX+tempNew.gettWidth());
                                        path.setToY(tempNew.startY+tempNew.gettHeight());
                                        path.createLine();
                                        
                                        path.setOpacity(0.4);

                                        lines.add(path);

                                        //set association

                                        //temp.setParentName(variableDialog.getType());
                                 
                                  }
                                  
                                  
                                }
                            
                        }
                        
                        
                        }

                    }
                
                
                
            } // else 
        } //for           
             
    }
    
    
    public void drawLineExtends(){
        
        lines.clear();
        
        for(int i=0; i<shapes.size(); i++){
            Node temp = shapes.get(i);
            
            if(temp instanceof DraggableClass){
                DraggableClass tempClass = (DraggableClass) temp;
                
                if(!tempClass.getParentName().equalsIgnoreCase("")){
                    String parentName = tempClass.getParentName();
                    Node parent = findParentObject(parentName);
                    //System.out.println("parent : "+ parentName);
                    
                    if(parent!=null){
                        
                        if(parent instanceof DraggableClass){
                            DraggableClass parentClass = (DraggableClass) parent;
                            
                            Relationship path = new Relationship(tempClass.getTitle(), parentName, "inheritance");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                            path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(parentClass.startX+parentClass.gettWidth());
                            path.setToY(parentClass.startY+parentClass.gettHeight());
                            path.createLine();
                            
                          

                            lines.add(path);
                            
                        }else{
                            DraggableInterface parentClass = (DraggableInterface) parent;
                            
                            Relationship path = new Relationship(tempClass.getTitle(), parentName, "inheritance");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                            path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(parentClass.startX+parentClass.gettWidth());
                            path.setToY(parentClass.startY+parentClass.gettHeight());
                            path.createLine();
                            lines.add(path);
                            
                            /*
                            Path path = new Path();

                            MoveTo moveTo = new MoveTo();
                            moveTo.xProperty().bind(tempClass.layoutXProperty());
                            moveTo.yProperty().bind(tempClass.layoutYProperty());

                            HLineTo hLineTo = new HLineTo();
                            hLineTo.xProperty().bind(tempClass.layoutXProperty().add(tempClass.layoutXProperty()).divide(4));

                            LineTo lineTo = new LineTo();
                            lineTo.xProperty().bind(parentClass.layoutXProperty());
                            lineTo.yProperty().bind(parentClass.layoutYProperty());

                            path.getElements().add(moveTo);
                            path.getElements().add(hLineTo);
                            path.getElements().add(lineTo);

                            path.setStrokeWidth(1);
                            path.setStroke(Color.BLUE);

                            lines.add(path);
                            */
                        }
                        
                        //System.out.println("found parent");
                        
                    }
                }
                
            }
            
            
            else{
                DraggableInterface tempClass = (DraggableInterface) temp;
                
                if(!tempClass.getParentName().equalsIgnoreCase("")){
                    String parentName = tempClass.getParentName();
                    Node parent = findParentObject(parentName);
                    //System.out.println("parent : "+ parentName);
                    
                    if(parent!=null){
                        
                        if(parent instanceof DraggableClass){
                            DraggableClass parentClass = (DraggableClass) parent;
                            
                            Relationship path = new Relationship(tempClass.getTitle(), parentName, "inheritance");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                            path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(parentClass.startX+parentClass.gettWidth());
                            path.setToY(parentClass.startY+parentClass.gettHeight());
                            path.createLine();
                            
                          

                            lines.add(path);
                            
                        }else{
                            DraggableInterface parentClass = (DraggableInterface) parent;
                            
                            Relationship path = new Relationship(tempClass.getTitle(), parentName, "inheritance");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                            path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(parentClass.startX+parentClass.gettWidth());
                            path.setToY(parentClass.startY+parentClass.gettHeight());
                            path.createLine();
                            lines.add(path);
                            
                            /*
                            Path path = new Path();

                            MoveTo moveTo = new MoveTo();
                            moveTo.xProperty().bind(tempClass.layoutXProperty());
                            moveTo.yProperty().bind(tempClass.layoutYProperty());

                            HLineTo hLineTo = new HLineTo();
                            hLineTo.xProperty().bind(tempClass.layoutXProperty().add(tempClass.layoutXProperty()).divide(4));

                            LineTo lineTo = new LineTo();
                            lineTo.xProperty().bind(parentClass.layoutXProperty());
                            lineTo.yProperty().bind(parentClass.layoutYProperty());

                            path.getElements().add(moveTo);
                            path.getElements().add(hLineTo);
                            path.getElements().add(lineTo);

                            path.setStrokeWidth(1);
                            path.setStroke(Color.BLUE);

                            lines.add(path);
                            */
                        }
                        
                        //System.out.println("found parent");
                        
                    }
                }
                
            }
            
            /*else{
                DraggableInterface tempClass = (DraggableInterface) temp;

                if(tempClass.getItsParents().size()!=0){
                    String parentName = tempClass.getItsParents().get(0).getTitle();
                    Node parent = findParentObject(parentName);
                    System.out.println("interface parent : "+ parentName);
                    
                    if(parent!=null){
                        
                        
                            DraggableInterface parentClass = (DraggableInterface) parent;
                            
                            Relationship path = new Relationship(tempClass.getTitle(), parentName, "inheritance");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(parentClass.layoutXProperty().add(parentClass.widthProperty().subtract(parentClass.width)));
                            path.setToBoxYDoubleBinding(parentClass.layoutYProperty().add(parentClass.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(parentClass.startX+parentClass.gettWidth());
                            path.setToY(parentClass.startY+parentClass.gettHeight());
                            path.createLine();
                            lines.add(path);
                        
                        
                    }
                }
                
            }*/ // else interface
        }
    }
    
    public void drawLineImplements(){
        
        for(int i=0; i<shapes.size(); i++){
            Node temp = shapes.get(i);
            
            if(temp instanceof DraggableClass){
                DraggableClass tempClass = (DraggableClass) temp;
                
                if(tempClass.getItsImplements().size()!=0){
                    for(int j=0; j<tempClass.getItsImplements().size(); j++){
                        String implementName = tempClass.getItsImplements().get(j).getTitle();
                        DraggableInterface itsInterface = (DraggableInterface) findParentObject(implementName);
                        //System.out.println("parent : "+ parentName);

                        if(itsInterface!=null){

                            
                            
                            Relationship path = new Relationship(tempClass.getTitle(), implementName, "realization");
                            
                            path.setFromBoxXDoubleBinding(tempClass.layoutXProperty().add(tempClass.gettWidth()));
                            path.setFromBoxYDoubleBinding(tempClass.layoutYProperty().add(0));
                            path.setToBoxXDoubleBinding(itsInterface.layoutXProperty().add(itsInterface.widthProperty().subtract(itsInterface.width)));
                            path.setToBoxYDoubleBinding(itsInterface.layoutYProperty().add(itsInterface.heightProperty()));
                            
                            path.setFromX(tempClass.startX);
                            path.setFromY(tempClass.startY);
                            path.setToX(itsInterface.startX+itsInterface.gettWidth());
                            path.setToY(itsInterface.startY+itsInterface.gettHeight());
                            path.createLine();
                            lines.add(path);
                            
                            }
                        } // for j
                        
                        
                    
                }
                
            

            }
        }
        
        
        

    }
    
    
    
    public Node findParentObject(String targetName){
        for(int i=0; i<shapes.size(); i++){
            Node temp = shapes.get(i);
            if(temp instanceof DraggableClass){
                DraggableClass tempClass = (DraggableClass) temp;
                
                if(tempClass.getTitle().equals(targetName))
                    return tempClass;
                
            }else{
                DraggableInterface tempClass = (DraggableInterface) temp;
                
                if(tempClass.getTitle().equals(targetName))
                    return tempClass;
            }
        }
        return null;
    }

    public ArrayList<Relationship> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Relationship> lines) {
        this.lines = lines;
    }
    
    
    
    public ArrayList<Node> getShapes() {
	return shapes;
    }

    public Color getBackgroundColor() {
	return backgroundColor;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    
    public void setShapes(ArrayList<Node> initShapes) {
	shapes = initShapes;
    }
    
    public void setCurrentPoints(double x, double y){
        this.currentX = x;
        this.currentY = y;
        
        //System.out.println("                    "+x + "   " + y);
    }
    

    
    
    public void setBackgroundColor(Color initBackgroundColor) {
	backgroundColor = initBackgroundColor;
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
	Background background = new Background(fill);
	canvas.setBackground(background);
    }

    public void setCurrentFillColor(Color initColor) {
	currentFillColor = initColor;
	//if (selectedShape != null)
	//    selectedShape.setFill(currentFillColor);
    }

    public void setCurrentOutlineColor(Color initColor) {
	currentOutlineColor = initColor;
	if (selectedShape != null) {
	//    selectedShape.setStroke(initColor);
	}
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
	currentBorderWidth = initBorderWidth;
	if (selectedShape != null) {
	//    selectedShape.setStrokeWidth(initBorderWidth);
	}
    }
    
    public void removeSelectedShape() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    selectedShape = null;
	}
    }
    
    public void moveSelectedShapeToBack() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    if (shapes.isEmpty()) {
		shapes.add(selectedShape);
	    }
	    else {
		ArrayList<Node> temp = new ArrayList();
		temp.add(selectedShape);
		for (Node node : shapes)
		    temp.add(node);
		shapes.clear();
		for (Node node : temp)
		    shapes.add(node);
	    }
	}
    }
    
    public void moveSelectedShapeToFront() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    shapes.add(selectedShape);
	}
    }
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
	//setState(SELECTING_SHAPE);
	newShape = null;
	selectedShape = null;

	// INIT THE COLORS
	//currentFillColor = Color.web(WHITE_HEX);
	//currentOutlineColor = Color.web(BLACK_HEX);
	
	shapes.clear();
	((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void selectSizedShape() {
	if (selectedShape != null){
	    unhighlightShape(selectedShape);
        }
	selectedShape = newShape;
	highlightShape(selectedShape);
	newShape = null;
	if (state == RESIZING_SHAPE) {
	 //   state = ((Draggable)selectedShape).getStartingState();
	}
    }
    
    public void unhighlightShape(Node shape) {
	//shape.getStyleClass().add("wholebox");
        if(shape!=null)
            shape.getStyleClass().clear();
    }
    
    public void highlightShape(Node shape) {
        shape.getStyleClass().add("selectbox");
	//shape.setEffect(highlightedEffect);
    }
    
    public void bluehighlightShape(Node shape) {
        shape.getStyleClass().add("selectbox2");
	//shape.setEffect(highlightedEffect);
    }
    
    

    /**
     * Create a new Class Object and init controller
     * 
     * @param x
     * @param y 
     */
    public void startNewClass(int x, int y) {
	DraggableClass newClass = new DraggableClass();
        newClass.setWS((Workspace)app.getWorkspaceComponent());
        newClass.setDM(this);
        newClass.setLayoutX(x);
        newClass.setLayoutY(y);
	//newClass.start(x, y);
/*
        newClass.setOnMouseDragged((MouseEvent me) -> { 
           // if(currentMode.equals("Select")){
            //System.out.println(ellipse.getId()+" 1");
            double dragX = me.getX();
            double dragY = me.getY();
            
            double dragXx = currentX;
            double dragYy = currentY;
             
            //calculate new position of the circle
            double newXPosition = (dragXx) - (newClass.gettWidth()/2);
            double newYPosition = (dragYy) - (newClass.gettHeight()/2);
            
            //Node's method
            newClass.relocate(newXPosition, newYPosition);
            newClass.setStartX(newXPosition);
            newClass.setStartY(newYPosition);
             
        });
        
        newClass.setOnMousePressed((MouseEvent me) -> {
            unselectClass();
            highlightShape(newClass);
            setSelectedShape(newClass);
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.loadSelectClassWorkspace();
        });
        
      */  
        newShape = newClass;
	initNewShape();
    }
    
     public void startNewClassFromJson(int x, int y) {
	DraggableClass newClass = new DraggableClass();
        newClass.setWS((Workspace)app.getWorkspaceComponent());
        newClass.setDM(this);
/*
        newClass.setOnMouseDragged((MouseEvent me) -> {
            
            double dragX = me.getX();
            double dragY = me.getY();
            
            double dragXx = currentX;
            double dragYy = currentY;
             
            //calculate new position of the circle
            double newXPosition = (dragXx) - (newClass.gettWidth()/2);
            double newYPosition = (dragYy) - (newClass.gettHeight()/2);
            
            //Node's method
            newClass.relocate(newXPosition, newYPosition);
            newClass.setStartX(newXPosition);
            newClass.setStartY(newYPosition);
            
       
             //updateJsonShape(dataManager, rectangle, null);
           
             
            
        });
        
        newClass.setOnMousePressed((MouseEvent me) -> {
            
            unselectClass();
            highlightShape(newClass);
            setSelectedShape(newClass);
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.loadSelectClassWorkspace();
               
        });
        
        */
        newShape = newClass;
	initNewShape();
    }
    
     
     
     
     /**
     * Create a new Interface Object and init controller
     * 
     * @param x
     * @param y 
     */
    public void startNewInterface(int x, int y) {
	DraggableInterface newInterface = new DraggableInterface();
        newInterface.setWS((Workspace)app.getWorkspaceComponent());
        newInterface.setDM(this);
        newInterface.setLayoutX(x);
        newInterface.setLayoutY(y);
	//newClass.start(x, y);
/*
        newInterface.setOnMouseDragged((MouseEvent me) -> {
            
           // if(currentMode.equals("Select")){
            //System.out.println(ellipse.getId()+" 1");
            double dragX = me.getX();
            double dragY = me.getY();
            
            double dragXx = currentX;
            double dragYy = currentY;
             
            //calculate new position of the circle
            double newXPosition = (dragXx) - (newInterface.gettWidth()/2);
            double newYPosition = (dragYy) - (newInterface.gettHeight()/2);
            
            //Node's method
            newInterface.relocate(newXPosition, newYPosition);
            newInterface.setStartX(newXPosition);
            newInterface.setStartY(newYPosition);
            
         
        });
        
        newInterface.setOnMousePressed((MouseEvent me) -> {
            
            unselectClass();
            highlightShape(newInterface);
            setSelectedShape(newInterface);
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.loadSelectClassWorkspace();
            
           
        });
    */    
        
        newShape = newInterface;
	initNewShape();
    }
    
     public void startNewInterfaceFromJson(int x, int y) {
	DraggableInterface newInterface = new DraggableInterface();
        newInterface.setWS((Workspace)app.getWorkspaceComponent());
        newInterface.setDM(this);
/*
        newInterface.setOnMouseDragged((MouseEvent me) -> {
            
            double dragX = me.getX();
            double dragY = me.getY();
            
            double dragXx = currentX;
            double dragYy = currentY;
             
            //calculate new position of the circle
            double newXPosition = (dragXx) - (newInterface.gettWidth()/2);
            double newYPosition = (dragYy) - (newInterface.gettHeight()/2);
            
            //Node's method
            newInterface.relocate(newXPosition, newYPosition);
            newInterface.setStartX(newXPosition);
            newInterface.setStartY(newYPosition);
            
       
             //updateJsonShape(dataManager, rectangle, null);
           
             
            
        });
        
        newInterface.setOnMousePressed((MouseEvent me) -> {
            
            unselectClass();
            highlightShape(newInterface);
            setSelectedShape(newInterface);
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.loadSelectClassWorkspace();
            
          
        });
    */    
        
        newShape = newInterface;
	initNewShape();
    }
     
     
     
     
     

    public void unselectClass(){
        for (int i = 0; i < shapes.size(); i++) {
            unhighlightShape(shapes.get(i));
        }
        
    }
    
    
    public void startNewEllipse(int x, int y) {
	//DraggableEllipse newEllipse = new DraggableEllipse();
	//newEllipse.start(x, y);
	//newShape = newEllipse;
	//initNewShape();
    }

    public void initNewShape() {
// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
	    selectedShape = null;
	}

	// USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        if(app !=null){
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
        workspace.getCanvas().getChildren().add(newShape);
        }
//	newShape.setFill(workspace.getFillColorPicker().getValue());
//	newShape.setStroke(workspace.getOutlineColorPicker().getValue());
//	newShape.setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
	
	// ADD THE SHAPE TO THE CANVAS
	shapes.add(newShape);
        
        
	
	// GO INTO SHAPE SIZING MODE
	//state = MakerState.SELECTING_SHAPE;
    }

    public Node getNewShape() {
	return newShape;
    }

    public Node getSelectedShape() {
	return selectedShape;
    }
    
    public String getSelectedShapeName() {
        
        String selectedShapeName = "";
        
        if(selectedShape != null){
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;
                selectedShapeName = temp.getTitle();
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;
                selectedShapeName = temp.getTitle();
            }  
        }
        return selectedShapeName;
    }

    public void setSelectedShape(Node initSelectedShape) {
	selectedShape = initSelectedShape;
    }
    
    public void editSelectedShapeName(String name){
       
        if(selectedShape != null){
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;
                temp.setTitle(name);
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;
                temp.setTitle(name);
            }  
        }
    }
    
    public boolean editParent(String name){
        
        try{
            if(selectedShape != null){
                if(selectedShape instanceof DraggableClass){
                    DraggableClass temp = (DraggableClass) selectedShape;
                    //temp.setParentName(name);
                   // System.out.println("selectedShape " + temp.getTitle());
                    Node selectedParent = findParentObject(name);

                    if(selectedParent instanceof DraggableInterface){
                     //   System.out.println("Class's Parent should be class " + name);
                         AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                         messageDialog.show("Warning","Class's Parent should be class");
                         return false;
                    }else{
                        temp.setParentName(name);

                      //   System.out.println("null");
                         return true;
                    }

                }else{
                    DraggableInterface temp = (DraggableInterface) selectedShape;

                    Node selectedParent = findParentObject(name);

                    if(selectedParent instanceof DraggableClass){
                     //   System.out.println("Interface's Parent should be interface " + name);
                        AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                       //  messageDialog.show("Warning","Interface's Parent should be interface");
                         return false;
                    }else{
                        temp.setParentName(name);
                      //   System.out.println("null");
                         return false;
                    }

                    //System.out.println("editParent " + name);
                }  
            }
        }catch(IndexOutOfBoundsException e){
           // System.out.println("/////////////////");
        }
        return false;
    }
    
    public boolean editImplement(String name){
        if(selectedShape != null){
            
            
            if(selectedShape instanceof DraggableClass){
                  
                DraggableClass temp = (DraggableClass) selectedShape;
                Node selectedParent = findParentObject(name);
                
               //  System.out.println("selectedShape " + temp.getTitle());
                
                if(selectedParent instanceof DraggableInterface){
                    temp.addItsImplements((DraggableInterface) selectedParent);
                    return true;
                }else if(selectedParent instanceof DraggableClass){
                    
                //    System.out.println("Class's Implement should be interface " + name);
                    AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                     messageDialog.show("Warning","Class's Implement should be interface ");
                     return false;
                }else{
                   // System.out.println("null");
                    return false;
                }
                
            }else{
             //  System.out.println("Interface can not have Implement");
               //AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
               //      messageDialog.show("Warning","Interface can not have Implement");
                     return false;
            }  
        }
        return false;
    }
    
    public void removedParentReloadOthers(){
        
        String removedParentName = "";
        
        if(selectedShape != null){
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;
                removedParentName = temp.getTitle();
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;
                removedParentName = temp.getTitle();
            }  
        }
        
        for(int i=0; i<shapes.size(); i++){
            if(shapes.get(i) instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) shapes.get(i);
                if(temp.getParentName().equals(removedParentName)){
                    temp.setParentName("");
                }
            }else{
                DraggableInterface temp = (DraggableInterface) shapes.get(i);
                if(temp.getParentName().equals(removedParentName)){
                    temp.setParentName("");
                }
            }
        }
        
    }
    
    
    
    public void addNewVariable(){
        if(selectedShape != null){
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    //workspace.loadSelectedShapeSettings(shape);
            
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;

                AppVariableMessageDialog variableDialog = AppVariableMessageDialog.getSingleton();
                variableDialog.setVariableList(temp.getVariables());
                variableDialog.show("1","2");
                
                
                if(!variableDialog.getName().equalsIgnoreCase("")){
                    Variable newVariable = new Variable();
                    newVariable.setAccess(variableDialog.getAccess());
                    newVariable.setName(variableDialog.getName());
                    newVariable.setType(variableDialog.getType());
                    newVariable.setIsStatic(variableDialog.isIsStatic());
                    newVariable.setIsFinal(variableDialog.isIsFinal());
                    
                    newVariable.reloaditsValues();
                    temp.addVariable(newVariable);
                    
                    Node newTypeVariable = findParentObject(variableDialog.getType());
                    if(newTypeVariable == null){
                        DraggableClass tempNew = new DraggableClass();
                        tempNew.setLayoutX(selectedShape.getLayoutX()+300);
                        tempNew.setLayoutY(selectedShape.getLayoutY()-10);
                        tempNew.setStartX(selectedShape.getLayoutX()+300);
                        tempNew.setStartY(selectedShape.getLayoutY()-10);
                        tempNew.setTitle(variableDialog.getType());
                        addClass(tempNew);
                        
                        //set association
                        
                        //temp.setParentName(variableDialog.getType());
                    }

                    variableDialog.resetData();
                }

                workspace.variableTableReload(temp.getVariables());
                workspace.reloadWorkspace();
               // System.err.println("variable count " + temp.getVariables().size());
                
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;

                AppVariableMessageDialog variableDialog = AppVariableMessageDialog.getSingleton();
                variableDialog.show("1","2");
                
                if(!variableDialog.getName().equalsIgnoreCase("")){
                    Variable newVariable = new Variable();
                    newVariable.setAccess(variableDialog.getAccess());
                    newVariable.setName(variableDialog.getName());
                    newVariable.setType(variableDialog.getType());
                    newVariable.setIsStatic(variableDialog.isIsStatic());
                    newVariable.setIsFinal(variableDialog.isIsFinal());
                    
                    newVariable.reloaditsValues();
                    temp.addVariable(newVariable);
                    
                    Node newTypeVariable = findParentObject(variableDialog.getType());
                    if(newTypeVariable == null){
                        DraggableClass tempNew = new DraggableClass();
                        tempNew.setLayoutX(selectedShape.getLayoutX()+300);
                        tempNew.setLayoutY(selectedShape.getLayoutY()-10);
                        tempNew.setStartX(selectedShape.getLayoutX()+300);
                        tempNew.setStartY(selectedShape.getLayoutY()-10);
                        tempNew.setTitle(variableDialog.getType());
                        addClass(tempNew);
                    }

                    variableDialog.resetData();
                }
                
                 workspace.variableTableReload(temp.getVariables());
                 workspace.reloadWorkspace();
            }  
        }
    }
    
    public void removeSelectedVariable(Variable itemToRemove){
        // PROMPT THE USER TO SAVE UNSAVED WORK
        //yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        AppYesNoCancelDialogSingleton messageDialog = AppYesNoCancelDialogSingleton.getSingleton();
        //messageDialog.init(primaryStage);
        messageDialog.show("Remove The Variable","Will you remove the variable?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = messageDialog.getSelection();

        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) {
            
            if(selectedShape != null){
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    //workspace.loadSelectedShapeSettings(shape);
            
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;
                temp.getVariables().remove(itemToRemove);
                temp.reloadVariable();
                workspace.variableTableReload(temp.getVariables());
                
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;
                 temp.getVariables().remove(itemToRemove);
                 temp.reloadVariable();
                 workspace.variableTableReload(temp.getVariables());
            }
            
            
            
            }
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            //gui.getFileController().markAsEdited(gui);
        }
    }
    
    public void removeSelectedMethod(Method itemToRemove){
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton messageDialog = AppYesNoCancelDialogSingleton.getSingleton();
        //messageDialog.init(primaryStage);
        messageDialog.show("Remove The Variable","Will you remove the variable?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = messageDialog.getSelection();

        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) {
            
            if(selectedShape != null){
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    //workspace.loadSelectedShapeSettings(shape);
            
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;
                temp.getMethods().remove(itemToRemove);
                temp.reloadMethod();
                workspace.methodTableReload(temp.getMethods());
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;
                 temp.getMethods().remove(itemToRemove);
                 temp.reloadMethod();
                 workspace.methodTableReload(temp.getMethods());
            }
            
            
            
            }
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            //gui.getFileController().markAsEdited(gui);
        }
    }
    
    public void addNewMethod(){
        if(selectedShape != null){
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    //workspace.loadSelectedShapeSettings(shape);
            
            if(selectedShape instanceof DraggableClass){
                DraggableClass temp = (DraggableClass) selectedShape;

                AppMethodMessageDialog methodDialog = AppMethodMessageDialog.getSingleton();
                methodDialog.setMethodList(temp.getMethods());
                methodDialog.show("1","2");
                
                
                if(!methodDialog.getName().equalsIgnoreCase("")){
                    Method newMethod = new Method();
                    newMethod.setAccess(methodDialog.getAccess());
                    newMethod.setName(methodDialog.getName());
                    newMethod.setReturnType(methodDialog.getReturnType());
                    newMethod.setStaticType(methodDialog.isIsStatic());
                    newMethod.setFinalType(methodDialog.isIsFinal());
                    newMethod.setAbstractType(methodDialog.isIsAbstract());
                    
                    newMethod.setType1(methodDialog.getType1());
                    newMethod.setArg1(methodDialog.getArg1());
                    newMethod.setType2(methodDialog.getType2());
                    newMethod.setArg2(methodDialog.getArg2());
                    newMethod.setType3(methodDialog.getType3());
                    newMethod.setArg3(methodDialog.getArg3());
                    
                    
                    newMethod.reloaditsValues();
                    temp.addMethod(newMethod);
                    
                    Node newReturnTypeMethod = findParentObject(methodDialog.getReturnType());
                    if(newReturnTypeMethod == null){
                        DraggableClass tempNew = new DraggableClass();
                        tempNew.setLayoutX(selectedShape.getLayoutX()+300);
                        tempNew.setLayoutY(selectedShape.getLayoutY()-10);
                        tempNew.setStartX(selectedShape.getLayoutX()+300);
                        tempNew.setStartY(selectedShape.getLayoutY()-10);
                        tempNew.setTitle(methodDialog.getReturnType());
                        addClass(tempNew);
                    }

                    methodDialog.resetData();
                }
                
                workspace.methodTableReload(temp.getMethods());
                workspace.reloadWorkspace();
               // System.err.println("variable count " + temp.getVariables().size());
                
            }else{
                DraggableInterface temp = (DraggableInterface) selectedShape;

                AppMethodMessageDialog methodDialog = AppMethodMessageDialog.getSingleton();
                methodDialog.setMethodList(temp.getMethods());
                methodDialog.show("1","2");
                
                if(!methodDialog.getName().equalsIgnoreCase("")){
                    Method newMethod = new Method();
                    newMethod.setAccess(methodDialog.getAccess());
                    newMethod.setName(methodDialog.getName());
                    newMethod.setReturnType(methodDialog.getReturnType());
                    newMethod.setStaticType(methodDialog.isIsStatic());
                    newMethod.setFinalType(methodDialog.isIsFinal());
                    newMethod.setAbstractType(methodDialog.isIsAbstract());
                    newMethod.setType1(methodDialog.getType1());
                    newMethod.setArg1(methodDialog.getArg1());
                    newMethod.setType2(methodDialog.getType2());
                    newMethod.setArg2(methodDialog.getArg2());
                    newMethod.setType3(methodDialog.getType3());
                    newMethod.setArg3(methodDialog.getArg3());
                    
                    
                    newMethod.reloaditsValues();
                    temp.addMethod(newMethod);
                    
                    Node newReturnTypeMethod = findParentObject(methodDialog.getReturnType());
                    if(newReturnTypeMethod == null){
                        DraggableClass tempNew = new DraggableClass();
                        tempNew.setLayoutX(selectedShape.getLayoutX()+300);
                        tempNew.setLayoutY(selectedShape.getLayoutY()-10);
                        tempNew.setStartX(selectedShape.getLayoutX()+300);
                        tempNew.setStartY(selectedShape.getLayoutY()-10);
                        tempNew.setTitle(methodDialog.getReturnType());
                        addClass(tempNew);
                    }

                    methodDialog.resetData();
                }
                
                workspace.methodTableReload(temp.getMethods());
                workspace.reloadWorkspace();
            }  
        }
    }
    

    public void addShape(Node shapeToAdd) {

            
    }
    
    /*
    
    Add ClassBox from json
    
    */
    public void addClass(DraggableClass shapeToAdd) {
        
	DraggableClass newClass = shapeToAdd;
        newClass.reloadVariable();
        newClass.reloadMethod();
        //newClass.getBottom().setPrefHeight(newClass.getHeight());
        newClass.setWS((Workspace)app.getWorkspaceComponent());
        newClass.setDM(this);
      
        //drawLineAssociation();
        
        newShape = newClass;
	initNewShape();

    }
    
    /*
    public void addLinesAfterAddBox(){
        Node newTypeVariable = findParentObject(variableDialog.getType());
                    if(newTypeVariable == null){
                        DraggableClass tempNew = new DraggableClass();
                        tempNew.setLayoutX(selectedShape.getLayoutX()+300);
                        tempNew.setLayoutY(selectedShape.getLayoutY()-100);
                        tempNew.setTitle(variableDialog.getType());
                        addClass(tempNew);
                        
                        //set association
                        
                        //temp.setParentName(variableDialog.getType());
                    }
    }
    */
    
    public void addInterface(DraggableInterface shapeToAdd) {
        DraggableInterface newInterface = shapeToAdd;
        newInterface.reloadVariable();
        newInterface.reloadMethod();
        //newInterface.getBottom().setPrefHeight(newInterface.getHeight());
        newInterface.setWS((Workspace)app.getWorkspaceComponent());
        newInterface.setDM(this);
       
        newShape = newInterface;
	initNewShape();
    }
    
     
    
    
    public void removeShape(Shape shapeToRemove) {
	shapes.remove(shapeToRemove);
    }


    public MakerState getState() {
	return state;
    }

    public void setState(MakerState initState) {
	state = initState;
    }

    public boolean isInState(MakerState testState) {
	return state == testState;
    }
    
   
}
