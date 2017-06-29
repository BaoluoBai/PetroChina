package com.example.petrochina.model;

import com.example.petrochina.util.DataHexUtil;

public class Payment {
	public static final int PAY_CASH = 1;
	
	public static final int PAY_IC = 2;
	
	public static final int PAY_VISA = 3;
	
	public static final int PAY_CELLPHONE = 4;

	public static final int COMMAND_CODE = 35;
	
	public static final int MSG_SIZE = 3;
	
	public byte[] MsgPayment(int way){
		DataHexUtil dhx = new DataHexUtil();
		byte[] msg = new byte[2+MSG_SIZE];
		msg[0] = (byte) 0xfd;
		msg[1] = 3;
		msg[2] = COMMAND_CODE;
		switch (way) {
		case 0:
			msg[3] = PAY_CASH;
			break;
			
		case 1:
			msg[3] = PAY_IC;
			break;
		case 2:
			msg[3] = PAY_VISA;
			break;
		case 3:
			msg[3] = PAY_CELLPHONE;
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
