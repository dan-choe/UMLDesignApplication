package jcd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.reflect.Modifier.INTERFACE;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import jcd.data.DataManager;
//import jcd.data.DraggableEllipse;
//import jcd.data.DraggableRectangle;
import jcd.data.Draggable;
//import static jcd.data.Draggable.RECTANGLE;
import jcd.data.DraggableClass;
import jcd.data.DraggableInterface;
import jcd.data.Method;
import jcd.data.Variable;
import jcd.data.DraggableClass;
import jcd.data.DraggableInterface;
import jcd.data.Method;
import jcd.data.Variable;
import saf.ui.AppMessageDialogSingleton;
import java.lang.Object.*;
import jcd.data.Relationship;
//import com.sun.codemodel.JDefinedClass;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    /**
     *
     * @param data
     * @param filePathS
     */
    @Override
    public void exportCodeAsFile(AppDataComponent data, String filePath) {
        System.out.println("exportCodeAsFile " + filePath);
        
        DataManager dm = (DataManager) data;
        
        try {
            for(int i=0; i<dm.getShapes().size(); i++){
                Node chosenBox = dm.getShapes().get(i);
                
                String filename = ""; // filename inappropriate check
                String content = "";
                String packageName = "";
                
                if(chosenBox instanceof DraggableClass){
                    DraggableClass temp = (DraggableClass) chosenBox;
                    
                    packageName = temp.getPackageName();
                    
                    String[] eachParts = packageName.split("[\\.]");
                    
                    //if(eachParts.length>0){
                        
                        String currentfilepath = "";
                        String customfilePath = filePath;
                        for(int j=0; j<eachParts.length; j++){
                            if(!eachParts[0].equalsIgnoreCase("javafx") && !eachParts[0].equalsIgnoreCase("java")){
                                currentfilepath = customfilePath+"/"+eachParts[j];
                                File exportTemp = new File(currentfilepath);
                                if (!exportTemp.exists()) { //&& checkInterruptedCSSfolder == false
                                    exportTemp.mkdir();
                                }
                                customfilePath += "/"+eachParts[j];
                            }
                        }
                        if(!eachParts[0].equalsIgnoreCase("javafx") && !eachParts[0].equalsIgnoreCase("java")){
                            File exportJava = new File(currentfilepath);
                            exportJava.createNewFile();

                            filename = temp.getTitle();
                            content = readClassUMLAsJavaFile(temp);
                            PrintWriter out = new PrintWriter(currentfilepath+"/"+filename+".java");
                            out.print(content);
                            out.close();
                        }
                }else{
                    DraggableInterface temp = (DraggableInterface) chosenBox;
                    filename = temp.getTitle();
 
                    packageName = temp.getPackageName();
                    
                    String[] eachParts = packageName.split("[\\.]");
                    
                    //if(eachParts.length>0){
                        
                        String currentfilepath = "";
                        String customfilePath = filePath;
                        for(int j=0; j<eachParts.length; j++){
                            if(!eachParts[0].equalsIgnoreCase("javafx") && !eachParts[0].equalsIgnoreCase("java")){
                                currentfilepath = customfilePath+"/"+eachParts[j];
                                File exportTemp = new File(currentfilepath);
                                if (!exportTemp.exists()) { //&& checkInterruptedCSSfolder == false
                                    exportTemp.mkdir();
                                }
                                customfilePath += "/"+eachParts[j];
                            }
                        }
                        if(!eachParts[0].equalsIgnoreCase("javafx") && !eachParts[0].equalsIgnoreCase("java")){
                            File exportJava = new File(currentfilepath);
                            exportJava.createNewFile();

                            filename = temp.getTitle();
                            content = readInterfaceUMLAsJavaFile(temp);
                            PrintWriter out = new PrintWriter(currentfilepath+"/"+filename+".java");
                            out.print(content);
                            out.close();
                        }
                }

                
            }
            //
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String readClassUMLAsJavaFile(DraggableClass chosenBox) {
        //package jcd.test_bed;
        //public abstract class AppTemplate extends Application {
        String abstractAsString = " abstract ";
        String extendsAsString = " extends ";
        String implementsAsString = " implements ";
        
        String content = "package " + chosenBox.getPackageName()+ ";\n\n";
        
        ArrayList<String> importCheckedClass = new ArrayList<>();
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0; // ThreadExample
        int g = 0; // Thread
        
        for(int i=0; i<chosenBox.getVariables().size(); i++){

                //Class tempClass = Class.forName(chosenBox.getVariables().get(i).getType());
                if(!(chosenBox.getVariables().get(i).getType()).equalsIgnoreCase("String") 
                        || !(chosenBox.getVariables().get(i).getType()).equalsIgnoreCase("Double")
                        || !(chosenBox.getVariables().get(i).getType()).equalsIgnoreCase("int")
                        || !(chosenBox.getVariables().get(i).getType()).equalsIgnoreCase("Integer")
                        ||!(chosenBox.getVariables().get(i).getType()).equalsIgnoreCase("boolean")){
 
                    
                        String type = chosenBox.getVariables().get(i).getType();
                        
                        if(type.equalsIgnoreCase("Button") 
                                || type.equalsIgnoreCase("Label") || type.equalsIgnoreCase("TextArea")
                                || type.equalsIgnoreCase("Text")){ 
                            if(a == 0){
                            type = "javafx.scene.control.*";
                            content += "import " + type+";\n";
                            a++;
                            }
                        }
                        
                        else if(type.equalsIgnoreCase("VBox") || type.equalsIgnoreCase("BorderPane")
                                || type.equalsIgnoreCase("FlowPane") || type.equalsIgnoreCase("ScrollPane") 
                                || type.equalsIgnoreCase("HBox") || type.equalsIgnoreCase("Pane")){
                            if(b == 0){
                            type = "javafx.scene.layout.*";
                            content += "import " + type+";\n";
                            b++;
                            }
                        }
                        
                        else if(type.equalsIgnoreCase("Date")){
                            if(c == 0){
                            type = "java.util.Date";
                            content += "import " + type+";\n";
                            c++;
                            }
                        }
                        
                        else if(type.equalsIgnoreCase("Task")){
                            if(d == 0){
                            type = "javafx.concurrent.Task";
                            content += "import " + type+";\n";
                            d++;
                            }
                        }
                        
                        else if(type.equalsIgnoreCase("Stage")){
                            if(e == 0){
                            type = "javafx.stage.Stage";
                            content += "import " + type+";\n";
                            type = "javafx.application.Application";
                            content += "import " + type+";\n";
                            type = "javafx.scene.Scene";
                            content += "import " + type+";\n";
                            e++;
                            }
                        }else{
                            if(type.equals("ThreadExample"))
                                content += "import " + type+";\n";
                            
                           // DataManager
                            
                           // chosenBox.getVariables().get(i).getType();
                            
                            System.err.println(type);
                        }
                       
                        //ClassLoader loader = new NetworkClassLoader(host, port);
                        //Object main = loader.loadClass("Main", true).newInstance();
                        
                        //Class tt = Class.forName(type);
                        //SimpleClassLoader sc = new SimpleClassLoader(); 
                       
                        //Class tt = Class.forName("javax.swing.JButton");
                        
                        
                        //    Object obj = tempClass.newInstance();
//                            Package pack = obj.getClass().getPackage();
                }

        }
        
       content += "\n";
        
        // CHECK ABSTART
        if(chosenBox.isIsAbstract())
            content += "public" + abstractAsString + " class " + chosenBox.getTitle();
        else
            content += "public" + " class " + chosenBox.getTitle();
        
        // CHECK EXTENDS
        if(!chosenBox.getParentName().equals(""))
            content += extendsAsString + chosenBox.getParentName();
        
        // CHECK IMPLEMENTS
        if(chosenBox.getItsImplements().size()!=0){
            content += implementsAsString;
            for(int i=0; i<chosenBox.getItsImplements().size(); i++){
                content += chosenBox.getItsImplements().get(i).getTitle() + ", ";    
            }
            content = content.substring(0, content.length()-2);
        }
        
        content += " {\n\n"; //open class
        
        // LOAD VARIABLE
        if(chosenBox.getVariables().size()!=0){
            for(int i=0; i<chosenBox.getVariables().size(); i++){
                content += "    "+chosenBox.getVariables().get(i).getAccess()+" "
                        +chosenBox.getVariables().get(i).getStaticAsStrings()+" "
                        +chosenBox.getVariables().get(i).getFinalAsStrings()+" "
                        +chosenBox.getVariables().get(i).getType()+" " 
                        +chosenBox.getVariables().get(i).getName()+ ";\n";    
            }
            //content = content.substring(0, content.length()-1);
        }
        
        if(chosenBox.getMethods().size()!=0){
            for(int i=0; i<chosenBox.getMethods().size(); i++){
                String checkstatic = "";
                String checkabstract = "";
                if(chosenBox.getMethods().get(i).isStaticType()) checkstatic = "static ";
                if(chosenBox.getMethods().get(i).isAbstractType()) checkabstract = "abstract";
                
                content += "    "+chosenBox.getMethods().get(i).getAccess()+" "
                        +checkstatic
                        +checkabstract
                        +" "
                        +chosenBox.getMethods().get(i).getReturnType()+" "
                        +chosenBox.getMethods().get(i).getName()+"("
                        +chosenBox.getMethods().get(i).getType1()+" "
                        +chosenBox.getMethods().get(i).getArg1()+") {\n";
                
                if(!chosenBox.getMethods().get(i).getReturnType().equals("void")||!chosenBox.getMethods().get(i).getReturnType().equals("")){
                    content += "        return null\n";
                }
                content += "    }\n";
            }
            
            
        }
        
        
        content += "}";  //close class
        return content;
    }
    
    
    public String readInterfaceUMLAsJavaFile(DraggableInterface chosenBox){
        //package jcd.test_bed;
        //public abstract class AppTemplate extends Application {
       // String abstractAsString = " abstract ";
        String extendsAsString = " extends ";
       // String implementsAsString = " implements ";
        
        String content = "package " + chosenBox.getPackageName()+ ";\n\n";
        
        content += "public" + " interface " + chosenBox.getTitle();

   
        // CHECK EXTENDS
        if(chosenBox.getItsParents().size()!=0){
            content += extendsAsString;
            for(int i=0; i<chosenBox.getItsParents().size(); i++){
                content += chosenBox.getItsParents().get(i).getTitle() + ", ";    
            }
            content = content.substring(0, content.length()-2);
        }
        
        content += " {\n\n"; //open class
        
        // LOAD VARIABLE
        if(chosenBox.getVariables().size()!=0){
            for(int i=0; i<chosenBox.getVariables().size(); i++){
                content += "    "+chosenBox.getVariables().get(i).getAccess()+" "
                        +chosenBox.getVariables().get(i).getStaticAsStrings()+" "
                        +chosenBox.getVariables().get(i).getFinalAsStrings()+" "
                        +chosenBox.getVariables().get(i).getType()+" " 
                        +chosenBox.getVariables().get(i).getName()+ ";\n";    
            }
            //content = content.substring(0, content.length()-1);
        }
        
        if(chosenBox.getMethods().size()!=0){
            for(int i=0; i<chosenBox.getMethods().size(); i++){
                //String checkstatic = "";
                //if(chosenBox.getMethods().get(i).isStaticType()) checkstatic = "static";
                
                content += "    "+chosenBox.getMethods().get(i).getAccess()+" "
               //         +checkstatic
                        +" "
                        +chosenBox.getMethods().get(i).getReturnType()+" "
                        +chosenBox.getMethods().get(i).getName()+"("
                        +chosenBox.getMethods().get(i).getType1()+" "
                        +chosenBox.getMethods().get(i).getArg1()+");\n";
            }
            
            
        }
        
        
        content += "}";  //close class
        return content;
    }
 
    
     /*
            File exportTemp = new File(PATH_CSS);
            if (!exportTemp.exists() && checkInterruptedCSSfolder == false) {
            exportTemp.mkdir();
            
            
            File exportCSS = new File(TEMP_CSS_PATH);
            if (!exportCSS.exists() && checkInterruptedCSS == false) {
                exportCSS.createNewFile();
                checkInterruptedCSS = true;
                //System.out.println("2. CSS is created. filePath : " + TEMP_CSS_PATH);
            }else if (!exportCSS.exists() && checkInterruptedCSS == true){
                exportCSS.createNewFile();
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show("Warning", "Do not delete css file.");
            */
    
    
    
    
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        
        StringWriter sw = new StringWriter();
        //filePath = filePath+".json";
        
	// BUILD THE HTMLTags ARRAY
	DataManager dataManager = (DataManager)data;

	// THEN THE TREE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	ArrayList<Node> root = dataManager.getShapes(); //ObservableList
	
        //System.out.println("saveData "+ root.size());
        
            fillArrayWithTreeTags(root, arrayBuilder);
            JsonArray nodesArray = arrayBuilder.build();

            // THEN PUT IT ALL TOGETHER IN A JsonObject
            JsonObject dataManagerJSO = Json.createObjectBuilder()
                    .add("canvas", nodesArray)
                    .add("bg", "#dddddd") //dataManager.getCSSText()
                    .build();

            // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            JsonWriter jsonWriter = writerFactory.createWriter(sw);
            jsonWriter.writeObject(dataManagerJSO);
            jsonWriter.close();

            // INIT THE WRITER
            OutputStream os = new FileOutputStream(filePath);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(filePath);
            pw.write(prettyPrinted);
            pw.close();
      
    }
    
    public JsonObject makeJsonColorObject(Color color) {
	JsonObject colorJson = Json.createObjectBuilder()
		.add(JSON_RED, color.getRed())
		.add(JSON_GREEN, color.getGreen())
		.add(JSON_BLUE, color.getBlue())
		.add(JSON_ALPHA, color.getOpacity()).build();
	return colorJson;
    }
      
    
            
   
            
    private void fillArrayWithTreeTags(ArrayList<Node> root, JsonArrayBuilder arrayBuilder) {
        for (int i = 0; i< root.size(); i++) {
            //if(root.get(i).)
            if(root.get(i) instanceof DraggableClass){
                DraggableClass childData = (DraggableClass)root.get(i);
                arrayBuilder.add(makeClassJsonObject(childData, root.size()));
            }
            else{
                DraggableInterface childData = (DraggableInterface)root.get(i);
                arrayBuilder.add(makeInterfaceJsonObject(childData, root.size()));
            }
	    //DraggableClass childData = (DraggableClass)root.get(i);
	   
	}
    }

    
    private void fillArrayWithTreeTagsVariable(ArrayList<Variable> root, JsonArrayBuilder arrayBuilder) {
        for (int i = 0; i< root.size(); i++) {
	    Variable childData = (Variable)root.get(i);
	    arrayBuilder.add(makeVariableJsonObject(childData));
	}
    }
    
    private void fillArrayWithTreeTagsMethod(ArrayList<Method> root, JsonArrayBuilder arrayBuilder) {
        for (int i = 0; i< root.size(); i++) {
	    Method childData = (Method)root.get(i);
	    arrayBuilder.add(makeMethodJsonObject(childData));
	}
    }
     
    private void fillArrayWithTreeTagsImplement(ArrayList<DraggableInterface> root, JsonArrayBuilder arrayBuilder) {
        for (int i = 0; i< root.size(); i++) {
	    DraggableInterface childData = (DraggableInterface)root.get(i);
	    arrayBuilder.add(makeImplementJsonObject(childData));
	}
    }
    
    private void fillArrayWithTreeTagsRelationship(ArrayList<Relationship> root, JsonArrayBuilder arrayBuilder) {
        for (int i = 0; i< root.size(); i++) {
	    Relationship childData = (Relationship)root.get(i);
	    arrayBuilder.add(makeRelationshipJsonObject(childData));
	}
    }
    
    
  
   
    
  
     // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonObject makeClassJsonObject(DraggableClass tag, int numChildren) {
        //DraggableClass tagdc = tag;
        // THEN THE TREE
	JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
	ArrayList<Variable> variableArray = tag.getVariables();
        fillArrayWithTreeTagsVariable(variableArray, arrayBuilder1);
	JsonArray variableJsonArray = arrayBuilder1.build();
        
        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
	ArrayList<Method> methodArray = tag.getMethods();
        fillArrayWithTreeTagsMethod(methodArray, arrayBuilder2);
	JsonArray methodJsonArray = arrayBuilder2.build();
        
        JsonArrayBuilder arrayBuilder3 = Json.createArrayBuilder();
	ArrayList<DraggableInterface> implementArray = tag.getItsImplements();
        fillArrayWithTreeTagsImplement(implementArray, arrayBuilder3);
	JsonArray implementJsonArray = arrayBuilder3.build();
        
        JsonArrayBuilder arrayBuilder4 = Json.createArrayBuilder();
	ArrayList<Relationship> relationshipsArray = tag.getItsRelationships();
        fillArrayWithTreeTagsRelationship(relationshipsArray, arrayBuilder4);
	JsonArray relationshipJsonArray = arrayBuilder4.build();
        
	JsonObject jso = Json.createObjectBuilder()
                .add("type", "class")
		.add("title", tag.getTitle())//tag.getShapeName()
                .add("package", tag.getPackageName())//tag.getFillColorString()
                .add("parent", tag.getParentName())
                .add("implements", implementJsonArray)
                .add("isAbstract", tag.isIsAbstract()+"")
                .add("variable", variableJsonArray)
                .add("method", methodJsonArray)
                
                .add("positionX", tag.getStartX()+"")
                .add("positionY", tag.getStartY()+"")
                .add("width", tag.getWidth()+"") //gettWidth
                .add("height", tag.getHeight()+"")
                .add("relationship", relationshipJsonArray)
		.build();
        
        //System.out.println("jcd.file.FileManager.makeClassJsonObject()  " + tag.getWidth());
        
	return jso;
    }
    
    private JsonObject makeInterfaceJsonObject(DraggableInterface tag, int numChildren) {
        //DraggableClass tagdc = tag;
        // THEN THE TREE
	JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
	ArrayList<Method> methodArray = tag.getMethods();
        fillArrayWithTreeTagsMethod(methodArray, arrayBuilder2);
	JsonArray methodJsonArray = arrayBuilder2.build();
        
        JsonArrayBuilder arrayBuilder4 = Json.createArrayBuilder();
	ArrayList<Relationship> relationshipsArray = tag.getItsRelationships();
        fillArrayWithTreeTagsRelationship(relationshipsArray, arrayBuilder4);
	JsonArray relationshipJsonArray = arrayBuilder4.build();
        
	JsonObject jso = Json.createObjectBuilder()
                .add("type", "interface")
		.add("title", tag.getTitle())//tag.getShapeName()
                .add("package", tag.getPackageName())//tag.getFillColorString()
                //.add("parent", tag.getParentName())
                
               
                .add("method", methodJsonArray)
                
                .add("positionX", tag.getStartX()+"")
                .add("positionY", tag.getStartY()+"")
                .add("width", tag.getWidth()+"")
                .add("height", tag.getHeight()+"")
                .add("relationship", relationshipJsonArray)
		.build();
	return jso;
    }
    
    
    
    private JsonObject makeImplementJsonObject(DraggableInterface tag) {

        //Interface can have extended multiple interface
        JsonArrayBuilder arrayBuilder3 = Json.createArrayBuilder();
	ArrayList<DraggableInterface> interfaceArray = tag.getItsParents();
        fillArrayWithTreeTagsImplement(interfaceArray, arrayBuilder3);
	JsonArray interfaceJsonArray = arrayBuilder3.build();
        
	JsonObject jso = Json.createObjectBuilder()
		.add("implement", tag.getTitle())//tag.getShapeName()
		.build();
	return jso;
    }    
    
    
    private JsonObject makeVariableJsonObject(Variable tag) {
        //DraggableClass tagdc = tag;
	JsonObject jso = Json.createObjectBuilder()
		.add("name", tag.getName())//tag.getShapeName()
                .add("type", tag.getType())
                .add("access", tag.getAccess())
                .add("isStatic", tag.isIsStatic()+"")
                .add("isFinal", tag.isIsFinal()+"")
		.build();
	return jso;
    }
    
    private JsonObject makeRelationshipJsonObject(Relationship tag) {
        //DraggableClass tagdc = tag;
	JsonObject jso = Json.createObjectBuilder()
		.add("start", tag.getFromBox())//tag.getShapeName()
                .add("end", tag.getToBox())
                .add("lineType", tag.getLineType())
		.build();
	return jso;
    }
    
    private JsonObject makeMethodJsonObject(Method tag) {
        //DraggableClass tagdc = tag;
	JsonObject jso = Json.createObjectBuilder()
		.add("name", tag.getName())//tag.getShapeName()
                .add("return", tag.getReturnType())//tag.getFillColorString()
                .add("type1", tag.getType1())
                .add("arg1", tag.getArg1())
                .add("isStatic", tag.isStaticType()+"")
                .add("isAbstract", tag.isAbstractType()+"")
                .add("access", tag.getAccess())
		.build();
	return jso;
    }    
    
    
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {

        //filePath = filePath+".json";
        //System.err.println("loadData " + filePath);
        //CHECK_LOAD = "LOAD";
	// LOAD THE JSON FILE WITH ALL THE DATA
        DataManager dataManager = (DataManager)data;
	dataManager.reset();
        
	JsonObject json = loadJSONFile(filePath);
	
	// LOAD THE TAG TREE
	JsonArray jsonTagTreeArray = json.getJsonArray("canvas");
      //  System.out.println("loadData " + jsonTagTreeArray.size());
        //System.err.println("tagJSO " + jsonTagTreeArray.getJsonObject(0).getString(JSON_SHAPE_NAME));  
        
       for (int i = 0; i < jsonTagTreeArray.size(); i++) {
            //System.out.println("tagJSO " + jsonTagTreeArray.g);
	    JsonObject tagJSO = jsonTagTreeArray.getJsonObject(i);
	    String tagName = tagJSO.getString("type");

	    if(tagName.equals("class")){
                DraggableClass tag = new DraggableClass();
                
                tag.setTitle(tagJSO.getString("title"));
                tag.setPackageName(tagJSO.getString("package"));
                tag.setParentName(tagJSO.getString("parent"));
                tag.setStartX(Double.parseDouble(tagJSO.getString("positionX")));
                tag.setStartY(Double.parseDouble(tagJSO.getString("positionY")));
                tag.settWidth(Double.parseDouble(tagJSO.getString("width")));
                tag.settHeight(Double.parseDouble(tagJSO.getString("height")));
                tag.setPrefWidth(Double.parseDouble(tagJSO.getString("width")));
                tag.setPrefHeight(Double.parseDouble(tagJSO.getString("height")));
                
                tag.getBottom().setPrefHeight(Double.parseDouble(tagJSO.getString("height")));
                //tag.getBottom().setHeight(Double.parseDouble(tagJSO.getString("height")));
                
                if(tagJSO.getString("isAbstract").equalsIgnoreCase("true")) tag.setIsAbstract(true);
                else tag.setIsAbstract(false);
                
                //Load all of its implemented interface
                JsonArray jsonImplementsArray = tagJSO.getJsonArray("implements");
                ArrayList<DraggableInterface> itsImplements = new ArrayList<>();
                if(jsonImplementsArray != null){
                    for (int j = 0; j < jsonImplementsArray.size(); j++) {
                        JsonObject implementJSO = jsonImplementsArray.getJsonObject(j);
                        DraggableInterface intf = new DraggableInterface();
                        intf.setTitle(implementJSO.getString("implement"));
                        itsImplements.add(intf); 
                    }
                }
                tag.setItsImplements(itsImplements);
                
                //Load all of its variables
                JsonArray jsonVariableArray = tagJSO.getJsonArray("variable");
                ArrayList<Variable> itsVariables = new ArrayList<>();
                if(jsonVariableArray != null){
                    for (int k = 0; k < jsonVariableArray.size(); k++) {
                        JsonObject implementJSO = jsonVariableArray.getJsonObject(k);
                        Variable intf = new Variable();
                        intf.setName(implementJSO.getString("name"));
                        intf.setType(implementJSO.getString("type"));
                        intf.setAccess(implementJSO.getString("access"));
                        if(implementJSO.getString("isStatic").equals("true"))
                            intf.setIsStatic(true);
                        else
                            intf.setIsStatic(false);
                        if(implementJSO.getString("isFinal").equals("true"))
                            intf.setIsFinal(true);
                        else
                            intf.setIsFinal(false);
                        itsVariables.add(intf); 
                    }
                }
                tag.setVariables(itsVariables);
                
                
                
                
                //Load all of its method
                JsonArray jsonMethodArray = tagJSO.getJsonArray("method");
                ArrayList<Method> itsMethods = new ArrayList<>();
                if(jsonMethodArray != null){
                    for (int o = 0; o < jsonMethodArray.size(); o++) {
                        JsonObject implementJSO = jsonMethodArray.getJsonObject(o);
                        Method intf = new Method();
                        intf.setName(implementJSO.getString("name"));
                        intf.setReturnType(implementJSO.getString("return"));
                        intf.setType1(implementJSO.getString("type1"));
                        if(implementJSO.getString("isStatic").equals("true"))
                            intf.setStaticType(true);
                        else
                            intf.setStaticType(false);
                        if(implementJSO.getString("isAbstract").equals("true"))
                            intf.setAbstractType(true);
                        else
                            intf.setAbstractType(false);
                        intf.setArg1(implementJSO.getString("arg1"));
                        intf.setAccess(implementJSO.getString("access"));
                        itsMethods.add(intf); 
                    }
                }
                tag.setMethods(itsMethods);
                
                
                //Load all of its relationship
                JsonArray jsonRelationshipArray = tagJSO.getJsonArray("relationship");
                ArrayList<Relationship> itsRelationships = new ArrayList<>();
                if(jsonRelationshipArray != null){
                    for (int z = 0; z < jsonRelationshipArray.size(); z++) {
                        JsonObject implementJSO = jsonRelationshipArray.getJsonObject(z);
                        Relationship intf = new Relationship(implementJSO.getString("start"),implementJSO.getString("end"),implementJSO.getString("lineType"));
                       
                        itsRelationships.add(intf); 
                    }
                }
                tag.setItsRelationships(itsRelationships);
                
                
                
//                tag.setStartX(Double.parseDouble(tagJSO.getString("positionX")));
//                tag.setStartY(Double.parseDouble(tagJSO.getString("positionY")));
//                tag.settWidth(Double.parseDouble(tagJSO.getString("width")));
//                tag.settHeight(Double.parseDouble(tagJSO.getString("height")));
                tag.relocate(Double.parseDouble(tagJSO.getString("positionX")), Double.parseDouble(tagJSO.getString("positionY")));
                
                 
                
                
                
                
                dataManager.addClass(tag);
            }else{
                DraggableInterface tag = new DraggableInterface();
                
                tag.setTitle(tagJSO.getString("title"));
                tag.setPackageName(tagJSO.getString("package"));
                
                tag.setStartX(Double.parseDouble(tagJSO.getString("positionX")));
                tag.setStartY(Double.parseDouble(tagJSO.getString("positionY")));
                tag.settWidth(Double.parseDouble(tagJSO.getString("width")));
                tag.settHeight(Double.parseDouble(tagJSO.getString("height")));
                tag.setPrefWidth(Double.parseDouble(tagJSO.getString("width")));
                tag.setPrefHeight(Double.parseDouble(tagJSO.getString("height")));
                tag.getBottom().setPrefHeight(Double.parseDouble(tagJSO.getString("height")));
//                //Load all of its implemented interface
//                JsonArray jsonImplementsArray = json.getJsonArray("implements");
//                ArrayList<DraggableInterface> itsImplements = new ArrayList<>();
//                for (int j = 0; j < jsonImplementsArray.size(); j++) {
//                    JsonObject implementJSO = jsonImplementsArray.getJsonObject(i);
//                    DraggableInterface intf = new DraggableInterface();
//                    intf.setTitle(implementJSO.getString("implement"));
//                    itsImplements.add(intf); 
//                }
//                tag.setItsImplements(itsImplements);
                
                //Load all of its variables
                JsonArray jsonVariableArray = tagJSO.getJsonArray("variable");
                ArrayList<Variable> itsVariables = new ArrayList<>();
                if(jsonVariableArray != null){
                    for (int k = 0; k < jsonVariableArray.size(); k++) {
                        JsonObject implementJSO = jsonVariableArray.getJsonObject(k);
                        Variable intf = new Variable();
                        intf.setName(implementJSO.getString("name"));
                        intf.setType(implementJSO.getString("type"));
                        intf.setAccess(implementJSO.getString("access"));
                        if(implementJSO.getString("isStatic").equals("true"))
                            intf.setIsStatic(true);
                        else
                            intf.setIsStatic(false);
                        if(implementJSO.getString("isFinal").equals("true"))
                            intf.setIsFinal(true);
                        else
                            intf.setIsFinal(false);
                        itsVariables.add(intf); 
                    }
                }
                tag.setVariables(itsVariables);

                //Load all of its method
                JsonArray jsonMethodArray = tagJSO.getJsonArray("method");
                ArrayList<Method> itsMethods = new ArrayList<>();
                if(jsonMethodArray != null){
                    for (int g = 0; g < jsonMethodArray.size(); g++) {
                        JsonObject implementJSO = jsonMethodArray.getJsonObject(g);
                        Method intf = new Method();
                        intf.setName(implementJSO.getString("name"));
                        intf.setReturnType(implementJSO.getString("return"));
                        intf.setType1(implementJSO.getString("type1"));
                        intf.setArg1(implementJSO.getString("arg1"));
                        intf.setAccess(implementJSO.getString("access"));
                        itsMethods.add(intf); 
                    }
                }
                tag.setMethods(itsMethods);
                
                 //Load all of its relationship
                JsonArray jsonRelationshipArray = tagJSO.getJsonArray("relationship");
                ArrayList<Relationship> itsRelationships = new ArrayList<>();
                if(jsonRelationshipArray != null){
                    for (int z = 0; z < jsonRelationshipArray.size(); z++) {
                        JsonObject implementJSO = jsonRelationshipArray.getJsonObject(z);
                        Relationship intf = new Relationship(implementJSO.getString("start"),implementJSO.getString("end"),implementJSO.getString("lineType"));
                       
                        itsRelationships.add(intf); 
                    }
                }
                tag.setItsRelationships(itsRelationships);
                
                
               
                tag.relocate(Double.parseDouble(tagJSO.getString("positionX")), Double.parseDouble(tagJSO.getString("positionY")));
                dataManager.addInterface(tag);
            }
            
            
            
         
            
        //    tag.setThickValue(Double.parseDouble(tagThick));
        //    tag.setPositionX(Double.parseDouble(tagJSO.getString(JSON_SHAPE_POSITIONX)));
         //   tag.setPositionY(Double.parseDouble(tagJSO.getString(JSON_SHAPE_POSITIONY)));
            
         //   tag.setWidth(Double.parseDouble(tagJSO.getString("width")));
         //   tag.setHeight(Double.parseDouble(tagJSO.getString("height")));
         //   tag.setZindex(tagJSO.getString("zindex"));
	    // NOW GIVE THE LOADED TAG TO THE DATA MANAGER
            
            
        }
       //System.out.println(" finished load a file " );
       	// AND GET THE CSS CONTENT
//	String cssContent = json.getString("bg");
//	dataManager.setCSSText(cssContent);

        
        
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    /*
    public Shape loadShape(JsonObject jsonShape) {
	// FIRST BUILD THE PROPER SHAPE TYPE
	String type = jsonShape.getString(JSON_TYPE);
	Shape shape;
        
	if (type.equals(RECTANGLE)) {
	    //shape = new DraggableRectangle();
	}
	else {
	  //  shape = new DraggableEllipse();
	}
	
	// THEN LOAD ITS FILL AND OUTLINE PROPERTIES
	Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
	Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
	double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
	//shape.setFill(fillColor);
	//shape.setStroke(outlineColor);
	//shape.setStrokeWidth(outlineThickness);
	
	// AND THEN ITS DRAGGABLE PROPERTIES
	double x = getDataAsDouble(jsonShape, JSON_X);
	double y = getDataAsDouble(jsonShape, JSON_Y);
	double width = getDataAsDouble(jsonShape, JSON_WIDTH);
	double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
	//Draggable draggableShape = (Draggable)shape;
	//draggableShape.setLocationAndSize(x, y, width, height);
	
	// ALL DONE, RETURN IT
	return null;//shape
    }
    
    public Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }
    */
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE Web Page Maker APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED WEB PAGES
    }
}
