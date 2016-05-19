package jcd.data;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jcd.gui.Workspace;

/**
 * This is a DraggableClass for our jClass application.
 * 
 * @author Dan Choe
 * @version 1.0
 */
public class DraggableClass extends VBox implements Draggable {
    
    
    private boolean resizeMode = false;
    // 0 : nothing, 1 : drag, 2: resize 
    private int status = 0;
    
    private boolean resizeTop, resizeBottom, resizeLeft, resizeRight;
    
    private double currentMouseX = 0;
    private double currentMouseY = 0;


    
    VBox group;
    VBox top, middle, bottom;
    //Label title, packageName, parentName, isAbstractLabel;
    Text title, packageName, isAbstractLabel;
    boolean isAbstract;
    
    DraggableClass itsParent;
    String parentName;
    
    ArrayList<DraggableInterface> itsImplements;
    ArrayList<Variable> variables;
    ArrayList<Method> methods;  
    
    ArrayList<Relationship> itsRelationships;
    int countConnectLines;
    
    double startX, startY;
    double width, height;
    
    DataManager dm;
    Workspace ws;
    
    Node itsParentNode = DraggableClass.this;
    public DraggableClass() {
      
        onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                
                if (dm.getState()==MakerState.SELECTING_SHAPE ) {
          //  System.out.println("onMousePressedProperty : Change to resize");
            itsParentNode.setCursor(Cursor.DEFAULT);
                //bottom.setPrefHeight(group.getPrefHeight());
                dm.unselectClass();
                dm.highlightShape(DraggableClass.this);
                dm.setSelectedShape(DraggableClass.this);
                ws.loadSelectClassWorkspace();
          
                currentMouseX = e.getSceneX();
                currentMouseY = e.getSceneY();

                startX = itsParentNode.getLayoutX()
                        *(itsParentNode.getParent().localToSceneTransformProperty().getValue().getMxx());
                startY = itsParentNode.getLayoutY()*
                        (itsParentNode.getParent().localToSceneTransformProperty().getValue().getMyy());
                }
                
                if (dm.getState()==MakerState.RESIZING_SHAPE) {
          //  System.out.println("onMousePressedProperty : Change to resize");
            
                //bottom.setPrefHeight(group.getPrefHeight());
                dm.unselectClass();
                dm.bluehighlightShape(DraggableClass.this);
                dm.setSelectedShape(DraggableClass.this);
                ws.loadSelectClassWorkspace();
          
                currentMouseX = e.getSceneX();
                currentMouseY = e.getSceneY();

                startX = itsParentNode.getLayoutX()
                        *(itsParentNode.getParent().localToSceneTransformProperty().getValue().getMxx());
                startY = itsParentNode.getLayoutY()*
                        (itsParentNode.getParent().localToSceneTransformProperty().getValue().getMyy());
                }
            }
        });

        //Event Listener for MouseDragged
        onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                
               // System.out.println("onMouseDraggedProperty : changing");

          
                 double scaleX = itsParentNode.localToSceneTransformProperty().getValue().getMxx();
                 double scaleY = itsParentNode.localToSceneTransformProperty().getValue().getMyy();

                double offsetX = e.getSceneX() - currentMouseX;
                double offsetY = e.getSceneY() - currentMouseY;

                if ( dm.getState()==MakerState.SELECTING_SHAPE ) {

                    startX += offsetX;
                    startY += offsetY;

                    double scaledX = (startX* 1)/(itsParentNode.getParent().localToSceneTransformProperty().getValue().getMxx());
                    double scaledY = (startY* 1)/(itsParentNode.getParent().localToSceneTransformProperty().getValue().getMyy());

                    itsParentNode.setLayoutX(scaledX);
                    itsParentNode.setLayoutY(scaledY);
                    //bottom.setScaleY(newScaleY);

                    setStartX(scaledX);
                    setStartY(scaledY);

                } else if( dm.getState()==MakerState.RESIZING_SHAPE ) {

                    if (resizeTop) {
                        double newHeightSize =getBoundsInLocal().getHeight()- offsetY / scaleY- getInsets().getTop();
                        startY += offsetY;
                       
                        setLayoutY(startY / (itsParentNode.getParent().localToSceneTransformProperty().getValue().getMyy()));
                        setPrefHeight(newHeightSize);
                        bottom.setPrefHeight(getPrefHeight());
                        
                    }
                    if (resizeLeft) {
                        double newWidthSize =getBoundsInLocal().getWidth()- offsetX / scaleX- getInsets().getLeft();
                        startX += offsetX;
                        setLayoutX(startX / (itsParentNode.getParent().localToSceneTransformProperty().getValue().getMxx()));
                        setPrefWidth(newWidthSize);

                    }

                    if (resizeBottom) {
                        double newHeightSize =getBoundsInLocal().getHeight()+ offsetY / scaleY- getInsets().getBottom();
                        setPrefHeight(newHeightSize);
                        bottom.setPrefHeight(getPrefHeight());
                    }
                    if (resizeRight) {
                        double newWidthSize =getBoundsInLocal().getWidth()+ offsetX / scaleX- getInsets().getRight();
                        setPrefWidth(newWidthSize);
                    }
                    //System.out.println(getPrefWidth() + ", " + getPrefHeight());
                    setWidth(getPrefWidth());
                    setHeight(getPrefHeight());
                    
                }

                currentMouseX = e.getSceneX();
                currentMouseY = e.getSceneY();

                e.consume();
            }
        });


        onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                
               // System.out.println("onMouseMovedProperty : drag");

                
             
                double newScaleX = itsParentNode.localToSceneTransformProperty().getValue().getMxx();
                double newScaleY = itsParentNode.localToSceneTransformProperty().getValue().getMyy();
                
                
               
                
                double leftX = Math.abs(itsParentNode.getBoundsInLocal().getMinX()-e.getX());
                double leftY = Math.abs(itsParentNode.getBoundsInLocal().getMinY()-e.getY());
                double rightX = Math.abs(itsParentNode.getBoundsInLocal().getMaxX()-e.getX());
                double rightY = Math.abs(itsParentNode.getBoundsInLocal().getMaxY()-e.getY());

                

                boolean leftSide = leftX * newScaleX < 10;
                boolean topSide = leftY * newScaleY < 10;
                boolean rightSide = rightX * newScaleX < 10;
                boolean bottomSide = rightY * newScaleY < 10;

                resetResizeMode();

                
                if( dm.getState()==MakerState.RESIZING_SHAPE){
                
                    if(leftSide && !topSide && !bottomSide) {
                        resizeMode = true;
                        itsParentNode.setCursor(Cursor.W_RESIZE);
                        resizeLeft = true;
                    }else if(leftSide && !topSide && bottomSide) {
                        resizeMode = true;
                        itsParentNode.setCursor(Cursor.SW_RESIZE);
                       
                        resizeLeft = true;
                        resizeBottom = true;
                    }else if(rightSide && !topSide && !bottomSide) {
                        resizeMode = true;
                        itsParentNode.setCursor(Cursor.E_RESIZE);
                       
                        resizeRight = true;
                   
                    }else if(rightSide && !topSide && bottomSide) {
                        resizeMode = true;
                        itsParentNode.setCursor(Cursor.SE_RESIZE);
                      
                        resizeRight = true;
                        resizeBottom = true;
                  
                    }else if(bottomSide && !leftSide && !rightSide) {
                         resizeMode = true;
                        itsParentNode.setCursor(Cursor.S_RESIZE);
                       
                        resizeBottom = true;
                    }else{
                        resizeMode = false;
                       itsParentNode.setCursor(Cursor.DEFAULT); 
                    }
                }
            }
        });

        
        
        group  = new VBox();
        
//        group.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//               group.setPrefWidth((double) newValue);
//            }
//        });
        
        
        top = new VBox();
        middle = new VBox();
        bottom = new VBox();
        
        //titleString = "DefaultClass";
        isAbstractLabel = new Text("");
        title = new Text("DefaultClass");
        packageName = new Text("DefaultPackage");
        parentName = "";
        
        itsParent = null;
        itsImplements = new ArrayList<>();
        
        isAbstract = false;
        
        variables = new ArrayList();
        methods = new ArrayList();
        
        itsRelationships = new ArrayList<>();
        countConnectLines = 0;
        //Variable v0 = new Variable();
       // Method m0 = new Method();
        
      //  variables.add(v0);
      //  methods.add(m0);

        setMinSize(50, 100);
        width = 50;
        height = 100;
        startX = 300;
        startY = 300;
       
	setOpacity(1.0);
        top.getChildren().addAll(title); //packageName
       // topSide.getChildren().addAll(isAbstractLabel, title, packageName);
        reloadVariable();
        reloadMethod();
        initStyle();

        group.getChildren().addAll(top, middle, bottom);
        getChildren().add(group);
      
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    
    
    public void resetResizeMode(){
        resizeTop = false;
        resizeLeft = false;
        resizeBottom = false;
        resizeRight = false;
    }
    
    public void setDM(DataManager dm){
        this.dm = dm;
    }
    
    public void setWS(Workspace ws){
        this.ws = ws;
    }
    
    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        if(isAbstract == true){
             isAbstractLabel = new Text("{abstract}");
             if(!top.getChildren().contains(isAbstractLabel)){
                 top.getChildren().remove(title);
                // topSide.getChildren().remove(packageName);
                 top.getChildren().addAll(isAbstractLabel, title); //packageName
             }
        }else{
             isAbstractLabel = new Text("");
             top.getChildren().clear();
             //if(top.getChildren().contains(isAbstractLabel))
             top.getChildren().add(title); //packageName
        }
    }

    
    
    public DraggableClass getItsParent() {
        return itsParent;
    }

    public void setItsParent(DraggableClass itsParent) {
        this.itsParent = itsParent;
    }

    public ArrayList<DraggableInterface> getItsImplements() {
        return itsImplements;
    }

    public void setItsImplements(ArrayList<DraggableInterface> itsImplements) {
        this.itsImplements = itsImplements;
    }
    
    public void addItsImplements(DraggableInterface itsImplements) {
        this.itsImplements.add(itsImplements);
    }
    
    
    public String getPackageName() {
        return packageName.getText();
    }

    public void setPackageName(String packageName) {
        this.packageName.setText(packageName);
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;  
    }
    
    
    
    
    public VBox getGroup() {
        return group;
    }

    public void setGroup(VBox group) {
        this.group = group;
    }

    public VBox getTop() {
        return top;
    }

    public void setTop(VBox top) {
        this.top = top;
    }

    public VBox getMiddle() {
        return middle;
    }

    public void setMiddle(VBox middle) {
        this.middle = middle;
    }

    public VBox getBottom() {
        return bottom;
    }

    public void setBottom(VBox bottom) {
        this.bottom = bottom;
    }

    public String getTitle() {
        //return titleString;
        return title.getText();
    }

    public void setTitle(String title) {
        //this.titleString = title;
        this.title.setText(title);
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void reloadVariable(){
        middle.getChildren().clear();
        for (int i=0; i<variables.size(); i++) {
            middle.getChildren().add(variables.get(i));
            variables.get(i).reloaditsValues();
	}
        
      //  System.out.println(getPrefWidth() + ", " + getPrefHeight() + " ::: " + getWidth() + ", " + getHeight());
        
       setWidth(getWidth());
       setHeight(getHeight());
    }
    
    public void reloadMethod(){
         bottom.getChildren().clear();
         setIsAbstract(false);
        for (int i=0; i<methods.size(); i++) {
            bottom.getChildren().add(methods.get(i));
            methods.get(i).reloaditsValues();
            if(methods.get(i).isAbstractType()){
                
                setIsAbstract(true);
            }
	}
      // System.out.println(getPrefWidth() + ", " + getPrefHeight() + " ::: " + gettWidth() + ", " + getHeight());
        
      setWidth(getWidth());
       setHeight(getHeight());
    }
    
    public void addVariable(Variable newOne){
        //Variable v1 = new Variable();
        variables.add(newOne);
        reloadVariable();
    }
    
    public void addMethod(Method newOne){
        //Method m1 = new Method();
        methods.add(newOne);
        reloadMethod();
    }
    
    public void removeVariable(int target){
        variables.remove(target);
        reloadVariable();
    }
    
    public void removeMethod(int target){
        methods.remove(target);
        reloadMethod();
    }
    
    @Override
    public MakerState getStartingState() {
	return MakerState.STARTING_CLASS;
    }
    
    @Override
    public void start(int x, int y) {
//	startX = x;
//	startY = y;
//	setTranslateX(x);
//	setTranslateY(y);
    }

    public void initStyle() {
	top.getStyleClass().add("whitebox");
        middle.getStyleClass().add("whitebox");
        bottom.getStyleClass().add("whitebox");
        group.getStyleClass().add("bluebox");
        this.getStyleClass().add("wholebox");
    }
    
    @Override
    public void size(int x, int y) {
//	double width = x - getX();
	//widthProperty().set(width);
//	double height = y - getY();
	//heightProperty().set(height);
       // setWidth(width);
	//setHeight(height);
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	//setTranslateX(initX);
	//setTranslateY(initY);
	//setWidth(initWidth);
	//setHeight(initHeight);
    }
    /*
    @Override
    public String getShapeType() {
	return RECTANGLE;
    }
*/
   // @Override
    public double gettWidth() {
        return width;//group.getWidth(); //width
    }

    //@Override
    public double gettHeight() {
       return height;//group.getHeight(); //height;//
    }
    
     public void settWidth(Double widthsize) {
        width = widthsize;
    }

    //@Override
    public void settHeight(Double heightsize) {
       height = heightsize;
    }

    @Override
    public double getStroke() {
        group.getStyleClass().add("whitebox");
        return 0; //group.getstr
    }

    @Override
    public void setStroke() {
        group.getStyleClass().add("selectedBox");
    }

    public ArrayList<Relationship> getItsRelationships() {
        return itsRelationships;
    }

    public void setItsRelationships(ArrayList<Relationship> itsRelationships) {
        this.itsRelationships = itsRelationships;
    }

    public int getCountConnectLines() {
        return countConnectLines;
    }

    public void setCountConnectLines(int countConnectLines) {
        this.countConnectLines = countConnectLines;
    }
    
    public void addRelationship(Relationship newRelationship){
        itsRelationships.add(newRelationship);
    }
    
    public void removeRelationship(Relationship newRelationship){
        itsRelationships.remove(newRelationship);
    }

    
}
