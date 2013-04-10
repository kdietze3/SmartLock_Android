
package com.dietze.smartlock.utilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dietze.smartlock.R;
import com.dietze.smartlock.controllers.SmartLockController;
import com.dietze.smartlock.core.CoreActivity;


public class MenuFragment extends SherlockFragment implements ITitleable {
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }
 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.menu, container,false);
        
        Button wifi = (Button)v.findViewById(R.id.wifi_btn);
        wifi.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	Transitions.transitionToWifi((CoreActivity )getActivity());
                
            }
        });
        
        Button bluetooth = (Button)v.findViewById(R.id.bluetooth_btn);
        bluetooth.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	Transitions.transitionToBluetooth((CoreActivity )getActivity());
                
            }
        });
        
        Button camera = (Button)v.findViewById(R.id.camera_btn);
        camera.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Transitions.transitionToCamera((CoreActivity )getActivity());
                
            }
        });
      
        
        
        
        
        return v;
    }
    
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(getTitle());
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
    }
    
    @Override
    public int getTitle() {
        return R.string.app_name;
    }
    
    

   
}
