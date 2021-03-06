package com.example.petrochina.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.petrochina.model.Oil;
import com.example.petrochina.model.Password;

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
	
	public Map<String, String> handle_13_MSG(byte[] buffer){
		Map<String, String> map = new HashMap<String, String>();
		switch (buffer[3] & 0xFF) {
		case 0x01:
			map.put("view_number", "1");
			break;
		case 0x02:
			int view = buffer[4]&0xFF;
			map.put("view_number", "2");
			map.put("count", view+"");
			break;
		case 0x03:
			map.put("view_number", "3");
			//1:获取余额长度
			byte[] sizeOfMoney = subBytes(buffer, 4, 1);
			int sizeOfMoney_int = binary(sizeOfMoney);
			LogUtil.d(TAG, "余额长度为："+sizeOfMoney_int);
			//2:获取余额字节数组
			byte[] lastOfMoney = subBytes(buffer, 5, sizeOfMoney_int);
			//3:开始解析余额字节数组
			String tmpMoney = "";
			byte[] tmpByte = new byte[1];
			int tmpMoneyOfInt = 0;
			String money = "";
			for(int i = 0; i<lastOfMoney.length; i++){
				tmpByte = subBytes(lastOfMoney, i, 1);
				tmpMoneyOfInt = binary(tmpByte);
				tmpMoney = Integer.toHexString(tmpMoneyOfInt);
				if(i == 0){
					if(tmpMoney.equals("0")){
						money = "";
					}else{
						money = tmpMoney;
					}
				}else if((lastOfMoney.length-1)==i) {
					if(tmpMoney.length()==1){
						tmpMoney = ".0"+tmpMoney;
						money = money+tmpMoney;
					}else{
						money = money+"."+tmpMoney;
					}
				}else{
					if(tmpMoney.length()==1){
						tmpMoney = "0"+tmpMoney;
						money = money+tmpMoney;
					}else{
						money = money+tmpMoney;
					}
				}
			}
			LogUtil.d(TAG, "卡余额："+money);
			map.put("money", money);
			//4:解析卡状态
			break;
		case 0x04:
			map.put("view_number", "4");
			break;
		case 0x05:
			map.put("view_number", "5");
			break;
		case 0x06:
			map.put("view_number", "6");
			break;
		case 0x07:
			map.put("view_number", "7");
			break;
		case 0x08:
			map.put("view_number", "8");
			byte[] tempMoney = subBytes(buffer, 5, buffer[4] & 0xFF);
			String cutMoney = handleRMB(tempMoney);
			LogUtil.d(TAG, "扣款为:"+cutMoney);
			map.put("cut_money", cutMoney);
			int point = (buffer[4] & 0xFF)+4+1;
			byte[] tempLastMoney = subBytes(buffer, (buffer[4] & 0xFF)+5+1, (buffer[point] & 0xFF));
			String lastMoney = handleRMB(tempLastMoney);
			LogUtil.d(TAG, "余额为:"+lastMoney);
			map.put("last_money", lastMoney);
			LogUtil.d(TAG, ""+(buffer[1] & 0xFF));
			int printyesorno = buffer[(buffer[1] & 0xFF)]&0xFF;
			if(printyesorno == 0){
				map.put("yes_no", "no");
			}else if(printyesorno == 1){
				map.put("yes_no", "yes");
			}else{
				map.put("yes_no", "error");
			}
			break;
		case 0x31:
			byte[] tmp_collection = subBytes(buffer, 5, (buffer[4]&0xFF));
			String collection = handleCollection(tmp_collection);
			map.put("view_number", "31");
			map.put("collection", collection);
			int lenghtOfCol = buffer[4]&0xFF;
			int startOfWel = 6+lenghtOfCol;
			int lengthOfWel = buffer[5+lenghtOfCol]&0XFF;
			byte[] tmp_welcome = subBytes(buffer, startOfWel, lengthOfWel);
			String welcome = "";
			try {
				welcome = new String(tmp_welcome, "GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				welcome = "";
			}
			map.put("welcome", welcome);
			break;
		default:
			map.put("view_number", "0");
			break;
		}
		return map;
	}
	
	/**
	 * 处理加油方式放回结果
	 * @param buffer
	 */
	public boolean handlPaymentResult(byte[] buffer){
		boolean allow = false;
		if((buffer[3]&0xFF) == 0x00){
			allow = true;
		}else{
			allow = false;
		}
		return allow;
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
	
	public int binary(byte bys[]) {
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
  		if(buffer.length == 1){
  			vc ^= (buffer[0]&0xFF);
  		}else{
  			for(int i=0;i<buffer.length-1;i++){
  	  			if(i==0){
  	  				vc = (buffer[i]&0xFF)^(buffer[i+1]&0xFF);
  	  			}else{
  	  				vc ^= (buffer[i+1]&0xFF);
  	  			}
  	  		}
  		}
		return (vc&0xFF);
  		
  	}
  	
  	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    public String handleRMB(byte[] buffer){
    	String str = "";
    	for(int i = 0; i<buffer.length; i++){
    		//截取需要解析的byte数组
    		byte[] rmb = subBytes(buffer, i, 1);
    		//逐个解析
    		int rmb_binary = binary(rmb);
    		String tmp=Integer.toHexString(rmb_binary);
    		if(i==buffer.length-1){
    			str = str+"."+tmp;
    		}else{
    			str += tmp;
    		}
    		
    	}
    	LogUtil.d(TAG, "金额为:"+str);
    	
		return str;
 	}
    
    public String handleCollection(byte[] buffer){
    	String str = "";
    	for(int i = 0; i<buffer.length; i++){
    		byte[] collection = subBytes(buffer, i, 1);
    		//逐个解析
    		int col_binary = binary(collection);
    		String tmp=Integer.toHexString(col_binary);
    		str +=tmp;
    	}
    	LogUtil.d(TAG, "积分为:"+str);
    	return str;
    }
}
