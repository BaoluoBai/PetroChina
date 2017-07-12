package com.example.petrochina.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Util {
	public static final String TAG = "Util";
	static List<String> fileTempList = new ArrayList<String>();
	static List<String> pictureTempList = new ArrayList<String>();
	static String appName = "";
	public static void deleteAllFiles(String path) {
		LogUtil.d(TAG, "delete file");
		File root = new File(path);
        File files[] = root.listFiles();  
        if (files != null){
        	for (File f : files) {  
        		if (f.isDirectory()) { // 判断是否为文件夹  
        			deleteAllFiles(f.getAbsolutePath());  
        			try {  
        				f.delete();  
        			} catch (Exception e) {  
        			}  
        		} else {  
        			if (f.exists()) { // 判断是否存在  
        				deleteAllFiles(f.getAbsolutePath());  
        				try {  
        					f.delete();  
        				} catch (Exception e) {  
        				}  
        			}  
        		}  
        	}
        }         
    }
	
	
	public static List<String> doSearchVideo(String path) {		
		File file = new File(path);
		if (file.exists()) {	
			if (file.isDirectory()) {	
				File[] fileArray = file.listFiles();
				if(fileArray == null){
					
				}else{
					for (File f : fileArray) {
						if (f.isDirectory()) {
							doSearchVideo(f.getPath());
						} 
						else {
							if(f.getName().endsWith(".mp4") || f.getName().endsWith(".avi")){
								fileTempList.add(f.getName());
//								LogUtil.d(TAG, "list: "+ fileTempList.toString());
							}
						} 					
					}	
				}						
			} 			
		}
		return fileTempList;
	}
	
	public static List<String> doSearchPicture(String path){
		LogUtil.d(TAG, "doSearchPicture");
		
		File file = new File(path);
		if (file.exists()) {	
			if (file.isDirectory()) {	
				File[] fileArray = file.listFiles();
				if(fileArray == null){
					
				}else{
					for (File f : fileArray) {
						if (f.isDirectory()) {
							doSearchPicture(f.getPath());
						} 
						else {
							if(f.getName().endsWith(".jpg") || f.getName().endsWith(".png")){
								LogUtil.d(TAG, "file: "+f.getName());
								pictureTempList.add(f.getName());
//								LogUtil.d(TAG, "list: "+ fileTempList.toString());
							}
						} 					
					}	
				}						
			} 			
		}
		LogUtil.d(TAG, "list: "+ pictureTempList.toString());
		return pictureTempList;
	}
	
	public static String doSearchApk(String path){
		LogUtil.d(TAG, "doSearchApk");

		File file = new File(path);
		if (file.exists()) {	
			if (file.isDirectory()) {	
				File[] fileArray = file.listFiles();
				if(fileArray == null){
					
				}else{
					for (File f : fileArray) {
						if (f.isDirectory()) {
							doSearchPicture(f.getPath());
						} 
						else {
							if(f.getName().endsWith(".apk")){
								LogUtil.d(TAG, "file: "+f.getName());
								appName = f.getName();
//								LogUtil.d(TAG, "list: "+ fileTempList.toString());
							}else{
								appName = "";
							}
						} 					
					}	
				}						
			} 			
		}
		LogUtil.d(TAG, "list: "+ pictureTempList.toString());
		return appName;
	}
}
