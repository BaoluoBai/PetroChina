package com.example.petrochina.model;

import com.example.petrochina.util.DataHexUtil;

public class FillInfo {
	public static final int SET_MONEY = 0;
	
	public static final int SET_LITRE = 1;
	
	public static final int SET_FREE = 2;
	
	public static final int COMMAND_CODE = 15;
	
	public static final int MSG_SIZE = 6;
	
	public byte[] MsgMoney(String money){
		DataHexUtil dhx = new DataHexUtil();
		byte[] msg = new byte[2+MSG_SIZE];
		msg[0] = (byte) 0xfd;
		msg[1] = 6;
		msg[2] = COMMAND_CODE;
		msg[3] = SET_MONEY;
		byte[] content = new byte[3];
		int length = money.length();
		switch (length) {
		case 1:
			money = "000"+money+"00";
			content = DataHexUtil.hexStringToBytes(money);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;

		case 2:
			money = "00"+money+"00";
			content = DataHexUtil.hexStringToBytes(money);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
			
		case 3:
			money = "0"+money+"00";
			content = DataHexUtil.hexStringToBytes(money);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
			
		case 4:
			money = money+"00";
			content = DataHexUtil.hexStringToBytes(money);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
		default:
			break;
		}
		
		byte[] buffer = new byte[MSG_SIZE-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE+1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE+1] = (byte) vc;
		return msg;
	}
}
