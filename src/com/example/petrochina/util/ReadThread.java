package com.example.petrochina.util;

import android.annotation.SuppressLint;

public class ReadThread implements Runnable {
	public static final String TAG = "ReadThread";

	@SuppressLint("SdCardPath") @Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String data = Util.getDataFromFile("/sys/class/gpio/gpio83/value");
			LogUtil.d(TAG, "data:"+data);
			if(data.equals("0")){
				Util.writeDataToFile("1", "/sdcard/.conf", false);
			}else if(data.equals("1")){
				Util.writeDataToFile("0", "/sdcard/.conf", false);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
