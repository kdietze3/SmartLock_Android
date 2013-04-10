
package com.dietze.smartlock.utilities;

import android.support.v4.app.Fragment;

import com.dietze.smartlock.R;
import com.dietze.smartlock.controllers.BluetoothFragment;
import com.dietze.smartlock.controllers.CameraFragment;
import com.dietze.smartlock.controllers.WifiFragment;
import com.dietze.smartlock.core.CoreActivity;

/**
 * Singleton class to manage transitions of the active content
 * displayed on the app. 
 */
public class Transitions {
    
    private final static int BLUETOOTH = R.string.bluetooth;
    private final static int WIFI = R.string.wifi;
    private final static int CAMERA  = R.string.camera;
    
    
    private final static BluetoothFragment bluetoothFragment = new BluetoothFragment();
    private final static WifiFragment wifiFragment = new WifiFragment();
    private final static CameraFragment cameraFragment = new CameraFragment();
    

    //Home is where you get sent after connecting to the network - for now
    //this is the playlist
    private final static int HOME = WIFI;
    
    public static void transitionToHome(CoreActivity  activity){
        switchFragment(HOME, activity);
    }
    
    public static void transitionToWifi(CoreActivity  activity){
        switchFragment(WIFI,activity);
    }
    
    public static void transitionToBluetooth(CoreActivity  activity){
        switchFragment(BLUETOOTH, activity);
    }
    
    public static void transitionToCamera(CoreActivity  activity){
        switchFragment(CAMERA, activity);
    }
    
    
    
    private static void switchFragment(int fragmentName, CoreActivity activity){
        Fragment fragment = getFragment(fragmentName);
        
        activity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(null).commit();
        activity.showContent();
        String title = activity.getResources().getString(((ITitleable)fragment).getTitle());
        activity.setTitle(title);
    }
    
    private static Fragment getFragment(int fragmentInx) {
        Fragment newFragment = null;
        
        switch (fragmentInx) {
        case WIFI:
            newFragment = wifiFragment;
            break;
        case BLUETOOTH:
            newFragment = bluetoothFragment;
            break;
        case CAMERA:
            newFragment = cameraFragment;
            break;
        }
        return newFragment;
    }
}
