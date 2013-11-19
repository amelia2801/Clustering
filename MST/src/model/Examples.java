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
public class Examples {
//    training examples
//    contoh :
//    sunny, hot, high, weak, no
//    sunny, hot, high, strong, no
    private ArrayList<Attribute> attributes;
    private ArrayList<ArrayList<String>> data;
    public Examples() {
        data = new ArrayList<ArrayList<String>>();
        // banyak data --> data.size()
    }
    
    public Examples (Examples ex) {
        //attributes = new ArrayList<Attribute>(ex.attributes);
        attributes = new ArrayList<Attribute>();
        for (int i = 0; i < ex.attributes.size(); i++) { 
            attributes.add(new Attribute(ex.attributes.get(i)));
        } 
        
        data = new ArrayList<ArrayList<String>>(); 
        for (int i = 0; i < ex.data.size(); i++) {
            data.add(new ArrayList<String>());
            for (int j = 0; j < ex.data.get(i).size(); j++) {
                data.get(i).add(ex.data.get(i).get(j)); 
            } 
        }
    }
    /**
     * @return the data
     */
    public ArrayList<ArrayList<String>> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ArrayList<ArrayList<String>> data) {
        this.data = data;
    }
    
    /**
     * @return the attributes
     */
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }
    
    public boolean isExamplesPositive() {
        boolean classificationValue = true;
        int cvIndex = getData().get(0).size()-1;
        // index of classificationValue
        for (int i=0; i< getData().size(); i++) {
            if (getData().get(i).get(cvIndex).equals("no")) {
                classificationValue = false;
            }
        }
        return classificationValue;
    }
    
    public boolean isExampleNegative() {
        boolean classificationValue = true;
        int cvIndex = getData().get(0).size()-1;
        // index of classificationValue
        for (int i=0; i< getData().size(); i++) {
            if (getData().get(i).get(cvIndex).equals("yes")) {
                classificationValue = false;
            }
        }
        return classificationValue;
    }
    
    public boolean isExampleEmpty() {
        return (getData().isEmpty());
    }
    
    public int indexOfAttribute (String attrName) {
        int indexParent=-1;
        for (int i = 0; i < this.getAttributes().size(); i++){
            if (this.getAttributes().get(i).getAttributeName().equals(attrName)){
                indexParent = i;
                break;
            }
        }
        return indexParent;
    }

    public String toString() {
        String result = "";
        for (int i=0; i < this.getData().size(); i++) {
            result += "\n" + this.getData().get(i).toString();
        }
        return result;
    }
}
