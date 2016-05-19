package jcd.data;

/**
 * This enum has the various possible states of the pose maker app.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public enum MakerState {
    SELECTING_SHAPE,
    RESIZING_SHAPE,
    
    REMOVE_SHAPE,
    
    STARTING_CLASS,
    STARTING_INTERFACE,
    
    //SIZING_SHAPE,
    
    UNDO,
    REDO,
    
    SNAP,
    NONE
}
