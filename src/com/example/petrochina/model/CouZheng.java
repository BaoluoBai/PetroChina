package com.example.petrochina.model;

import com.example.petrochina.util.DataHexUtil;

public class CouZheng {
	public static final int MSG_SIZE = 2;
	
	public static final int COMMAND_CODE = 39;
	
	public byte[] msg_39_couzheng(){
		DataHexUtil dhx = new DataHexUtil();
		byte[] msg = new byte[2+MSG_SIZE];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE;
		msg[2] = COMMAND_CODE;
		byte[] buffer = new byte[MSG_SIZE-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE+1] = (byte) vc;
		return msg;
	}
}
