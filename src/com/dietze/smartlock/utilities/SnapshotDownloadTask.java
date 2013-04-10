package com.dietze.smartlock.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SnapshotDownloadTask extends AsyncTask<URL,Integer,Long> {
	

	

	@Override
	protected Long doInBackground(URL ... urls) {
		InputStream input=null;
		try {
			
			URL url = urls[0];
			input = url.openStream();
		    //The sdcard directory e.g. '/sdcard' can be used directly, or 
		    //more safely abstracted with getExternalStorageDirectory()
		    
		    File folder = new File(Environment.getExternalStorageDirectory() + "/SmartLock");
		    boolean success = true;
		    if (!folder.exists()) {
		        success = folder.mkdir();
		        Log.d("Snapshot Downloader","Folder Doesn't Exist: "+folder.getAbsolutePath());
		    }
		    if (success) {
		    	Log.d("Snapshot Downloader","Folder Exists: "+folder.getAbsolutePath());
		    } else {
		        // Do something else on failure 
		    }
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss",Locale.US);
		    Date dt = new Date();
		    String S = sdf.format(dt);
		    
		    
		    OutputStream output = new FileOutputStream (new File(folder.getAbsolutePath(),S+".png"));
		    try {
		        byte[] buffer = new byte[2048];
		        int bytesRead = 0;
		        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
		            output.write(buffer, 0, bytesRead);
		        }
		    } finally {
		        output.close();
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 finally {
		    try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	@Override
	protected void onPostExecute(Long result) {
        Log.d("Camera Downloader","Snapshot Saved");
    }
	

}

