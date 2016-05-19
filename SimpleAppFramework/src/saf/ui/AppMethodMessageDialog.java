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
public class AppMethodMessageDialog extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static AppMethodMessageDialog singleton = null;
    
    ArrayList<Label> existedMethods;
    
    
    // HERE ARE THE DIALOG COMPONENTS
    GridPane messagePane;
    Scene messageScene;
    //Label messageLabel;
    
    Label nameLabel;
    TextField nameTextField;
    
    Label returnLabel;
    TextField returnTextField;
    
    Label accessAccessLabel;
    RadioButton accessPublicCheckBox, accessPrivateCheckBox, accessProtectedCheckBox;
    ToggleGroup toggleGroup;
    
    Label staticLabel;
    CheckBox isStatiCheckBox;
    
    Label abstractLabel;
    CheckBox isAbstractCheckBox;
    
    Label finalLabel;
    CheckBox isFinalCheckBox;
    
    Label type1Label, type2Label, type3Label;
    TextField type1TextField, type2TextField, type3TextField;
    
    Label arg1Label, arg2Label, arg3Label;
    TextField arg1TextField, arg2TextField, arg3TextField;
    
    Button completeButton;
    Button cancelButton;
    
    String name, returnType, type1, type2, type3, arg1, arg2, arg3;
    boolean isStatic, isFinal, isAbstract;
    
    
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     * @param owner The owner stage of this modal dialoge.
     * 
     * @param closeButtonText Text to appear on the close button.
     */
    private AppMethodMessageDialog() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppMethodMessageDialog getSingleton() {
	if (singleton == null)
	    singleton = new AppMethodMessageDialog();
	return singleton;
    }
    
    public void setMethodList(ArrayList existed){
        this.existedMethods = existed;
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
        returnType = "";
        isFinal = false;
        isStatic = false;
        isAbstract = false;
        
        type1 = "";
        type2 = "";
        type3 = "";
        arg1 = "";
        arg2 = "";
        arg3 = "";

        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        
        Text info = new Text("Method names are case-sensitive. Unavailable existed methods.");
        
        nameLabel = new Label("Method Name:");
        nameTextField = new TextField();
        
        returnLabel = new Label("Method Return Type:");
        returnTextField = new TextField();
        
        toggleGroup = new ToggleGroup();
        accessAccessLabel = new Label("Must Select Access Type:");
        accessPublicCheckBox = new RadioButton("Public");
        accessPrivateCheckBox = new RadioButton("Private");
        accessProtectedCheckBox = new RadioButton("Protected");
        
        //accessPublicCheckBox.setSelected(true);
      
    
        accessPublicCheckBox.setToggleGroup(toggleGroup);
        accessPrivateCheckBox.setToggleGroup(toggleGroup);
        accessProtectedCheckBox.setToggleGroup(toggleGroup);

        staticLabel = new Label("Is Static?");
        isStatiCheckBox = new CheckBox();
      
        finalLabel = new Label("Is Final?");
        isFinalCheckBox = new CheckBox();
        
        abstractLabel = new Label("Is Abstract?");
        isAbstractCheckBox = new CheckBox();
        
        Text parameterInfo = new Text("Optional Parameters:");
        
        type1Label = new Label("Parameter1 Type:");
        type1TextField = new TextField();
        
        arg1Label = new Label("Parameter1 Arg:");
        arg1TextField = new TextField();
        
        type2Label = new Label("Parameter2 Type:");
        type2TextField = new TextField();
        
        arg2Label = new Label("Parameter2 Arg:");
        arg2TextField = new TextField();
        
        type3Label = new Label("Parameter3 Type:");
        type3TextField = new TextField();
        
        arg3Label = new Label("Parameter3 Arg:");
        arg3TextField = new TextField();
        
        completeButton = new Button("Complete");
        /******************************************************************/
        completeButton.setOnAction(e->{
            
            if(!checkExistedMethods(nameTextField.getText())){
                AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                messageDialog.show("Warning","The name is already existed.");
            }else if(nameTextField.getText().equalsIgnoreCase("") 
                    || returnTextField.getText().equalsIgnoreCase("") 
                    || !checkStringValue(nameTextField.getText())
                    || !checkStringValue(returnTextField.getText())
                    || !checkStringValueStarting(nameTextField.getText())
                    || !checkStringValueStartingType(returnTextField.getText())
                    || toggleGroup.selectedToggleProperty().getValue() == null)
            {
                AppWarningMessageDialogSingleton messageDialog = AppWarningMessageDialogSingleton.getSingleton();
                messageDialog.show("Warning","Oops! Please Check Values Again.");
                
            }else{
                savingData(nameTextField.getText(), returnTextField.getText(), isStatiCheckBox.isSelected(), 
                        isFinalCheckBox.isSelected(), isAbstractCheckBox.isSelected(), type1TextField.getText(), arg1TextField.getText(),
                        type2TextField.getText(), arg2TextField.getText(), type3TextField.getText(), arg3TextField.getText());
                AppMethodMessageDialog.this.close();
            }
        });
        /******************************************************************/
        // CLOSE BUTTON
        cancelButton = new Button(CLOSE_BUTTON_LABEL);
        cancelButton.setOnAction(e->{
            resetData();
            AppMethodMessageDialog.this.close();
        });
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new GridPane();
        messagePane.setPadding(new Insets(10, 10, 10, 10));
        messagePane.setHgap(10);
        messagePane.setVgap(10);
        
        messagePane.setAlignment(Pos.CENTER);
        
        //colum, row, cols, row
        messagePane.add(info, 0, 0, 3, 1);
        
        messagePane.add(nameLabel, 0, 1, 1, 1);
        messagePane.add(nameTextField, 1, 1, 3, 1);
        messagePane.add(returnLabel, 0, 2, 1, 1);
        messagePane.add(returnTextField, 1, 2, 3, 1);
        
        messagePane.add(accessAccessLabel, 0, 3, 1, 1);
        messagePane.add(accessPublicCheckBox, 1, 3, 1, 1);
        messagePane.add(accessPrivateCheckBox, 2, 3, 1, 1);
        messagePane.add(accessProtectedCheckBox, 3, 3, 1, 1);
        
        messagePane.add(staticLabel, 0, 4, 1, 1);
        messagePane.add(isStatiCheckBox, 1, 4, 1, 1);
        messagePane.add(finalLabel, 0, 5, 1, 1);
        messagePane.add(isFinalCheckBox, 1, 5, 1, 1);
        messagePane.add(abstractLabel, 0, 6, 1, 1);
        messagePane.add(isAbstractCheckBox, 1, 6, 1, 1);
        
        messagePane.add(parameterInfo, 0, 7, 3, 1);
        
        messagePane.add(type1Label, 0, 8, 1, 1);
        messagePane.add(type1TextField, 1, 8, 3, 1);
        messagePane.add(arg1Label, 0, 9, 1, 1);
        messagePane.add(arg1TextField, 1, 9, 3, 1);
        
        messagePane.add(type2Label, 0, 10, 1, 1);
        messagePane.add(type2TextField, 1, 10, 3, 1);
        messagePane.add(arg2Label, 0, 11, 1, 1);
        messagePane.add(arg2TextField, 1, 11, 3, 1);
        
        messagePane.add(type3Label, 0, 12, 1, 1);
        messagePane.add(type3TextField, 1, 12, 3, 1);
        messagePane.add(arg3Label, 0, 13, 1, 1);
        messagePane.add(arg3TextField, 1, 13, 3, 1);
        
        messagePane.add(completeButton, 1, 14, 1, 1);
        messagePane.add(cancelButton, 2,14, 1, 1);
        

        
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
    
    public void savingData(String name, String returnType, boolean isStatic, boolean isFinal, boolean isAbstract, String type1, String arg1, String type2, String arg2, String type3, String arg3){
        this.name = name;
        this.returnType = returnType;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.type1 = type1;
        this.arg1 = arg1;
        this.type2 = type2;
        this.arg2 = arg2;
        this.type3 = type3;
        this.arg3 = arg3;
    }
    
    public void resetData(){
        name = "";
        isFinal = false;
        isStatic = false;
        isAbstract = false;
        type1 = "";
        arg1 = "";
        type2 = "";
        arg2 = "";
        type3 = "";
        arg3 = "";
        
        nameTextField.clear();
        returnTextField.clear();
        accessPublicCheckBox.setSelected(false);
        accessPrivateCheckBox.setSelected(false);
        accessProtectedCheckBox.setSelected(false);
        isStatiCheckBox.setSelected(false);
        isFinalCheckBox.setSelected(false);
        isAbstractCheckBox.setSelected(false);
        type1TextField.clear();
        arg1TextField.clear();
        type2TextField.clear();
        arg2TextField.clear();
        type3TextField.clear();
        arg3TextField.clear();
        
    }

    public String getName() {
        return name;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
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
    
    public boolean checkExistedMethods(String newName){
        for(int i=0; i<existedMethods.size(); i++){
            Label existed = existedMethods.get(i);
            if(existed.getId().equalsIgnoreCase(newName))
                return false;
        }
        return true;
    }
    
    
    
}