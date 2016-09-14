package joy.user.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;



@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Content implements Serializable {

    private List<String> registration_ids;
    private Map<String,String> data;

    public void addRegId(String regId){
    	registration_ids = null;
        if(registration_ids == null)
            registration_ids = new LinkedList<String>();
        registration_ids.add(regId);
    }

    public void createData(String title, String message){
    	data = null;
        if(data == null)
            data = new HashMap<String,String>();

        data.put("title", title);
        data.put("message", message);
    }
    
    /*
    public String getRegistrationIds()
    {
    	return this.registrationIds;
    }
    
    public void setRegistrationIds(String id)
    {
    	this.registrationIds = id;
    }
    
    
    public Map<String,String> getData()
    {
    	return this.data;
    }
    
    public void setData(Map<String,String> message)
    {
    	this.data = message;
    }*/
} 