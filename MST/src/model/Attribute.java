/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
/**
 *
 * @author Abraham Krisnanda
 */
public class Attribute {
    private String attributeName;
    private ArrayList<String> attributeValue;
    public Attribute() {
        attributeValue = new ArrayList<String>();
    }
    
    public Attribute (Attribute attr) {
        attributeName = attr.attributeName;
        attributeValue = new ArrayList<String>();
        for (int i = 0; i < attr.attributeValue.size(); i++){
            attributeValue.add(attr.attributeValue.get(i));
        }
    }
    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * @param attributeName the attributeName to set
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * @return the attributeValue
     */
    public ArrayList<String> getAttributeValue() {
        return attributeValue;
    }

    /**
     * @param attributeValue the attributeValue to set
     */
    public void setAttributeValue(ArrayList<String> attributeValue) {
        this.attributeValue = attributeValue;
    }
    
}
