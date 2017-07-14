package com.example.petrochina.model;

import com.example.petrochina.util.DataHexUtil;

public class FillInfo {
	public static final int SET_MONEY = 0;
	
	public static final int SET_LITRE = 1;
	
	public static final int SET_FREE = 2;
	
	public static final int COMMAND_CODE_MONEY = 33;
	
	public static final int COMMAND_CODE_OUTCARD = 40;
	
	public static final int MSG_SIZE_MONEY = 6;
	
	public static final int MSG_SIZE_OUTCARD = 2;
	
	public static final int MSG_SIZE_SETFULL = 3;
	
	DataHexUtil dhx = new DataHexUtil();
	
	public byte[] MsgMoney(String money){
		
		byte[] msg = new byte[2+MSG_SIZE_MONEY];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE_MONEY;
		msg[2] = COMMAND_CODE_MONEY;
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
		
		byte[] buffer = new byte[MSG_SIZE_MONEY-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE_MONEY-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE_MONEY+1] = (byte) vc;
		return msg;
	}
	
	public byte[] MsgLitre(String litre){
		byte[] msg = new byte[2+MSG_SIZE_MONEY];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE_MONEY;
		msg[2] = COMMAND_CODE_MONEY;
		msg[3] = SET_LITRE;
		byte[] content = new byte[3];
		int length = litre.length();
		switch (length) {
		case 1:
			litre = "000"+litre+"00";
			content = DataHexUtil.hexStringToBytes(litre);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;

		case 2:
			litre = "00"+litre+"00";
			content = DataHexUtil.hexStringToBytes(litre);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
			
		case 3:
			litre = "0"+litre+"00";
			content = DataHexUtil.hexStringToBytes(litre);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
			
		case 4:
			litre = litre+"00";
			content = DataHexUtil.hexStringToBytes(litre);
			msg[4] = content[0];
			msg[5] = content[1];
			msg[6] = content[2];
			break;
		default:
			break;
		}
		
		byte[] buffer = new byte[MSG_SIZE_MONEY-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE_MONEY-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE_MONEY+1] = (byte) vc;
		return msg;
	}
	
	public byte[] setFull(){
		byte[] msg = new byte[2+MSG_SIZE_SETFULL];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE_SETFULL;
		msg[2] = COMMAND_CODE_MONEY;
		msg[3] = SET_FREE;
		
		byte[] buffer = new byte[MSG_SIZE_SETFULL-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE_SETFULL-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE_SETFULL+1] = (byte) vc;
		return msg;
	}
	
	public byte[] outcard(){
		byte[] msg = new byte[2+MSG_SIZE_OUTCARD];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE_OUTCARD;
		msg[2] = COMMAND_CODE_OUTCARD;
		byte[] buffer = new byte[MSG_SIZE_OUTCARD-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE_OUTCARD-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE_OUTCARD+1] = (byte) vc;
		return msg;
	}
}
