
package edu.msu.cbowen.cse335intellijplugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Values we get from APIResponse.
 * Effectively like a PHP value which can be an array
 * or an object with keys/values.
 */
public class APIValue {
    
    public APIValue(Object obj) {
        if(obj instanceof JSONArray) {
            this.array = (JSONArray)obj;
        } else {
            this.obj = (JSONObject)obj;
        }
    }
        
    /**
     * Does this object have this property?
     * @param property Property name
     * @return true if property exists
     */
    public boolean hasProperty(String property) {
        return obj != null && obj.get(property) != null;
    }
    
    public APIValue get(String property) {
        if(obj == null) {
            return null;
        }
        
        Object getObj = obj.get(property);
        if(getObj == null) {
            return null;
        }
        
        return new APIValue(getObj);
    }
    
        
    public String getAsString(String property) {
        if(obj == null) {
            return null;
        }
        
        Object getObj = obj.get(property);
        if(getObj == null) {
            return null;
        }
        
        return (String)getObj;
    }
    
            
    public Long getAsLong(String property) {
        if(obj == null) {
            return null;
        }
        
        Object getObj = obj.get(property);
        if(getObj == null) {
            return null;
        }
        
        return (Long)getObj;
    }
    
    public APIValue get(int n) {
        Object getObj = array.get(n);
        if(getObj == null) {
            return null;
        }
        
        return new APIValue(getObj);
    }
    
    public int size() {
        if(array == null) {
            return 0;
        }
        
        return array.size();
    }
    
    private JSONArray array = null;
    private JSONObject obj = null;
} 