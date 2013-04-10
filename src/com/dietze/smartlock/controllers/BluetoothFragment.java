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

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dietze.smartlock.R;
import com.dietze.smartlock.protocols.BlueSmirfSPP;
import com.dietze.smartlock.utilities.ITitleable;

public class BluetoothFragment extends SherlockFragment implements ITitleable, Runnable, OnClickListener{
 
	String ipaddress = "10.10.1.100";
	Context context;
	
	//App State
	private BlueSmirfSPP      mSPP;
	private boolean           mIsThreadRunning;
	private String            mBluetoothAddress;
	private ArrayList<String> mArrayListBluetoothAddress;

	// UI
	private TextView     mTextViewStatus;
	private Spinner      mSpinnerDevices;
	private ArrayAdapter mArrayAdapterDevices;
	private Handler      mHandler;

	// Arduino state
	private int mStateLED;
	private int mStatePOT;
	private int LEDStatus;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.bluetooth, container, false);
        
        
        
        
       return v;

    }
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
    	super.onViewCreated(view, savedInstanceState);
		mIsThreadRunning           = false;
		mBluetoothAddress          = null;
		mSPP                       = new BlueSmirfSPP();
		mStateLED                  = 0;
		mStatePOT                  = 0;
		mArrayListBluetoothAddress = new ArrayList<String>();
        
		mTextViewStatus         = (TextView) view.findViewById(R.id.ID_STATUS);
		ArrayList<String> items = new ArrayList<String>();
		mSpinnerDevices         = (Spinner) view.findViewById(R.id.ID_PAIRED_DEVICES);
		mArrayAdapterDevices    = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
		mHandler                = new Handler();
		
		mSpinnerDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Object item = parent.getItemAtPosition(pos);
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		mArrayAdapterDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDevices.setAdapter(mArrayAdapterDevices);
		
		Button bluetoothSettings = (Button) view.findViewById(R.id.bluetooth_settings_btn);
        bluetoothSettings.setOnClickListener(this);
	}
	
	
	/*
	 * OnClickListener for all of the button functionality
	 */
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bluetooth_settings_btn:
        		Intent i = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
				startActivity(i);
            break;
        case R.id.bluetooth_connect_btn:
    			if(mIsThreadRunning == false)
    			{
    				mIsThreadRunning = true;
    				Thread t = new Thread(this);
    				t.start();
    				UpdateUI();
    			}
        	break;
        case R.id.bluetooth_disconnect_btn:
        	mSPP.disconnect();
        	break;
        case R.id.bluetooth_open_btn:
        	if(mSPP.isConnected())
			{
				mStateLED = 2;
			}
        	break;
        case R.id.bluetooth_lock_btn:
        	if(mSPP.isConnected())
			{
				mStateLED = 1;
				
			}
        	break;
        }
    }
	
	 @Override
	    public void onResume(){
	        super.onResume();
	        getActivity().setTitle(getTitle());
	        
	    	

	    		// update the paired device(s)
	    		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	    		Set<BluetoothDevice> devices = adapter.getBondedDevices();
	    		mArrayAdapterDevices.clear();
	    		mArrayListBluetoothAddress.clear();
	    		Log.d("SmartLock Controller", "Adapters Cleared");
	    		if(devices.size() > 0)
	    		{
	    			for(BluetoothDevice device : devices)
	    			{
	    				mArrayAdapterDevices.add(device.getName());
	    				mArrayListBluetoothAddress.add(device.getAddress());
	    			}
	    			
	    			Log.d("SmartLock Controller", "Adapters Filled");
	    			// request that the user selects a device
	    			if(mBluetoothAddress == null)
	    			{
	    				//mSpinnerDevices.performClick();
	    			}
	    		}
	    		else
	    		{
	    			mBluetoothAddress = null;
	    		}

	    		UpdateUI();
	    	}
	        
	        
	    
	    
	    @Override
	    public int getTitle(){
	    	return R.string.bluetooth;
	    }
	
	    @Override
		public void onPause()
		{
			mSPP.disconnect();
			super.onPause();
		}
	    
		/*
		 * Spinner callback
		 */

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
		{
			mBluetoothAddress = mArrayListBluetoothAddress.get(pos);
		}

		public void onNothingSelected(AdapterView<?> parent)
		{
			mBluetoothAddress = null;
		}

		


		/*
		 * main loop
		 */

		public void run()
		{
			Looper.prepare();
			mSPP.connect(mBluetoothAddress);
			int tempStatus = 0;
			while(mSPP.isConnected())
			{
				
				mSPP.flush();
				if(mStateLED != 0){
				mSPP.writeByte(mStateLED);
				}
				mSPP.flush();
				
				mStateLED = 0;
				
				tempStatus = mSPP.readByte();
				
				if(tempStatus != 0){
					LEDStatus = tempStatus;
					Log.d("Bluetooth",Integer.toString(LEDStatus));
				}
				
				
				if(mSPP.isError())
				{
					mSPP.disconnect();
				}
				
				mHandler.sendEmptyMessage(0);

				// wait briefly before sending the next packet
				try { Thread.sleep(700); }
				catch(InterruptedException e) { Log.e("Bluetooth", e.getMessage());}
				//mIsThreadRunning = false;
			}
			
			mStateLED        = 0;
			mIsThreadRunning = false;
			mHandler.sendEmptyMessage(0);
			
		}

		/*
		 * Update UI Functionality
		 */
	    
	    public boolean handleMessage (Message msg)
		{
			// received update request from Bluetooth IO thread
			UpdateUI();
			return true;
		}

		private void UpdateUI()
		{
			if(mSPP.isConnected())
			{
				String LEDstate = "Unknown";
				
				if(LEDStatus == 1){
					LEDstate = "on";
					mTextViewStatus.setText("connected to " + mSPP.getBluetoothAddress() + "\n" +
	                        "LED is " + LEDstate + "\n");
				}
				else if(LEDStatus == 2){
					LEDstate = "off";
					mTextViewStatus.setText("connected to " + mSPP.getBluetoothAddress() + "\n" +
	                        "LED is " + LEDstate + "\n");
				}
				else{ 
					LEDstate = "Unknown";
					mTextViewStatus.setText("connected to " + mSPP.getBluetoothAddress() + "\n" +
	                    "LED is " + LEDstate + "\n");
				}
				
				
				                       
			}
			else if(mIsThreadRunning)
			{
				mTextViewStatus.setText("connecting to " + mBluetoothAddress);
			}
			else
			{
				mTextViewStatus.setText("disconnected");
			}
		}
}