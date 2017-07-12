package com.example.petrochina;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.petrochina.model.ParamStatic;
import com.example.petrochina.util.LogUtil;
import com.example.petrochina.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends Activity{
public static final String TAG = "UpdateActivity";
	
	private TextView tv_count, tv_number;
	
    int cursor = 0;
	
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_activity);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_number = (TextView) findViewById(R.id.tv_number);
        count = getFileCount(ParamStatic.FROM_PATH);
        LogUtil.d(TAG, "文件总数为："+count);
        if(count == -1){
        	Toast.makeText(this, "该U盘中没有可识别文件", Toast.LENGTH_SHORT).show();
        	finish();
        }else{
        	tv_count.setText("/"+count+"个文件");
        	Util.deleteAllFiles(ParamStatic.TARGET_PATH);
    		copy(ParamStatic.FROM_PATH, ParamStatic.TARGET_PATH);
        }	
	}
	
	public void copy(final String fromFile, final String toFile){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//要复制的文件目录
		        File[] currentFiles;
		        File root = new File(fromFile);
		        //如同判断SD卡是否存在或者文件是否存在
		        //如果不存在则 return出去
		        if(root.exists())
		        {
		        	//如果存在则获取当前目录下的全部文件 填充数组
			        currentFiles = root.listFiles();
			         
			        //目标目录
			        File targetDir = new File(toFile);
			        //创建目录
			        if(!targetDir.exists())
			        {
			            targetDir.mkdirs();
			        }
			        //遍历要复制该目录下的全部文件
			        for(int i= 0;i<currentFiles.length;i++)
			        {
			            if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
			            {
			                copy(currentFiles[i].getPath() + "/", toFile + "/" + currentFiles[i].getName() + "/");
			                 
			            }else//如果当前项为文件则进行文件拷贝
			            {
			            	cursor++;
			            	LogUtil.d(TAG, "正在复制第"+cursor+"个文件");
			            	runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv_number.setText(cursor+"");
								}
							});
			                copySdcardFile(currentFiles[i].getPath(), toFile + "/" +currentFiles[i].getName());
			                if(cursor == count){
			                	runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(UpdateActivity.this, "文件已经复制完成", Toast.LENGTH_SHORT).show();
									}
								});
			                	
			                	String app = Util.doSearchApk(ParamStatic.TARGET_PATH);
			                	LogUtil.i(TAG, "app name: "+ app);
			                	if(app.equals("")){
			                		finish();
			                	}else{
			                		Log.e("install","new version");
			                		Intent intent = new Intent();
			                		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                		intent.setAction(android.content.Intent.ACTION_VIEW);
			                		intent.setDataAndType(Uri.fromFile(new File(ParamStatic.TARGET_PATH+"/"+app)),
			                				"application/vnd.android.package-archive");
			                		startActivity(intent);
			                	}
			                }
			            }
			        }
		        }  
			}
		}).start();
        
    }
	
	 //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
	public int copySdcardFile(String fromFile, String toFile){ 
        try{
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;             
        } catch (Exception ex){
            return -1;
        }
    }
    
    public int getFileCount(String fromFile){
    	File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if(!root.exists())
        {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
    	for(int j = 0; j<currentFiles.length; j++){
    		 if(currentFiles[j].isDirectory())//如果当前项为子目录 进行递归
             {
    			 getFileCount(currentFiles[j].getPath());
                  
             }else//如果当前项为文件则进行文件拷贝
             {
                 count++;
             }
        }
    	return count;
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.i(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i(TAG, "onDestroy");
	}
}
