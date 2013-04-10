

package com.dietze.smartlock.core;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dietze.smartlock.R;
import com.dietze.smartlock.utilities.ITitleable;
import com.dietze.smartlock.utilities.MenuFragment;
import com.dietze.smartlock.utilities.Transitions;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;


public class CoreActivity extends SlidingFragmentActivity{
    private final String TAG = CoreActivity.class.getName();

    private Fragment menu;
        
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.content_frame);
        
        
        setBehindContentView(R.layout.menu_frame);
        
        
        menu = new MenuFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.menu_frame, menu)
            .commit();
        
        if(savedInstanceState == null) {
            Transitions.transitionToHome(this);
            setSlidingActionBarEnabled(true);
        }
        else{
        
        	enableSlidingMenu();
        
        }

        
        getSlidingMenu().setBehindOffsetRes(R.dimen.show_content);
        
    }
    
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        
        if (item.getItemId() == android.R.id.home) {
            toggle(); 
            if(getSlidingMenu().isMenuShowing() && menu.isAdded()){
                setTitle(((ITitleable)menu).getTitle());
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    

    public void showContent(){
        getSlidingMenu().showContent();
    }
    
    public void enableSlidingMenu(){
     
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }
    
    
}
