package com.example.petrochina.model;

import com.example.petrochina.util.DataHexUtil;

public class PrintSelectOrNot {
	public static final int MSG_SIZE = 2;
	
	public static final int COMMAND_CODE = 38;
	
	public static final int PRINT_YES = 1;
	
	public static final int PRINT_NO = 0;
	
	public byte[] msg_38_print(int type){
		DataHexUtil dhx = new DataHexUtil();
		byte[] msg = new byte[2+MSG_SIZE];
		msg[0] = (byte) 0xfd;
		msg[1] = MSG_SIZE;
		msg[2] = COMMAND_CODE;
		switch (type) {
		case 0:
			msg[3] = PRINT_NO;
			break;
		case 1:
			msg[3] = PRINT_YES;
			break;
		default:
			break;
		}
		byte[] buffer = new byte[MSG_SIZE-1];
		buffer = dhx.subBytes(msg, 2, MSG_SIZE-1);
		int vc = dhx.checkVC(buffer);
		msg[MSG_SIZE+1] = (byte) vc;
		return msg;
	}
}
