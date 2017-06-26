package com.example.petrochina.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.petrochina.model.Oil;

public class DataHexUtil {
	public static final String TAG = "DataHexUtil";
	public List<Oil> handleOilData(byte[] buffer){
		int gunCount = getGunCount(buffer);
		List<Oil> oil_list = new ArrayList<Oil>();
		for(int i = 0; i<gunCount; i++){
			int flag = i*5;
			Oil oil = new Oil();
			oil.setUnit_price(handlePrice(subBytes(buffer,4+flag,2)));
			oil.setGun_number(handleGunNumber(subBytes(buffer, 6+flag, 1)));
			oil.setOil_kind(handleOilKind(subBytes(buffer, 7+flag, 2)));
			oil_list.add(oil);
		}
		return oil_list;
	}
	/**
	 * 处理加油方式放回结果
	 * @param buffer
	 */
	public void handlPaymentResult(byte[] buffer){
		
	}
	
	public String handlePrice(byte[] buffer){
		byte[] danwei = subBytes(buffer, 0, 1);
		// tv.setText(binary);
 		int danwei_binary = binary(danwei);
 		String str = Integer.toHexString(danwei_binary);
 		if(str.equals("0")){
 			str = "";
 		}
 		danwei = subBytes(buffer, 1, 1);
 		danwei_binary = binary(danwei);
 		String number_2 = Integer.toHexString(danwei_binary);
 		if(number_2.length() == 1){
 			number_2 = "0"+number_2;
 		}
 		str += "."+number_2;
 		Log.d(TAG, "单价为: "+ str);
 		return str;
	}
	
	private String handleGunNumber(byte[] buffer){
		int gunNumber = binary(buffer);
		String gun = Integer.toHexString(gunNumber)+"号枪";
		LogUtil.d(TAG, "枪号为: "+gun);
		return gun;
	}
	
	private String handleOilKind(byte[] buffer){
		byte[] danwei = subBytes(buffer, 0, 1);
		String oil_kind = "";
		// tv.setText(binary);
 		int gun_high = binary(danwei);
 		String str = Integer.toHexString(gun_high);
 		if(str.equals("0")){
 			str = "";
 		}
 		danwei = subBytes(buffer, 1, 1);
 		int gun_low = binary(danwei);
 		String number_2 = Integer.toHexString(gun_low);
 		if(number_2.equals("0")){
 			number_2 = "0";
 		}
 		String oil = str+number_2;
 		int oil_integer = Integer.parseInt(oil);
 		LogUtil.d(TAG, "整形的油品为: "+oil_integer);
 		if(oil_integer>100){
 			oil_integer = oil_integer-100;
 			oil_kind = "-"+oil_integer+"#";
 		}else{
 			oil_kind = oil_integer+"#";
 		}
 		LogUtil.d(TAG, "换算好的油品为: "+oil_kind);
 		return oil_kind;
	}
	
	public byte[] subBytes(byte[] src, int begin, int count) {  
	    byte[] bs = new byte[count];  
	    System.arraycopy(src, begin, bs, 0, count);  
	    return bs;  
	}
	
	private int binary(byte bys[]) {
  		int big_bytesToInt = little_bytesToInt(bys);
  		return big_bytesToInt;
  	}
  	// 小端转int
  	public int little_bytesToInt(byte[] bytes) {
  	 	int addr = 0;
  	 	if (bytes.length == 1) {
  	 		addr = bytes[0] & 0xFF;
  	 	} else if (bytes.length == 2) {
  	 		addr = bytes[0] & 0xFF;
  	 		addr |= (((int) bytes[1] << 8) & 0xFF00);
  	 	} else if(bytes.length == 3){
  	 		addr = bytes[0] & 0xFF;
  	 		addr |= (((int) bytes[1] << 8) & 0xFF00);
  	 		addr |= (((int) bytes[2] << 16) & 0xFF0000);
  	 	}else {
  	 		addr = bytes[0] & 0xFF;
  	 		addr |= (((int) bytes[1] << 8) & 0xFF00);
  	 		addr |= (((int) bytes[2] << 16) & 0xFF0000);
  	 		addr |= (((int) bytes[3] << 24) & 0xFF000000);
  	 	}
  	 	return addr;
  	 }
  	
  	
  	private int getGunCount(byte[] buffer){
  		int count = binary(subBytes(buffer, 3, 1));
  		LogUtil.d(TAG, "一共有 "+count+" 把枪");
  		return count;
  	}
  	
	
  	public int checkVC(byte[] buffer){
  		int vc = 0x00;
  		for(int i=0;i<buffer.length-1;i++){
  			if(i==0){
  				vc = (buffer[i]&0xFF)^(buffer[i+1]&0xFF);
  			}else{
  				vc ^= (buffer[i]&0xFF);
  			}
  		}
  		
		return (vc&0xFF);
  		
  	}
}
