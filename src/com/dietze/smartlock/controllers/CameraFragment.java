
package com.dietze.smartlock.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dietze.smartlock.R;
import com.dietze.smartlock.utilities.ITitleable;
import com.dietze.smartlock.utilities.SnapshotDownloadTask;

public class CameraFragment extends SherlockFragment implements ITitleable{
    
	private WebView webview;
	private Activity activity;
	private Handler mHandler;
	private Runnable toastUpdateCompletedRunnable;
	private Runnable toastUpdateStartingRunnable;
	
	private final Integer updateTimeInSeconds = 60;
	
	private String cameraAddress = "http://kdietze3.dyndns.org:1337/snapshot.cgi?user=app&pwd=oxford2012";
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.camera, container, false);
        return v;
    }
    
    
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    	activity = getActivity();
    	webview = (WebView)view.findViewById(R.id.camera_webview);
    	webview.setWebViewClient(new MyWebViewClient());
    	mHandler = new Handler();
    	
    	toastUpdateCompletedRunnable = new Runnable() {public void run() {Toast.makeText(activity,"Auto Update Completed",Toast.LENGTH_LONG).show();}};
    	toastUpdateStartingRunnable = new Runnable() {public void run() {Toast.makeText(activity,"Auto Update Starting for "+ updateTimeInSeconds.toString()+" Seconds",Toast.LENGTH_LONG).show();}};
    	
    	
    	
    	view.findViewById(R.id.snapshot_btn).setOnClickListener(Listener);
    	view.findViewById(R.id.autoupdate_btn).setOnClickListener(Listener);
    	
    	Log.d("Camera","Attempting to Load Camera");
    	webview.loadUrl(cameraAddress);
    }
    
    
    
    private OnClickListener Listener = new OnClickListener() { 
		
	    public void onClick(View v) {
	    	
	    	if (v instanceof Button){
	    		switch (v.getId()){
	    		case R.id.snapshot_btn:
	    			webview.loadUrl(cameraAddress);
	    			try {
						new SnapshotDownloadTask().execute(new URL(cameraAddress));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
	    			break;
	    			
	    		case R.id.autoupdate_btn:
	    			
	    			
	    			if(!updateThread.isAlive()){
	    				Log.d("Camera Fragment", "Starting Auto Update Thread");
	    				updateThread.start();
	    			}
	    			else {
	    				Log.d("Camera Fragment", "Auto Update Thread Already Running");
	    			}
	    			break;
	    		}
	    	}
	    	}
    };
    
    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        
       
    }    

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getTitle());
    }
    
    @Override
    public int getTitle(){
    	return R.string.camera;
    }

    Thread updateThread = new Thread(new Runnable(){
		
		
		@Override
		public void run() {
			
			mHandler.post(toastUpdateStartingRunnable);
			
			int count = 0;
			while(count < updateTimeInSeconds){
				webview.loadUrl("http://kdietze3.dyndns.org:1337/snapshot.cgi?user=app&pwd=oxford2012");
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 mHandler.post(toastUpdateCompletedRunnable);
			 
		}
	    });
    
    
    
    
}
