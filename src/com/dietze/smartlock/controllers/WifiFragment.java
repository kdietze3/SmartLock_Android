/*
 * Copyright 2013 The Last Crusade ContactLastCrusade@gmail.com
 * 
 * This file is part of SoundStream.
 * 
 * SoundStream is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SoundStream is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SoundStream.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dietze.smartlock.controllers;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.dietze.smartlock.R;
import com.dietze.smartlock.utilities.ITitleable;
import com.dietze.smartlock.utilities.RequestTask;

public class WifiFragment extends SherlockFragment implements ITitleable{
    
	private Button addMembersButton, disconnect, disband;
	private Context context;
	//private View mView;
	private String ipaddress = "10.10.1.100";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	
    	View v = inflater.inflate(R.layout.wifi, container, false);
        
        context = v.getContext();
        
		
		
		Log.d("SmartLock Controller", "OnCreate finished");
        
       return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	super.onViewCreated(view, savedInstanceState);
    	
    	View v = getView();
    	if(v.equals(null)){
    		Log.d("Wifi","View Returned Null");
    	}
    	else{
    		Log.d("Wifi","Getting View");
    	}
    	
    	v.findViewById(R.id.button_open).setOnClickListener(Listener);
		v.findViewById(R.id.button_lock).setOnClickListener(Listener);
		//v.findViewById(R.id.ipaddress_edit).setOnClickListener(Listener);
		
		//tv = (TextView)findViewById(R.id.charCounts);
	    EditText textMessage = (EditText)v.findViewById(R.id.ipaddress_edit);
		textMessage.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            ipaddress = s.toString();
	            Log.d("Wifi","New IP: "+ipaddress);
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		
		
		
    }

    

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getTitle());
    }
    
    @Override
    public int getTitle(){
    	return R.string.wifi;
    }
    
    
    
private OnClickListener Listener = new OnClickListener() { 
		
	    public void onClick(View v) {
	    	
	    	if (v instanceof Button){
	           String buttonText = ((Button) v).getText().toString();
	           Log.d("Wifi", "Button Text:" + buttonText);
	           
	           
	           Log.d("Wifi","Got IP Address: "+ipaddress);
	           
	           if (buttonText.equals("Open")){
	        	   //Do something
	        	   Log.d("SmartLock App", "Sending Open Command");
	        	   new RequestTask(context).execute("http://"+ipaddress+"/?OPEN");
	        	   
	           }
	           
	           else if (buttonText.equals("Lock")){
	        	   //Do something
	        	   Log.d("SmartLock App", "Sending Lock Command");
	        	   new RequestTask(context).execute("http://"+ipaddress+"/?LOCK"); 
	        	   
	           }
	           
	           else {
	        	   //Do nothing
	        	   Log.d("SmartLock App", "Click Matches No Known Actions");
	           }
	    	}
	    	else if (v instanceof EditText){
	    		//ipaddress = ((EditText) v.findViewById(R.id.editText1)).getText().toString();
	    		Log.d("Wifi","Saving New Ipaddress");
	    		ipaddress = ((EditText) v).getText().toString();
	    		Log.d("Wifi", "New Ipaddress: "+ipaddress);

	    	}
	    	else {
		    	Log.d("SmartLock App", "Not a Button");
		    }
	    	
	    }
	    
	}; 
    
    
}
