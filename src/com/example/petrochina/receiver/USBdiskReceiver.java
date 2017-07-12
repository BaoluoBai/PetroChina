package com.example.petrochina.receiver;


import com.example.petrochina.MainActivity;
import com.example.petrochina.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class USBdiskReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
        String path = intent.getData().getPath();
        LogUtil.d("usb",path);
        String unmounted = "U盘已移除";
        String mounted = "U盘已插入";
        if (!TextUtils.isEmpty(path)){
        	Bundle bundle = intent.getExtras(); 
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                LogUtil.d("usb",unmounted + path);
                processUnmountedMessage(context, bundle);
            }
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                LogUtil.d("usb",mounted + path);
                processMountedMessage(context, bundle);
            }
        }
	}
	
	private void processUnmountedMessage(Context context, Bundle bundle) {  
        
        Intent mIntent=new Intent(MainActivity.ACTION_INTENT_RECEIVER);  
        mIntent.putExtra("message", "false");  
        context.sendBroadcast(mIntent);  
          
	}
	
	private void processMountedMessage(Context context, Bundle bundle) {  
        
        Intent mIntent=new Intent(MainActivity.ACTION_INTENT_RECEIVER);  
        mIntent.putExtra("message", "true");  
        context.sendBroadcast(mIntent);    
	}

}
