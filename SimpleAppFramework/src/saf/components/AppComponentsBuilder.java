package saf.components;

/**
 * This interface provides the structure required for a builder
 * object used for initializing all components for this application.
 * This is one means of employing a component hierarchy.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public interface AppComponentsBuilder {
    public AppDataComponent buildDataComponent() throws Exception;
    public AppFileComponent buildFileComponent() throws Exception;
    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception;
}
