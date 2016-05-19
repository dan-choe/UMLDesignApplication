package jcd.data;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Dan Choe
 */
public class Relationship extends Group {
    Group root = new Group();
    String fromBox, toBox;
    String lineType;
    
    Path typeArrow;
    Line line, line2;    
    
    double mousex = 0;
    double mousey = 0;
    double fromX, fromY, toX, toY;
    //DoubleProperty
    DoubleProperty fromBoxXDoubleProperty, fromBoxYDoubleProperty, toBoxXDoubleProperty, toBoxYDoubleProperty;
    
    DoubleBinding fromBoxXDoubleBinding, fromBoxYDoubleBinding, toBoxXDoubleBinding, toBoxYDoubleBinding;
    
    public Relationship(String startBox, String endBox, String lineType) {
        this.fromBox = startBox;
        this.toBox = endBox;
        this.lineType = lineType;
        this.fromX = 200;
        this.fromY = 200;
        this.toX = 200;
        this.toY = 200;
        
        typeArrow = new Path();
        line = new Line();
        line2 = new Line();
       
        
       // line = createLine(fromX, fromY, toX, toY);
        
        
    }
    
    public void createLine(){
       // Line line = new Line();
       
       if(lineType.equals("association")){
            typeArrow = getAssociationArrow(fromBoxXDoubleBinding, fromBoxYDoubleBinding);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(1);

            line.startXProperty().bind(fromBoxXDoubleBinding);
            line.startYProperty().bind(fromBoxYDoubleBinding);

            line.endXProperty().bind(toBoxXDoubleBinding);
            line.endYProperty().bind(toBoxYDoubleBinding);



            getChildren().addAll(typeArrow,line);
        }else if(lineType.equals("inheritance") || lineType.equals("realization")){
            typeArrow = getInheritanceArrow(toBoxXDoubleBinding, toBoxYDoubleBinding);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(0);

            line.startXProperty().bind(fromBoxXDoubleBinding);
            line.startYProperty().bind(fromBoxYDoubleBinding);

            line.endXProperty().bind(toBoxXDoubleBinding);
            line.endYProperty().bind(toBoxYDoubleBinding);


            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(1);

            line2.startXProperty().bind(fromBoxXDoubleBinding);
            line2.startYProperty().bind(fromBoxYDoubleBinding);

            line2.endXProperty().bind(toBoxXDoubleBinding);
            line2.endYProperty().bind(toBoxYDoubleBinding.add(15));

            getChildren().addAll(typeArrow,line,line2);
        }else if(lineType.equals("aggregation")){
            typeArrow = getAggregationArrow(toBoxXDoubleBinding, toBoxYDoubleBinding);
              line.setStroke(Color.BLACK);
            line.setStrokeWidth(1);

            line.startXProperty().bind(fromBoxXDoubleBinding);
            line.startYProperty().bind(fromBoxYDoubleBinding);

            line.endXProperty().bind(toBoxXDoubleBinding);
            line.endYProperty().bind(toBoxYDoubleBinding);


            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(1);


            if(lineType.equals("realization")|| lineType.equals("dependancy")){
                line2.getStrokeDashArray().addAll(10d, 10d);
            }

            line2.startXProperty().bind(fromBoxXDoubleBinding);
            line2.startYProperty().bind(fromBoxYDoubleBinding);

            line2.endXProperty().bind(toBoxXDoubleBinding);
            line2.endYProperty().bind(toBoxYDoubleBinding.add(15));

            getChildren().addAll(typeArrow,line);
        }else if(lineType.equals("dependancy")){
            typeArrow = getAggregationArrow(toBoxXDoubleBinding, toBoxYDoubleBinding);
              line.setStroke(Color.BLACK);
            line.setStrokeWidth(1);

            line.startXProperty().bind(fromBoxXDoubleBinding);
            line.startYProperty().bind(fromBoxYDoubleBinding);

            line.endXProperty().bind(toBoxXDoubleBinding);
            line.endYProperty().bind(toBoxYDoubleBinding);


            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(1);


            if(lineType.equals("realization")|| lineType.equals("dependancy")){
                line2.getStrokeDashArray().addAll(10d, 10d);
            }

            line2.startXProperty().bind(fromBoxXDoubleBinding);
            line2.startYProperty().bind(fromBoxYDoubleBinding);

            line2.endXProperty().bind(toBoxXDoubleBinding);
            line2.endYProperty().bind(toBoxYDoubleBinding.add(15));

            getChildren().addAll(typeArrow,line);
        }else{
            /*realization*/
            typeArrow = getAssociationArrow(toBoxXDoubleBinding, toBoxYDoubleBinding);
             line.setStroke(Color.BLACK);
            line.setStrokeWidth(1);

            line.startXProperty().bind(fromBoxXDoubleBinding);
            line.startYProperty().bind(fromBoxYDoubleBinding);

            line.endXProperty().bind(toBoxXDoubleBinding);
            line.endYProperty().bind(toBoxYDoubleBinding);


            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(1);


            if(lineType.equals("realization")|| lineType.equals("dependancy")){
                line2.getStrokeDashArray().addAll(10d, 10d);
            }

            line2.startXProperty().bind(fromBoxXDoubleBinding);
            line2.startYProperty().bind(fromBoxYDoubleBinding);

            line2.endXProperty().bind(toBoxXDoubleBinding);
            line2.endYProperty().bind(toBoxYDoubleBinding.add(15));

            getChildren().addAll(typeArrow,line);
        }
  }
    
/*
    public Line createLine(DoubleProperty startx, DoubleProperty starty, DoubleProperty endx, DoubleProperty endy){
       // Line line = new Line();
       
        line.setStroke(Color.FORESTGREEN);
        line.setStrokeWidth(4);
       
        line.startXProperty().bind(startx);
        line.startYProperty().bind(starty);
                                             
        line.endXProperty().bind(endx);
        line.endYProperty().bind(endy);
        
        return line;
  }
  */  
    
    
    
    //black dia
    public Path getCompositionArrow(DoubleBinding startx, DoubleBinding starty){
        MoveTo moveTo = new MoveTo(toX,toY);
        moveTo.xProperty().bind(line.endXProperty());
        moveTo.yProperty().bind(line.endYProperty());
        LineTo lineTo = new LineTo(toX-15,toY+15);
        lineTo.xProperty().bind(line.endXProperty().subtract(15));
        lineTo.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo1 = new LineTo(toX,toY+30);
        lineTo1.xProperty().bind(line.endXProperty());
        lineTo1.yProperty().bind(line.endYProperty().add(30));
        LineTo lineTo2 = new LineTo(toX+15,toY+15);
        lineTo2.xProperty().bind(line.endXProperty().add(15));
        lineTo2.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo3 = new LineTo(toX,toY);
        lineTo3.xProperty().bind(line.endXProperty());
        lineTo3.yProperty().bind(line.endYProperty());

        
        Path pathTriangle=new Path(
                moveTo,
                lineTo,
                moveTo,
                lineTo1,
                lineTo2,
                lineTo3
        );
        pathTriangle.setFill(Color.BLACK);
        pathTriangle.setStroke(Color.BLACK);
        //connecting point : (startx, startx)
        return pathTriangle;
    }
    //blank dia
    public Path getAggregationArrow(DoubleBinding startx, DoubleBinding starty){
        
        MoveTo moveTo = new MoveTo(toX,toY);
        moveTo.xProperty().bind(line.endXProperty());
        moveTo.yProperty().bind(line.endYProperty());
        LineTo lineTo = new LineTo(toX-15,toY+15);
        lineTo.xProperty().bind(line.endXProperty().subtract(15));
        lineTo.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo1 = new LineTo(toX,toY+30);
        lineTo1.xProperty().bind(line.endXProperty());
        lineTo1.yProperty().bind(line.endYProperty().add(30));
        LineTo lineTo2 = new LineTo(toX+15,toY+15);
        lineTo2.xProperty().bind(line.endXProperty().add(15));
        lineTo2.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo3 = new LineTo(toX,toY);
        lineTo3.xProperty().bind(line.endXProperty());
        lineTo3.yProperty().bind(line.endYProperty());

        
        Path pathTriangle=new Path(
                moveTo,
                lineTo,
                moveTo,
                lineTo1,
                lineTo2,
                lineTo3
        );
        //pathTriangle.setFill(Color.BLACK);
        pathTriangle.setStroke(Color.BLACK);
        //connecting point : (startx, startx)
        return pathTriangle;
    }

    // simple arrow
    public Path getAssociationArrow(DoubleBinding startx, DoubleBinding starty){
        MoveTo moveTo = new MoveTo(toX,toY);
        moveTo.xProperty().bind(line.endXProperty());
        moveTo.yProperty().bind(line.endYProperty());
        LineTo lineTo = new LineTo(toX-15,toY+15);
        lineTo.xProperty().bind(line.endXProperty().subtract(15));
        lineTo.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo1 = new LineTo(toX+15,toY+15);
        lineTo1.xProperty().bind(line.endXProperty().add(15));
        lineTo1.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo2 = new LineTo(toX,toY);
        lineTo2.xProperty().bind(line.endXProperty());
        lineTo2.yProperty().bind(line.endYProperty());

        
        Path pathTriangle=new Path(
                moveTo,
                lineTo,
                moveTo,
                lineTo1,
                lineTo2
        );
        
        pathTriangle.setStroke(Color.BLACK);
        //connecting point : (startx, startx)
        return pathTriangle;
    }
    // simple arrow  dependancy
    public Path getDependancyArrow(DoubleBinding startx, DoubleBinding starty){
        MoveTo moveTo = new MoveTo(fromX,fromY);
        moveTo.xProperty().bind(line.startXProperty());
        moveTo.yProperty().bind(line.startYProperty());
        LineTo lineTo = new LineTo(fromX-15,fromY+15);
        lineTo.xProperty().bind(line.startXProperty().subtract(15));
        lineTo.yProperty().bind(line.startYProperty().add(15));
        LineTo lineTo1 = new LineTo(fromX+15,fromY+15);
        lineTo1.xProperty().bind(line.startXProperty().add(15));
        lineTo1.yProperty().bind(line.startYProperty().add(15));
        LineTo lineTo2 = new LineTo(fromX,fromY);
        lineTo2.xProperty().bind(line.startXProperty());
        lineTo2.yProperty().bind(line.startYProperty());

        
        Path pathTriangle=new Path(
                moveTo,
                lineTo,
                moveTo,
                lineTo1,
                lineTo2
        );
        
       
        pathTriangle.setFill(Color.BLACK);
        pathTriangle.setStroke(Color.BLACK);
        //connecting point : (startx, startx)
        return pathTriangle;
    }
    
    // simple triangle extends  inheritance
    public Path getInheritanceArrow(DoubleBinding startx, DoubleBinding starty){
        
        MoveTo moveTo = new MoveTo(toX,toY);
        moveTo.xProperty().bind(line.endXProperty());
        moveTo.yProperty().bind(line.endYProperty());
        LineTo lineTo = new LineTo(toX-15,toY+15);
        lineTo.xProperty().bind(line.endXProperty().subtract(15));
        lineTo.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo1 = new LineTo(toX+15,toY+15);
        lineTo1.xProperty().bind(line.endXProperty().add(15));
        lineTo1.yProperty().bind(line.endYProperty().add(15));
        LineTo lineTo2 = new LineTo(toX,toY);
        lineTo2.xProperty().bind(line.endXProperty());
        lineTo2.yProperty().bind(line.endYProperty());

        
        Path pathTriangle=new Path(
                moveTo,
                lineTo,
                lineTo1,
                lineTo2
        );
        /*
        Path pathTriangle=new Path(
                new MoveTo(startx.get(),starty.get()),
                new LineTo(startx.get()-15,starty.get()+15),
                new LineTo(startx.get()+15,starty.get()+15),
                new LineTo(startx.get(),starty.get())
        );
        */
     //pathTriangle.layoutXProperty().bind(line.endXProperty());
     //pathTriangle.layoutYProperty().bind(line.endYProperty());
     
     
     
     
     pathTriangle.setFill(Color.WHITE);
        pathTriangle.setStroke(Color.BLACK);
        //connecting point : (startx, startx)
        return pathTriangle;
    }

    public DoubleProperty getFromBoxXDoubleProperty() {
        return fromBoxXDoubleProperty;
    }

    public void setFromBoxXDoubleProperty(DoubleProperty fromBoxXDoubleProperty) {
        this.fromBoxXDoubleProperty = fromBoxXDoubleProperty;
    }

    public DoubleProperty getFromBoxYDoubleProperty() {
        return fromBoxYDoubleProperty;
    }

    public void setFromBoxYDoubleProperty(DoubleProperty fromBoxYDoubleProperty) {
        this.fromBoxYDoubleProperty = fromBoxYDoubleProperty;
    }

    public DoubleProperty getToBoxXDoubleProperty() {
        return toBoxXDoubleProperty;
    }

    public void setToBoxXDoubleProperty(DoubleProperty toBoxXDoubleProperty) {
        this.toBoxXDoubleProperty = toBoxXDoubleProperty;
    }

    public DoubleProperty getToBoxYDoubleProperty() {
        return toBoxYDoubleProperty;
    }

    public void setToBoxYDoubleProperty(DoubleProperty toBoxYDoubleProperty) {
        this.toBoxYDoubleProperty = toBoxYDoubleProperty;
    }
    
    
    

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public Path getTypeArrow() {
        return typeArrow;
    }

    public void setTypeArrow(Path typeArrow) {
        this.typeArrow = typeArrow;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getFromBox() {
        return fromBox;
    }

    public void setFromBox(String fromBox) {
        this.fromBox = fromBox;
    }

    public String getToBox() {
        return toBox;
    }

    public void setToBox(String toBox) {
        this.toBox = toBox;
    }

    public double getFromX() {
        return fromX;
    }

    public void setFromX(double fromX) {
        this.fromX = fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public void setFromY(double fromY) {
        this.fromY = fromY;
    }

    public double getToX() {
        return toX;
    }

    public void setToX(double toX) {
        this.toX = toX;
    }

    public double getToY() {
        return toY;
    }

    public void setToY(double toY) {
        this.toY = toY;
    }

    public DoubleBinding getFromBoxXDoubleBinding() {
        return fromBoxXDoubleBinding;
    }

    public void setFromBoxXDoubleBinding(DoubleBinding fromBoxXDoubleBinding) {
        this.fromBoxXDoubleBinding = fromBoxXDoubleBinding;
    }

    public DoubleBinding getFromBoxYDoubleBinding() {
        return fromBoxYDoubleBinding;
    }

    public void setFromBoxYDoubleBinding(DoubleBinding fromBoxYDoubleBinding) {
        this.fromBoxYDoubleBinding = fromBoxYDoubleBinding;
    }

    public DoubleBinding getToBoxXDoubleBinding() {
        return toBoxXDoubleBinding;
    }

    public void setToBoxXDoubleBinding(DoubleBinding toBoxXDoubleBinding) {
        this.toBoxXDoubleBinding = toBoxXDoubleBinding;
    }

    public DoubleBinding getToBoxYDoubleBinding() {
        return toBoxYDoubleBinding;
    }

    public void setToBoxYDoubleBinding(DoubleBinding toBoxYDoubleBinding) {
        this.toBoxYDoubleBinding = toBoxYDoubleBinding;
    }

    
    
    
    
    
}
