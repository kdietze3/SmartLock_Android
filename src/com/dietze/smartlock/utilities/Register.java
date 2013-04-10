package com.dietze.smartlock.utilities;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.dietze.smartlock.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Register extends Activity{
	
	String wifiAddress = "";
	String bluetoothAddress = "";
	String nfcAddress = "";
	String serial = "";
	
	RegisterTask mRegisterTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		
		WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		wifiAddress = wifiInf.getMacAddress();
		
		BluetoothAdapter bluetoothAdapter = (BluetoothAdapter) BluetoothAdapter.getDefaultAdapter();
		bluetoothAddress = bluetoothAdapter.getAddress();
		
		//NfcManager nfcManager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
		//NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter();
		
		serial = getSerial();
		
		findViewById(R.id.register_action_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Log.d("Register Activity","Attempting to Register");
						Log.d("Register Activity", "WiFi Mac Address: "+ wifiAddress);
						Log.d("Register Activity", "Bluetooth Address: "+ bluetoothAddress);
						Log.d("Register Activity", "Serial Number: " + serial);
						
						mRegisterTask = new RegisterTask();
						mRegisterTask.execute((Void) null);
						
						
					}
					
				});
		
		
	}
	private String getSerial(){
		String serial = null;
		try {
		    Class<?> c = Class.forName("android.os.SystemProperties");
		    Method get = c.getMethod("get", String.class);
		    serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception ignored) {
		}
		return serial;
		
	}
	
	public void postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
	    TextView emailView = (TextView) findViewById(R.id.email);
	    String email = new String(emailView.getText().toString());
	    
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("email", email));
	        nameValuePairs.add(new BasicNameValuePair("wifi", wifiAddress));
	        nameValuePairs.add(new BasicNameValuePair("bluetooth", bluetoothAddress));
	        nameValuePairs.add(new BasicNameValuePair("serial", serial));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}
	
	public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
				postData();
			} catch (InterruptedException e) {
				return false;
			}

			
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
			if (success) {
				finish();
			} else {
				//Do something
			}
		}

		@Override
		protected void onCancelled() {
			
		}
	}
}
