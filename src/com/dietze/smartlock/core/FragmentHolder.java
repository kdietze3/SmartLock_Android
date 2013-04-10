package com.dietze.smartlock.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.dietze.smartlock.R;
import com.dietze.smartlock.utilities.ITitleable;

public class FragmentHolder extends SherlockFragment implements ITitleable{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_holder, container, false);
        
       return v;

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
}
