/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mst;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import model.*;
/**
 *
 * @author Abraham Krisnanda
 */
public class ArffParser {
    private BufferedReader br = null;
    public ArrayList<Attribute> listOfAttribute;
    public ArrayList<ArrayList<String>> examples;
    public Examples Ex;
    int i=0;
    public ArffParser(String fileLocation ) {
        Ex = new Examples();
        try {
            String currentLine;
            String identifier;
            br = new BufferedReader(new FileReader(fileLocation));
            listOfAttribute = new ArrayList<>();
            examples = new ArrayList<ArrayList<String>>();
            while ((currentLine = br.readLine()) != null) {
                i++;
                if (!currentLine.equals("")) {
                    identifier = currentLine.split(" ")[0];
                    if (identifier.startsWith("@")) {
                        switch (identifier) {
                            case "@relation":
                                break;
                            case "@attribute":
                            {
                                Attribute tempAttribute = new Attribute();
                                tempAttribute.setAttributeName(currentLine.split(" ")[1]);
                                String cl = currentLine.replaceAll("\\s",""); // remove whitespace
                                String[] attrValue = cl.split("\\{")[1].split(",");
                                ArrayList<String> tempValue = new ArrayList<>(); // temp listOfAttribute value
                                for (int i=0; i<attrValue.length;i++) {
                                    if (i==(attrValue.length-1)) {
                                        // last element, remove the "}"
                                        tempValue.add(attrValue[i].split("}")[0]);
                                    }
                                    else {
                                        tempValue.add(attrValue[i]);
                                    }
                                }
                                tempAttribute.setAttributeValue(tempValue);
                                listOfAttribute.add(tempAttribute);
                            }
                                break;
                            case "@data":
                                break;
                        }
                    }
                    else {
                        // examples
                        String cl = currentLine.replaceAll("\\s", ""); // remove whitespaces
                        String[] dataValue = cl.split(",");
                        ArrayList<String> tempData = new ArrayList<>();
                        for (int i=0; i < dataValue.length; i++) {
                            tempData.add(dataValue[i]);
                        }
                        examples.add(tempData);
                        Ex.setData(examples);
                        Ex.setAttributes(listOfAttribute);
                    }
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                try {
                        if (br != null)br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

    }
}
