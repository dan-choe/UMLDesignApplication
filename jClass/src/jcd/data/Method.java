/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 *
 * @author Dan Choe
 */
public class Method extends Label {
    
    private String type1, type2, type3, name, access, staticAsString, abstractAsString,
            returnType, arg1, arg2, arg3;
    private boolean isStatic, isAbstract, isFinal;
    
    StringProperty columnName;
    StringProperty columnReturnType;
    StringProperty columnAcess;
    StringProperty columnFinal;
    StringProperty columnStatic;
    StringProperty columnAbstract;
    
    StringProperty columnType1,columnType2,columnType3;
    StringProperty columnArg1,columnArg2,columnArg3;
    
    public Method() {
        access = "";
        name = "";
        returnType = "";
        isStatic = false;
        isAbstract = false;
        isFinal = false;
        staticAsString = "";
        abstractAsString = "";
        arg1 = "";
        arg2 = "";
        arg3 = "";
        type1 ="";
        type2 ="";
        type3 ="";
        
        columnName = new SimpleStringProperty("");
        columnReturnType = new SimpleStringProperty("");
        columnAcess = new SimpleStringProperty("");
        columnFinal = new SimpleStringProperty("false");
        columnStatic = new SimpleStringProperty("false");
        columnAbstract = new SimpleStringProperty("false");
        
        columnType1 = new SimpleStringProperty("");
        columnArg1 = new SimpleStringProperty("");
        columnType2 = new SimpleStringProperty("");
        columnArg2 = new SimpleStringProperty("");
        columnType3 = new SimpleStringProperty("");
        columnArg3 = new SimpleStringProperty("");
        
        
       reloaditsValues();
    }
    
    public void reloaditsValues(){
        if(isFinal)
            name = name.toUpperCase();
        else
            name = name.toLowerCase();
        
        
        setColumnName(name);
        setColumnReturnType(returnType);
        setColumnAcess(access);
        setColumnFinal(isFinal+"");
        setColumnStatic(isStatic+"");
        setColumnAbstract(isAbstract+"");
        
        
        setColumnType1(type1);
        setColumnArg1(arg1);
        setColumnType2(type2);
        setColumnArg2(arg2);
        setColumnType3(type3);
        setColumnArg3(arg3);
        
        setText(access+staticAsString+name+"("+arg1+" "+type1+" "+arg2+" "+type2+" "+arg3+" "+type3+")"+": "+returnType);
    }
    
    public String getStaticAsString() {
        return staticAsString;
    }

    public void setStaticAsString(boolean staticAsString) {
        if(staticAsString == true) this.staticAsString = "$";
        else this.staticAsString = "";
        reloaditsValues();
    }
    
    public String getAbstractAsString() {
        return abstractAsString;
    }

    public void setAbstractAsString(boolean abstractAsString) {
        if(abstractAsString == true){ 
            this.abstractAsString = "{abstract}";
            setText(this.abstractAsString+"\n"+access+staticAsString+name+"("+arg1+" "+type1+" "+arg2+" "+type2+" "+arg3+" "+type3+")"+": "+returnType);
        }else {
            this.abstractAsString = ""; 
            setText(access+staticAsString+name+"("+arg1+" "+type1+" "+arg2+" "+type2+" "+arg3+" "+type3+")"+": "+returnType);
        }
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
        setColumnType1(type1);
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
        setColumnType2(type2);
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
        setColumnType1(type3);
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
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setId(name);
        setColumnName(name);
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

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
        setColumnReturnType(returnType);
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
        setColumnArg1(arg1);
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
        setColumnArg2(arg2);
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
        setColumnArg3(arg3);
    }

    public boolean isStaticType() {
        return isStatic;
    }

    public void setStaticType(boolean staticType) {
        this.isStatic = staticType;
        setStaticAsString(staticType);
    }

    public boolean isAbstractType() {
        return isAbstract;
    }

    public void setAbstractType(boolean abstractType) {
        this.isAbstract = abstractType;
        setAbstractAsString(abstractType);
    }
    
    public boolean isFinalType() {
        return isFinal;
    }

    public void setFinalType(boolean finalType) {
        this.isFinal = finalType;
        columnFinal.set(isFinal+"");
        //setAbstractAsString(abstractType);
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getColumnName() {
        return columnName.get();
    }

    public void setColumnName(String columnName) {
        this.columnName.set(columnName);
    }

    public String getColumnReturnType() {
        return columnReturnType.get();
    }

    public void setColumnReturnType(String columnReturnType) {
        this.columnReturnType.set(columnReturnType);
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

    public String getColumnAbstract() {
        return columnAbstract.get();
    }

    public void setColumnAbstract(String columnAbstract) {
        this.columnAbstract.set(columnAbstract);
    }

    public String getColumnType1() {
        return columnType1.get();
    }

    public void setColumnType1(String columnType1) {
        this.columnType1.set(columnType1);
    }

    public String getColumnType2() {
        return columnType2.get();
    }

    public void setColumnType2(String columnType2) {
        this.columnType2.set(columnType2);
    }

    public String getColumnType3() {
        return columnType3.get();
    }

    public void setColumnType3(String columnType3) {
        this.columnType3.set(columnType3);
    }

    public String getColumnArg1() {
        return columnArg1.get();
    }

    public void setColumnArg1(String columnArg1) {
        this.columnArg1.set(columnArg1);
    }

    public String getColumnArg2() {
        return columnArg2.get();
    }

    public void setColumnArg2(String columnArg2) {
        this.columnArg2.set(columnArg2);
    }

    public String getColumnArg3() {
        return columnArg3.get();
    }

    public void setColumnArg3(String columnArg3) {
        this.columnArg3.set(columnArg3);
    }
    
    
 

}
