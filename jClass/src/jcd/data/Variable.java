/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.control.Label;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 *
 * @author Dan Choe
 */
public class Variable extends Label{
    
    private String type, name, access, staticAsString, finalAsString;
    private boolean isFinal, isStatic;
    
    StringProperty columnName;
    StringProperty columnType;
    StringProperty columnAcess;
    StringProperty columnFinal;
    StringProperty columnStatic;
    
    
    
    public Variable() {
       
        access = "";
        type = "";
        name = "";
        isStatic = false;
        staticAsString = "";
        finalAsString = "";
        isFinal = false;
        
        columnName = new SimpleStringProperty("");
        columnType = new SimpleStringProperty("");
        columnAcess = new SimpleStringProperty("");
        columnFinal = new SimpleStringProperty("false");
        columnStatic = new SimpleStringProperty("false");

        reloaditsValues();
    }
    
    public void reloaditsValues(){
        if(isFinal)
            name = name.toUpperCase();
        else
            name = name.toLowerCase();
        
        
        setColumnName(name);
        setColumnType(type);
        setColumnAcess(access);
        setColumnFinal(isFinal+"");
        setColumnStatic(isStatic+"");
        
        
        setText(access+staticAsString+name+": "+type);
    }

    public String getAccess() {
        if(access.equals("+"))
            return "public";
        else if(access.equals("-"))
            return "private";
        else
            return "protected";
    }
    
    public String getAccessAsSymbol() {
        return access;
    }

    public String getStaticAsSymbol() {
        return staticAsString;
    }
    
    public String getStaticAsStrings() {
        if(isStatic == true) return "static";
        else return "";
    }
    
    public String getFinalAsStrings() {
        if(isFinal == true) return "final";
        else return "";
    }
   

    public void setStaticAsSymbol(boolean staticcheck) {
        if(staticcheck == true) this.staticAsString = "$";
        else this.staticAsString = "";
    }

    
    public String getFinalAsSymbol() {
        return finalAsString;
    }

    public void setFinalAsSymbol(boolean finalAsString) {
        if(finalAsString == true) name = name.toUpperCase();
        else name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
        setId(name);
        columnName.set(name);
    }

    public void setType(String type) {
        this.type = type;
        columnType.set(type);
    }

    public void setAccess(String access) {
        if(access.equalsIgnoreCase("public"))
           this.access = "+";
        else if(access.equalsIgnoreCase("private"))
           this.access = "-";
        else
            this.access = "#";
        
        columnAcess.set(access);
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
        setStaticAsSymbol(isStatic);
        columnStatic.set(isStatic+"");
        
    }


    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
        setFinalAsSymbol(isFinal);
        columnFinal.set(isFinal+"");
    }
    
    public String nameProperty() {
        return columnName.get();
    }

    public String getColumnName() {
        return columnName.get();
    }

    public void setColumnName(String columnName) {
        this.columnName.set(columnName);
    }

    public String getColumnType() {
        return columnType.get();
    }

    public void setColumnType(String columnType) {
        this.columnType.set(columnType);
    }

    public String getColumnAcess() {
        
        if(columnAcess.get().equals("+"))
            return "public";
        else if(columnAcess.get().equals("-"))
            return "private";
        else
            return "protected";
    }

    public void setColumnAcess(String columnAcess) {
        this.columnAcess.set(columnAcess);
    }

    public String getColumnFinal() {
        return columnFinal.get();
    }

    public void setColumnFinal(String columnFinal) {
        this.columnFinal.set(columnFinal);
    }

    public String getColumnStatic() {
        return columnStatic.get();
    }

    public void setColumnStatic(String columnStatic) {
        this.columnStatic.set(columnStatic);
    }
    
    public void reset() {
        setColumnName("");
    }
    
    
    

}
