package com.example.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author Bartek Karmilowicz
 * 
 * Class Responsible for pulling data from database in JSON format
 * 
 */


public class JSONPuller {
	
	private InputStream inputStream = null;

	
	//default constructor
	public JSONPuller(){
		
	}
	
	public JSONObject getJSONfromURL(String url){
		
		JSONObject jobject = null;
		String stringResult = null;
		
		// make HTTP connection
		stringResult = makeHTTPrequest(url);
		
		if(stringResult != null){		
			// parse JSON object
			try {
				jobject = new JSONObject(stringResult);
			} catch (JSONException e) {
				Log.e("JSON Parser", "ERROR while parsing json");
			}	
		}
		
		return jobject;
	}
	
	
	public String makeHTTPrequest(String url){
		
		String result = null;
		
		//http client, GET REQUEST
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		try {
			
			HttpResponse response = client.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			
			//open input stream
			inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8); 
			
			//not synchronized ver. of StringBuffer 
			StringBuilder builder = new StringBuilder();
			String line = null;
			
			while((line = reader.readLine()) != null){
				builder.append(line + "\n");
			}
			
			if(inputStream != null){
				inputStream.close();
			}

			result = builder.toString();
			
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		    Log.e("JSON Parser", "Connection ERROR");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	 Log.e("JSON Parser", "IO ERROR");
	    } catch (Exception e){
	    	e.printStackTrace();
	    	Log.e("JSON Parser", e.toString());
	    }
		
		return result;
	}
	
	
	
}
