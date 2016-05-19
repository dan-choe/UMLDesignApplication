package jcd.data;

/**
 * This interface represents a family of draggable shapes.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public interface Draggable {
    //public static final String RECTANGLE = "RECTANGLE";
    //public static final String ELLIPSE = "ELLIPSE";
    public MakerState getStartingState();
    public void start(int x, int y);
   // public void drag(int x, int y);
    public void size(int x, int y);
   // public double getX();
   // public double getY();
    public double getStroke();
    public void setStroke();
    public double getWidth();
    public double getHeight();
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight);
    //public String getShapeType();
}
