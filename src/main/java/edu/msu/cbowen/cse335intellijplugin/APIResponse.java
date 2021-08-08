/*
 * @file
 * Represents the response from the API
 */
package edu.msu.cbowen.cse335intellijplugin;

import org.json.simple.JSONValue;

/**
 * Represents the response from the API
 */
public class APIResponse extends APIValue {
    
    public APIResponse(String data) {
        super(JSONValue.parse(data));
    }

    
    public boolean hasError() {
        APIValue errors = get("errors");
        if(errors == null) {
            return false;
        }
        
        if(errors.size() > 0) {
            return true;
        }
        
        return false;
    }
    
    public String getErrorTitle() {
        APIValue errors = get("errors");
        if(errors == null) {
            return null;
        }
        
        APIValue errorObj = errors.get(0);
        return errorObj.getAsString("title");
    }
    
    public APIValue getData(String dataType) {
        APIValue data = get("data");
        for(int i=0;  i<data.size(); i++) {
            APIValue dataItem = data.get(i);
            String type = dataItem.getAsString("type");
            if(type.equals(dataType)) {
                return dataItem;
            }
        }
        
        return null;
    }
    
}
