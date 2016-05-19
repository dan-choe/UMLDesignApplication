package saf.settings;

/**
 * This enum provides properties that are to be loaded via
 * XML files to be used for setting up the application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public enum AppPropertyType {
        // LOADED FROM simple_app_properties.xml
        APP_TITLE,
	APP_LOGO,
	APP_CSS,
	APP_PATH_CSS,

        // APPLICATION ICONS
        NEW_ICON,
        LOAD_ICON,
        SAVE_ICON,
	SAVEAS_ICON,
        PHOTO_ICON,
        CODE_ICON,
        EXIT_ICON,
        
        SELECT_ICON,
        RESIZE_ICON,
        ADDC_ICON,
        ADDI_ICON,
        REMOVE_ICON,
        UNDO_ICON,
        REDO_ICON,
        
        ZOOMIN_ICON,
        ZOOMOUT_ICON,
        GRID_ICON,
        SNAP_ICON,
        
        // APPLICATION TOOLTIPS FOR BUTTONS
        NEW_TOOLTIP,
        LOAD_TOOLTIP,
        SAVE_TOOLTIP,
	SAVEAS_TOOLTIP,
        PHOTO_TOOLTIP,
        CODE_TOOLTIP,
	EXPORT_TOOLTIP,
        EXIT_TOOLTIP,

	// ERROR MESSAGES
	NEW_ERROR_MESSAGE,
	LOAD_ERROR_MESSAGE,
	SAVE_ERROR_MESSAGE,
        PHOTO_ERROR_MESSAGE,
        CODE_ERROR_MESSAGE,
	PROPERTIES_LOAD_ERROR_MESSAGE,

	// ERROR TITLES
	NEW_ERROR_TITLE,
	LOAD_ERROR_TITLE,
	SAVE_ERROR_TITLE,
        PHOTO_ERROR_TITLE,
        CODE_ERROR_TITLE,
	PROPERTIES_LOAD_ERROR_TITLE,

	// AND VERIFICATION MESSAGES AND TITLES
        NEW_COMPLETED_MESSAGE,
	NEW_COMPLETED_TITLE,
        LOAD_COMPLETED_MESSAGE,
	LOAD_COMPLETED_TITLE,
        SAVE_COMPLETED_MESSAGE,
	SAVE_COMPLETED_TITLE,
	SAVE_UNSAVED_WORK_TITLE,
        SAVE_UNSAVED_WORK_MESSAGE,
        
	SAVE_WORK_TITLE,
	LOAD_WORK_TITLE,
	WORK_FILE_EXT,
	WORK_FILE_EXT_DESC,
	PROPERTIES_
}
