package saf.ui;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import static saf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;

/**
 * This class serves to present custom text messages to the user when
 * events occur. Note that it always provides the same controls, a label
 * with a message, and a single ok button. 
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class AppVariableMessageDialog extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppVariableMessageDialog singleton = null;
    
    ArrayList<Label> existedVariables;
    
    // HERE ARE THE DIALOG COMPONENTS
    GridPane messagePane;
    Scene messageScene;
    //Label messageLabel;
    
    Label nameLabel;
    TextField nameTextField;
   
    Label typeLabel;
    TextField typeTextField;
    
    Label accessAccessLabel;
    RadioButton accessPublicCheckBox, accessPrivateCheckBox, accessProtectedCheckBox;
    ToggleGroup toggleGroup;
    
    Label staticLabel;
    CheckBox isStatiCheckBox;
    Label finalLabel;
    CheckBox isFinalCheckBox;
    
    Button completeButton;
    Button cancelButton;
    
    
    String name, type;
    boolean isStatic, isFinal;
    
    
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppVariableMessageDialog() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppVariableMessageDialog getSingleton() {
	if (singleton == null)
	    singleton = new AppVariableMessageDialog();
	return singleton;
    }
    
    public void setVariableList(ArrayList existed){
        this.existedVariables = existed;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        // MAKE IT MODAL
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        name = "";
        type = "";
        isFinal = false;
        isStatic = false;

        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        
        Text info = new Text("Variable names are case-sensitive. Unavailable existed variable.");
        
        nameLabel = new Label("Variable Name:");
        nameTextField = new TextField();
        
        typeLabel = new Label("Variable Type:");
        typeTextField = new TextField();
        
        toggleGroup = new ToggleGroup();
        accessAccessLabel = new Label("Must Select Access Type:");
        accessPublicCheckBox = new RadioButton("Public");
        accessPrivateCheckBox = new RadioButton("Private");
        accessProtectedCheckBox = new RadioButton("Protected");
        
        //accessPublicCheckBox.setSelected(true);
        
        accessPublicCheckBox.setToggleGroup(toggleGroup);
        accessPrivateCheckBox.setToggleGroup(toggleGroup);
        accessProtectedCheckBox.setToggleGroup(toggleGroup);
        
//        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                RadioButton chk = (RadioButton)newValue.getToggleGroup().getSelectedToggle(); // Cast object to radio button
//                //System.out.println("Selected Radio Button - "+chk.getText());
//            }
//        });

        staticLabel = new Label("Is Static?");
        isStatiCheckBox = new CheckBox();
      
        finalLabel = new Label("Is Final?");
        isFinalCheckBox = new CheckBox();
        
        completeButton = new Button("Complete");
        /******************************************************************/
        completeButton.setOnAction(e->{
            
            if(!checkExistedVariable(nameTextField.getText())){
                AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                messageDialog.show("Warning","The name is already existed.");
            }else if(nameTextField.getText().equalsIgnoreCase("") 
                    || typeTextField.getText().equalsIgnoreCase("") 
                    || !checkStringValue(nameTextField.getText())
                    || !checkStringValue(typeTextField.getText())
                    || !checkStringValueStarting(nameTextField.getText())
                    || !checkStringValueStartingType(typeTextField.getText())
                    || toggleGroup.selectedToggleProperty().getValue() == null)
            {
                AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                messageDialog.show("Warning","Oops! Please Check Values Again.");
                
            }else{
                savingData(nameTextField.getText(), typeTextField.getText(), isStatiCheckBox.isSelected(), isFinalCheckBox.isSelected());
                AppVariableMessageDialog.this.close();
            }
        });
        /******************************************************************/
        // CLOSE BUTTON
        cancelButton = new Button(CLOSE_BUTTON_LABEL);
        cancelButton.setOnAction(e->{
            resetData();
            AppVariableMessageDialog.this.close();
        });
        
        //Rectangle r = new Rectangle(50,5,50,5);
        //r.setFill(Color.RED);
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new GridPane();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.setPadding(new Insets(10, 10, 10, 10));
        messagePane.setHgap(10);
        messagePane.setVgap(10);
        
        messagePane.add(info, 0, 0, 3, 1);
        
        messagePane.add(nameLabel, 0, 1, 1, 1);
        messagePane.add(nameTextField, 1, 1, 3, 1);
        
        messagePane.add(typeLabel, 0, 2, 1, 1);
        messagePane.add(typeTextField, 1, 2, 3, 1);
                
        messagePane.add(accessAccessLabel, 0, 3, 1, 1);
        messagePane.add(accessPublicCheckBox, 1, 3, 1, 1);
        messagePane.add(accessPrivateCheckBox, 2, 3, 1, 1);
        messagePane.add(accessProtectedCheckBox, 3, 3, 1, 1);
        
        messagePane.add(staticLabel, 0, 4, 3, 1);
        messagePane.add(isStatiCheckBox, 1, 4, 3, 1);
        
        messagePane.add(finalLabel, 0, 5, 3, 1);
        messagePane.add(isFinalCheckBox, 1, 5, 3, 1);
        
        messagePane.add(completeButton, 1, 6, 1, 1);
        messagePane.add(cancelButton, 2, 6, 1, 1);
        
        // MAKE IT LOOK NICE
        //messagePane.setPadding(new Insets(20, 30, 20, 30));
       //messagePane.setSpacing(20);

        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
    }
 
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window.
     * 
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle("Add New Variable");
	
	// SET THE MESSAGE TO DISPLAY TO THE USER
        //nameLabel.setText(message);
	
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
    
    public void savingData(String name, String type, boolean isStatic, boolean isFinal){
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.isStatic = isStatic; 
    }
    
    public void resetData(){
        name = "";
        type = "";
        isFinal = false;
        isStatic = false;
        
        nameTextField.clear();
        typeTextField.clear();
        accessPublicCheckBox.setSelected(false);
        accessPrivateCheckBox.setSelected(false);
        accessProtectedCheckBox.setSelected(false);
        //toggleGroup.getSelectedToggle().setSelected(false);
        isStatiCheckBox.setSelected(false);
        isFinalCheckBox.setSelected(false);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public boolean isIsFinal() {
        return isFinal;
    }
    
    public boolean checkStringValue(String target){
        return target.matches("[a-zA-Z0-9$_?]*");
        //return target.matches("[a-zA-Z.? ]*");   [0-9]+
    }
     public boolean checkStringValueStarting(String target){
        String firstLetter = target.charAt(0)+"";
        
        if(firstLetter.matches("[0-9?]*")){
            return false;
        }
        
        if(firstLetter.matches("[A-Z?]*")){
            return false;
        }else
            return true;
    }
     
     public boolean checkStringValueStartingType(String target){
        String firstLetter = target.charAt(0)+"";
        
        if(firstLetter.matches("[0-9?]*")){
            return false;
        }else
            return true;
    }

    public String getAccess() {
        RadioButton access = (RadioButton)toggleGroup.selectedToggleProperty().getValue();
                //System.out.println("Selected Radio Button - "+chk.getText());
        //toggleGroup.selectedToggleProperty().getValue();
        return access.getText();
    }
    
    public boolean checkExistedVariable(String newName){
        try{
        for(int i=0; i<existedVariables.size(); i++){
            Label existed = existedVariables.get(i);
            if(existed.getId().equalsIgnoreCase(newName))
                return false;
        }
            
        }catch(NullPointerException e){
            
        }
        return true;
    }
    
    
    
}