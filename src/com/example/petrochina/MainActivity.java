package com.example.petrochina;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.petrochina.model.CouZheng;
import com.example.petrochina.model.FillInfo;
import com.example.petrochina.model.Oil;
import com.example.petrochina.model.ParamStatic;
import com.example.petrochina.model.Payment;
import com.example.petrochina.model.PrintSelectOrNot;
import com.example.petrochina.ui.CustomView;
import com.example.petrochina.util.DataHexUtil;
import com.example.petrochina.util.LogUtil;
import com.example.petrochina.util.SerialPortUtil;
import com.example.petrochina.util.SerialPortUtil.OnDataReceiveListener;
import com.example.petrochina.util.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity{
	public static String ACTION_INTENT_RECEIVER = "com.gc.broadcast.receiver"; 
	
	private static final String PORT_ONE = "/dev/ttymxc1";
	private static final String PORT_TWO = "/dev/ttymxc2";
	private static final int BAUDRATE = 9600;
	
	public String param = "";
	public int video_position = 0;
	
	public MessageReceiver mMessageReceiver; 
	
	private RelativeLayout mRelaytiveLayout_one, mRelaytiveLayout_two;
	private View mView_one, mView_two;
	private ImageView iv_oilcard_one, iv_visacard_one, iv_cash_one, iv_mobilepay_one,
	iv_oilcard_two, iv_visacard_two, iv_cash_two, iv_mobilepay_two, iv_setmoney_one,
	iv_setfill_one, iv_full_one, iv_setmoney_two,iv_setfill_two, iv_full_two;
	
	private Button btn_outcard_one, btn_100_one, btn_200_one, btn_500_one, btn_1000_one,
	btn_cancel_one, btn_confirm_one, btn_1_one, btn_2_one, btn_3_one, btn_4_one, btn_5_one,
	btn_6_one, btn_7_one, btn_8_one, btn_9_one, btn_0_one, btn_delete_one, btn_sure_one, btn_couzheng_one,
	btn_printyes_one, btn_printno_one, btn_outcard_two, btn_100_two, btn_200_two, btn_500_two, btn_1000_two,
	btn_cancel_two, btn_confirm_two, btn_1_two, btn_2_two, btn_3_two, btn_4_two, btn_5_two,
	btn_6_two, btn_7_two, btn_8_two, btn_9_two, btn_0_two, btn_delete_two, btn_sure_two, btn_couzheng_two,
	btn_printyes_two, btn_printno_two;
	
	private TextView tv_param_one, tv_password_one, tv_tips_one, tv_lastmoney_one, tv_kindoftext_one,
	tv_danwei_one, tv_paymoney_one, tv_yue_one, tv_password_two, tv_tips_two, tv_param_two, tv_kindoftext_two,
	tv_danwei_two, tv_lastmoney_two, tv_paymoney_two, tv_yue_two, tv_unitprice_one, tv_oils_one, tv_gun_one,
	tv_unitprice_two, tv_oils_two, tv_gun_two, tv_unitprice_three, tv_oils_three, tv_gun_three, tv_unitprice_four, tv_oils_four, tv_gun_four;
	
	public SerialPortUtil serialPortOne = null;
	public SerialPortUtil serialPortTwo = null;
	public DataHexUtil dataUtil = null;
	public Payment payment;
	private CustomView vv_ad;
	List<String> filename = new ArrayList<String>(); 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        serialPortOne = new SerialPortUtil(PORT_ONE, BAUDRATE);
        serialPortTwo = new SerialPortUtil(PORT_TWO, BAUDRATE);
        payment = new Payment();
        dataUtil = new DataHexUtil();
        initView();
        serialPortOne.setOnDataReceiveListener(new OnDataReceiveListener() {
			
			@Override
			public void onDataReceive(byte[] buffer, int size) {
				// TODO Auto-generated method stub
				switch (buffer[2]) {
				case 0x13:
					Map<String, String> map = new HashMap<String, String>();
					map = dataUtil.handle_13_MSG(buffer);
					String view = map.get("view_number");
					if(view.equals("2")){
						//处理输入密码页面逻辑
						one_handle_02_view(map);	
					}else if(view.equals("3")){
						//处理预置量输入页面逻辑
						one_handle_03_view(map);
					}else if(view.equals("4")){
						display_one(4);
					}else if(view.equals("5")){
						display_one(5);
					}else if(view.equals("6")){
						display_one(6);
					}else if(view.equals("7")){
						display_one(7);
					}else if(view.equals("8")){
						one_handle_08_view(map);
					}
					break;
				case 0x20:
					List<Oil> oils = new ArrayList<Oil>();
					oils = dataUtil.handleOilData(buffer);
					one_handle_32_view(oils);
					break;
				case 0x23:
					boolean allow = dataUtil.handlPaymentResult(buffer);
					if(allow == true){
						display_one(R.layout.one_insercard_view_30);
					}
				default:
					break;
				}
			}
		});
        
        serialPortTwo.setOnDataReceiveListener(new OnDataReceiveListener() {
			
			@Override
			public void onDataReceive(byte[] buffer, int size) {
				// TODO Auto-generated method stub
				switch (buffer[2]) {
				case 0x13:
					Map<String, String> map = new HashMap<String, String>();
					map = dataUtil.handle_13_MSG(buffer);
					String view = map.get("view_number");
					if(view.equals("2")){
						//处理输入密码页面逻辑
						two_handle_02_view(map);	
					}else if(view.equals("3")){
						//处理预置量输入页面逻辑
						two_handle_03_view(map);
					}else if(view.equals("4")){
						display_two(4);
					}else if(view.equals("5")){
						display_two(5);
					}else if(view.equals("6")){
						display_two(6);
					}else if(view.equals("7")){
						display_two(7);
					}else if(view.equals("8")){
						two_handle_08_view(map);
					}
					break;
				case 0x20:
					List<Oil> oils = new ArrayList<Oil>();
					oils = dataUtil.handleOilData(buffer);
					two_handle_32_view(oils);
					break;
				case 0x23:
					boolean allow = dataUtil.handlPaymentResult(buffer);
					if(allow == true){
						display_two(R.layout.one_insercard_view_30);
					}
				default:
					break;
				}
			}
		});
    }
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		filename.clear();
        filename = Util.doSearchVideo(ParamStatic.TARGET_PATH);
        if(filename.isEmpty()){
        	Toast.makeText(this, "没有找到可播放的视频文件", Toast.LENGTH_SHORT).show();
        }else{
        	playVideo(filename);
        }
	}


	private void initView(){
    	registerMessageReceiver();
    	tv_unitprice_one = (TextView) findViewById(R.id.tv_unitprice_one);
    	tv_oils_one = (TextView) findViewById(R.id.tv_oils_one);
    	tv_gun_one = (TextView) findViewById(R.id.tv_gun_one);
    	tv_unitprice_two = (TextView) findViewById(R.id.tv_unitprice_two);
    	tv_oils_two = (TextView) findViewById(R.id.tv_oils_two);
    	tv_gun_two = (TextView) findViewById(R.id.tv_gun_two);
    	tv_unitprice_three = (TextView) findViewById(R.id.tv_unitprice_three);
    	tv_oils_three = (TextView) findViewById(R.id.tv_oils_three);
    	tv_gun_three = (TextView) findViewById(R.id.tv_gun_three);
    	tv_unitprice_four = (TextView) findViewById(R.id.tv_unitprice_four);
    	tv_oils_four = (TextView) findViewById(R.id.tv_oils_four);
    	tv_gun_four = (TextView) findViewById(R.id.tv_gun_four);
    	mRelaytiveLayout_one = (RelativeLayout) findViewById(R.id.view_number_one);
    	mRelaytiveLayout_two = (RelativeLayout) findViewById(R.id.view_number_two);
    	vv_ad = (CustomView) findViewById(R.id.vv_advertisement);
    	display_one(0);
    	display_two(0);
    }

    
    public void display_one(int view_number){
    	switch (view_number) {
		case 0://支付方式
			mView_one = View.inflate(this, R.layout.one_payment_view_01, null);
	    	iv_oilcard_one = (ImageView) mView_one.findViewById(R.id.tv_oilcard_one);
	    	iv_oilcard_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_oilcard_one");
					serialPortOne.sendBuffer(payment.MsgPayment(1));
				}
			});
	    	iv_visacard_one = (ImageView) mView_one.findViewById(R.id.tv_visacard_one);
	    	iv_visacard_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_visacard_one");
					serialPortOne.sendBuffer(payment.MsgPayment(2));
				}
			});
	    	iv_cash_one = (ImageView) mView_one.findViewById(R.id.tv_cash_one);
	    	iv_cash_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_cash_one");
					serialPortOne.sendBuffer(payment.MsgPayment(0));
				}
			});
	    	iv_mobilepay_one = (ImageView) mView_one.findViewById(R.id.tv_mobilepay_one);
	    	iv_mobilepay_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_mobilepay_one");
					serialPortOne.sendBuffer(payment.MsgPayment(3));
				}
			});
			break;
		case 1://请插卡
			mView_one = View.inflate(this, R.layout.one_insercard_view_30, null);
			break;
		case 2://请输入密码
			mView_one = View.inflate(this, R.layout.one_password_view_02, null);
			break;
		case 3://预制量输入
			mView_one = View.inflate(this, R.layout.one_setting_view_03, null);
			one_initPageThree();
			break;
		case 4://请提枪加油
			mView_one = View.inflate(this, R.layout.one_pickgun_view_04, null);
			break;
		case 5:
			mView_one = View.inflate(this, R.layout.one_confirmoilagain_view_05, null);
			break;
		case 6:
			mView_one = View.inflate(this, R.layout.one_fillingup_view_06, null);
			final CouZheng cz = new CouZheng();
			btn_couzheng_one = (Button) mView_one.findViewById(R.id.btn_couzheng_one);
			btn_couzheng_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					serialPortOne.sendBuffer(cz.msg_39_couzheng());
				}
			});
			break;
		case 7:
			mView_one = View.inflate(this, R.layout.one_fillfinish_view_07, null);
			break;
		case 8:
			mView_one = View.inflate(this, R.layout.one_payinfo_view_08, null);
			break;
		default:
			break;
		}
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		    	mRelaytiveLayout_one.removeAllViews();
		    	mRelaytiveLayout_one.addView(mView_one);
			}
		});
    }
    
    public void display_two(int view_number){
    	switch (view_number) {
		case 0://支付方式
			mView_two = View.inflate(this, R.layout.two_payment_view_01, null);
			iv_oilcard_two = (ImageView) mView_two.findViewById(R.id.tv_oilcard_two);
	    	iv_oilcard_two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_oilcard_two");
					serialPortTwo.sendBuffer(payment.MsgPayment(1));
				}
			});
	    	iv_visacard_two = (ImageView) mView_two.findViewById(R.id.tv_visacard_two);
	    	iv_visacard_two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_visacard_two");
					serialPortTwo.sendBuffer(payment.MsgPayment(2));
				}
			});
	    	iv_cash_two = (ImageView) mView_two.findViewById(R.id.tv_cash_two);
	    	iv_cash_two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_cash_two");
					serialPortTwo.sendBuffer(payment.MsgPayment(0));
				}
			});
	    	iv_mobilepay_two = (ImageView) mView_two.findViewById(R.id.tv_mobilepay_two);
	    	iv_mobilepay_two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("点击事件", "iv_mobilepay_two");
					serialPortTwo.sendBuffer(payment.MsgPayment(3));
				}
			});
			break;
		case 1://请插卡
			mView_two = View.inflate(this, R.layout.two_insertcard_view_30, null);
			break;
		case 2://请输入密码
			mView_two = View.inflate(this, R.layout.two_password_view_02, null);
			break;
		case 3://预制量输入
			mView_two = View.inflate(this, R.layout.two_setting_view_03, null);
			two_initPageThree();
			break;
		case 4://请提枪加油
			mView_two = View.inflate(this, R.layout.two_pickgun_view_04, null);
			break;
		case 5://再次确认油品
			mView_two = View.inflate(this, R.layout.two_confirmoilagain_view_05, null);
			break;
		case 6://加油中
			mView_two = View.inflate(this, R.layout.two_fillingup_view_06, null);
			final CouZheng cz = new CouZheng();
			btn_couzheng_two = (Button) mView_two.findViewById(R.id.btn_couzheng_two);
			btn_couzheng_two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					serialPortOne.sendBuffer(cz.msg_39_couzheng());
				}
			});
			break;
		case 7://加油完毕挂枪
			mView_two = View.inflate(this, R.layout.two_fillfinish_view_07, null);
			break;
		case 8://支付信息
			mView_two = View.inflate(this, R.layout.two_payinfo_view_08, null);
			break;
		default:
			break;
		}
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mRelaytiveLayout_two.removeAllViews();
		    	mRelaytiveLayout_two.addView(mView_two);
			}
		});
    }
    
	//处理输入密码页面逻辑
	private void one_handle_02_view(Map<String, String> map){
		display_one(2);
		String cnt = map.get("count");
		int count = 0;
		try {
			count = Integer.parseInt(cnt);
		} catch (NumberFormatException e) {
		    Log.e("String转int", "不是整形的数");
		}
		if(count < 6){
			String pwd = "";
			for(int i = 1; i<=count; i++){
				pwd+="*";
			}
			final String password = pwd;
			if(tv_password_one == null){
				tv_password_one = (TextView) findViewById(R.id.tv_icpwd_one);	
			}
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tv_password_one.setText(password);
				}
			});
		}else{
			if(tv_tips_one == null){
				tv_tips_one = (TextView) findViewById(R.id.tv_tips_one);	
			}
			if(count == 11){
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_tips_one.setText("密码错误，请重新输入");
					}
				});
			}else if(count == 12){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_tips_one.setText("密码错误次数超限");
					}
				});
			}
		}
	}
	
	private void two_handle_02_view(Map<String, String> map){
		display_two(2);
		String cnt = map.get("count");
		int count = 0;
		try {
			count = Integer.parseInt(cnt);
		} catch (NumberFormatException e) {
		    Log.e("String转int", "不是整形的数");
		}
		if(count < 6){
			String pwd = "";
			for(int i = 1; i<=count; i++){
				pwd+="*";
			}
			final String password = pwd;
			if(tv_password_two == null){
				tv_password_two = (TextView) findViewById(R.id.tv_icpwd_two);	
			}
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tv_password_two.setText(password);
				}
			});
		}else{
			if(tv_tips_two == null){
				tv_tips_two = (TextView) findViewById(R.id.tv_tips_two);	
			}
			if(count == 11){
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_tips_two.setText("密码错误，请重新输入");
					}
				});
			}else if(count == 12){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_tips_two.setText("密码错误次数超限");
					}
				});
			}
		}
	}
	
	//处理预置量输入页面逻辑
	private void one_handle_03_view(Map<String, String> map){
		display_one(3);
		final String money = map.get("money");
		tv_lastmoney_one = (TextView) mView_one.findViewById(R.id.tv_lastmoney_one);
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_lastmoney_one.setText("卡余额:"+money);
			}
		});
	}
	
	private void two_handle_03_view(Map<String, String> map){
		display_two(3);
		final String money = map.get("money");
		tv_lastmoney_two = (TextView) mView_two.findViewById(R.id.tv_lastmoney_two);
		two_initPageThree();
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_lastmoney_two.setText("卡余额:"+money);
			}
		});
	}
	
	private void one_handle_08_view(final Map<String, String> map){
		display_one(8);
		tv_paymoney_one = (TextView) mView_one.findViewById(R.id.tv_paymoney_one);
		tv_yue_one = (TextView) mView_one.findViewById(R.id.tv_yue_one);
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_paymoney_one.setText(map.get("cut_money"));
				tv_yue_one.setText(map.get("last_money"));
			}
		});
		btn_printyes_one = (Button) mView_one.findViewById(R.id.btn_printyes_one);
		btn_printyes_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PrintSelectOrNot psn = new PrintSelectOrNot();
				// TODO Auto-generated method stub
				serialPortOne.sendBuffer(psn.msg_38_print(1));
			}
		});
		btn_printno_one = (Button) mView_one.findViewById(R.id.btn_printno_one);
		btn_printno_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PrintSelectOrNot psn = new PrintSelectOrNot();
				// TODO Auto-generated method stub
				serialPortOne.sendBuffer(psn.msg_38_print(0));
			}
		});
		
	}
	
	private void two_handle_08_view(final Map<String, String> map){
		display_two(8);
		tv_paymoney_two = (TextView) mView_two.findViewById(R.id.tv_paymoney_two);
		tv_yue_two = (TextView) mView_two.findViewById(R.id.tv_yue_two);
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_paymoney_two.setText(map.get("cut_money"));
				tv_yue_two.setText(map.get("last_money"));
			}
		});
		btn_printyes_two = (Button) mView_two.findViewById(R.id.btn_printyes_two);
		btn_printyes_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtil.d("点击事件", "btn_printyes_two");
				PrintSelectOrNot psn = new PrintSelectOrNot();
				// TODO Auto-generated method stub
				serialPortTwo.sendBuffer(psn.msg_38_print(1));
			}
		});
		btn_printno_two = (Button) mView_two.findViewById(R.id.btn_printno_two);
		btn_printno_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtil.d("点击事件", "btn_printno_two");
				PrintSelectOrNot psn = new PrintSelectOrNot();
				// TODO Auto-generated method stub
				serialPortTwo.sendBuffer(psn.msg_38_print(0));
			}
		});
		
	}
	
	private void one_handle_32_view(List<Oil> list){
		if(list.size() == 1){
			tv_unitprice_one.setText(list.get(0).getUnit_price());
			tv_oils_one.setText(list.get(0).getOil_kind());
			tv_gun_one.setText(list.get(0).getGun_number());
		}else if(list.size() == 2){
			tv_unitprice_one.setText(list.get(0).getUnit_price());
			tv_oils_one.setText(list.get(0).getOil_kind());
			tv_gun_one.setText(list.get(0).getGun_number());
			tv_unitprice_two.setText(list.get(1).getUnit_price());
			tv_oils_two.setText(list.get(1).getOil_kind());
			tv_gun_two.setText(list.get(1).getGun_number());
		}
	}
	
	private void two_handle_32_view(final List<Oil> list){
		if(list.size() == 1){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tv_unitprice_four.setText(list.get(0).getUnit_price());
					tv_oils_four.setText(list.get(0).getOil_kind());
					tv_gun_four.setText(list.get(0).getGun_number());
				}
			});
			
		}else if(list.size() == 2){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tv_unitprice_three.setText(list.get(0).getUnit_price());
					tv_oils_three.setText(list.get(0).getOil_kind());
					tv_gun_three.setText(list.get(0).getGun_number());
					tv_unitprice_four.setText(list.get(1).getUnit_price());
					tv_oils_four.setText(list.get(1).getOil_kind());
					tv_gun_four.setText(list.get(1).getGun_number());
				}
			});
			
		}
	}
	
	private void one_initPageThree(){
		final FillInfo fi = new FillInfo();
		tv_param_one = (TextView) mView_one.findViewById(R.id.tv_param_one);
		iv_setmoney_one = (ImageView) mView_one.findViewById(R.id.iv_setmoney_one);
		iv_setfill_one = (ImageView) mView_one.findViewById(R.id.iv_setfill_one);
		iv_full_one = (ImageView) mView_one.findViewById(R.id.iv_full_one);
		
		tv_kindoftext_one = (TextView) mView_one.findViewById(R.id.tv_kindoftext_one);
		tv_danwei_one = (TextView) mView_one.findViewById(R.id.tv_danwei_one);
		
		
		btn_outcard_one = (Button) mView_one.findViewById(R.id.btn_cardout_one);
		btn_100_one = (Button) mView_one.findViewById(R.id.btn_100_one);
		btn_200_one = (Button) mView_one.findViewById(R.id.btn_200_one);
		btn_500_one = (Button) mView_one.findViewById(R.id.btn_500_one);
		btn_1000_one = (Button) mView_one.findViewById(R.id.btn_1000_one);
		btn_cancel_one = (Button) mView_one.findViewById(R.id.btn_cancel_one);
		btn_confirm_one = (Button) mView_one.findViewById(R.id.btn_confirm_one);
		btn_1_one = (Button) mView_one.findViewById(R.id.btn_1_one);
		btn_2_one = (Button) mView_one.findViewById(R.id.btn_2_one);
		btn_3_one = (Button) mView_one.findViewById(R.id.btn_3_one);
		btn_4_one = (Button) mView_one.findViewById(R.id.btn_4_one);
		btn_5_one = (Button) mView_one.findViewById(R.id.btn_5_one);
		btn_6_one = (Button) mView_one.findViewById(R.id.btn_6_one);
		btn_7_one = (Button) mView_one.findViewById(R.id.btn_7_one);
		btn_8_one = (Button) mView_one.findViewById(R.id.btn_8_one);
		btn_9_one = (Button) mView_one.findViewById(R.id.btn_9_one);
		btn_0_one = (Button) mView_one.findViewById(R.id.btn_0_one);
		btn_delete_one = (Button) mView_one.findViewById(R.id.btn_delete_one);
		btn_sure_one = (Button) mView_one.findViewById(R.id.btn_sure_one);
		
		
		btn_outcard_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行退卡逻辑
				serialPortOne.sendBuffer(fi.outcard());
			}
		});
		btn_100_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额100逻辑
				param = "100";
				tv_param_one.setText("100");
			}
		});
		btn_200_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额200逻辑
				tv_param_one.setText("200");
			}
		});
		btn_500_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额500逻辑
				tv_param_one.setText("500");
			}
		});
		btn_1000_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额1000逻辑
				tv_param_one.setText("1000");
			}
		});
		btn_cancel_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行取消逻辑
				tv_param_one.setText("");
			}
		});
		btn_confirm_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行确认逻辑
				param = tv_param_one.getText().toString();
				String type = tv_danwei_one.getText().toString();
				if(type.equals("元")){
					serialPortOne.sendBuffer(fi.MsgMoney(param));
				}else if(type.equals("升")){
					serialPortOne.sendBuffer(fi.MsgLitre(param));
				}
				
			}
		});
		btn_1_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"1";
					tv_param_one.setText(param);
				}
			}
		});
		btn_2_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"2";
					tv_param_one.setText(param);
				}
			}
		});
		btn_3_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"3";
					tv_param_one.setText(param);
				}
				
			}
		});
		btn_4_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"4";
					tv_param_one.setText(param);
				}
			}
		});
		btn_5_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"5";
					tv_param_one.setText(param);
				}
			}
		});
		btn_6_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"6";
					tv_param_one.setText(param);
				}
			}
		});
		btn_7_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"7";
					tv_param_one.setText(param);
				}
			}
		});
		btn_8_one.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"8";
					tv_param_one.setText(param);
				}
			}
		});
		btn_9_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<4){
					param = param+"9";
					tv_param_one.setText(param);
				}
			}
		});
		btn_0_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(!param.trim().equals("")){
					if(param.length()<4){
						param = param+"0";
						tv_param_one.setText(param);
					}
				}
			}
		});
		
		btn_delete_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(!param.trim().equals("")){
					param = param.substring(0, param.length()-1);
					tv_param_one.setText(param);
				}
			}
		});
		btn_sure_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				serialPortOne.sendBuffer(fi.MsgMoney(param));
			}
		});
		iv_setmoney_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_kindoftext_one.setText("金额");
						tv_danwei_one.setText("元");
					}
				});
			}
		});
		iv_setfill_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.d("点击事件", "iv_setfill_one");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_kindoftext_one.setText("升数");
						tv_danwei_one.setText("升");
					}
				});
			}
		});
		iv_full_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行加满逻辑
				serialPortOne.sendBuffer(fi.setFull());
			}
		});
	}
	
	private void two_initPageThree(){
		final FillInfo fi = new FillInfo();
		tv_param_two = (TextView) mView_two.findViewById(R.id.tv_param_two);
		iv_setmoney_two = (ImageView) mView_two.findViewById(R.id.iv_setmoney_two);
		iv_setfill_two = (ImageView) mView_two.findViewById(R.id.iv_setfill_two);
		iv_full_two = (ImageView) mView_two.findViewById(R.id.iv_full_two);
		
		tv_kindoftext_two = (TextView) mView_two.findViewById(R.id.tv_kindoftext_two);
		tv_danwei_two = (TextView) mView_two.findViewById(R.id.tv_danwei_two);
		
		
		btn_outcard_two = (Button) mView_two.findViewById(R.id.btn_cardout_two);
		btn_100_two = (Button) mView_two.findViewById(R.id.btn_100_two);
		btn_200_two = (Button) mView_two.findViewById(R.id.btn_200_two);
		btn_500_two = (Button) mView_two.findViewById(R.id.btn_500_two);
		btn_1000_two = (Button) mView_two.findViewById(R.id.btn_1000_two);
		btn_cancel_two = (Button) mView_two.findViewById(R.id.btn_cancel_two);
		btn_confirm_two = (Button) mView_two.findViewById(R.id.btn_confirm_two);
		btn_1_two = (Button) mView_two.findViewById(R.id.btn_1_two);
		btn_2_two = (Button) mView_two.findViewById(R.id.btn_2_two);
		btn_3_two = (Button) mView_two.findViewById(R.id.btn_3_two);
		btn_4_two = (Button) mView_two.findViewById(R.id.btn_4_two);
		btn_5_two = (Button) mView_two.findViewById(R.id.btn_5_two);
		btn_6_two = (Button) mView_two.findViewById(R.id.btn_6_two);
		btn_7_two = (Button) mView_two.findViewById(R.id.btn_7_two);
		btn_8_two = (Button) mView_two.findViewById(R.id.btn_8_two);
		btn_9_two = (Button) mView_two.findViewById(R.id.btn_9_two);
		btn_0_two = (Button) mView_two.findViewById(R.id.btn_0_two);
		btn_delete_two = (Button) mView_two.findViewById(R.id.btn_delete_two);
		btn_sure_two = (Button) mView_two.findViewById(R.id.btn_sure_two);
		
		
		btn_outcard_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行退卡逻辑
				serialPortTwo.sendBuffer(fi.outcard());
			}
		});
		btn_100_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额100逻辑
				param = "100";
				tv_param_two.setText("100");
			}
		});
		btn_200_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额200逻辑
				tv_param_two.setText("200");
			}
		});
		btn_500_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额500逻辑
				tv_param_two.setText("500");
			}
		});
		btn_1000_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行定金额1000逻辑
				tv_param_two.setText("1000");
			}
		});
		btn_cancel_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行取消逻辑
				tv_param_two.setText("");
			}
		});
		btn_confirm_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行确认逻辑
				param = tv_param_two.getText().toString();
				String type = tv_danwei_two.getText().toString();
				if(type.equals("元")){
					serialPortTwo.sendBuffer(fi.MsgMoney(param));
				}else if(type.equals("升")){
					serialPortTwo.sendBuffer(fi.MsgLitre(param));
				}
				
			}
		});
		btn_1_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"1";
					tv_param_two.setText(param);
				}
			}
		});
		btn_2_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"2";
					tv_param_two.setText(param);
				}
			}
		});
		btn_3_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"3";
					tv_param_two.setText(param);
				}
				
			}
		});
		btn_4_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"4";
					tv_param_two.setText(param);
				}
			}
		});
		btn_5_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"5";
					tv_param_two.setText(param);
				}
			}
		});
		btn_6_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"6";
					tv_param_two.setText(param);
				}
			}
		});
		btn_7_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"7";
					tv_param_two.setText(param);
				}
			}
		});
		btn_8_two.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"8";
					tv_param_two.setText(param);
				}
			}
		});
		btn_9_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(param.length()<4){
					param = param+"9";
					tv_param_two.setText(param);
				}
			}
		});
		btn_0_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(!param.trim().equals("")){
					if(param.length()<4){
						param = param+"0";
						tv_param_two.setText(param);
					}
				}
			}
		});
		
		btn_delete_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				if(!param.trim().equals("")){
					param = param.substring(0, param.length()-1);
					tv_param_two.setText(param);
				}
			}
		});
		btn_sure_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_two.getText().toString();
				serialPortTwo.sendBuffer(fi.MsgMoney(param));
			}
		});
		iv_setmoney_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_kindoftext_two.setText("金额");
						tv_danwei_two.setText("元");
					}
				});
			}
		});
		iv_setfill_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.d("点击事件", "iv_setfill_two");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_kindoftext_two.setText("升数");
						tv_danwei_two.setText("升");
					}
				});
			}
		});
		iv_full_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//执行加满逻辑
				serialPortTwo.sendBuffer(fi.setFull());
			}
		});
	}
	
	public class MessageReceiver extends BroadcastReceiver {  
		  
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {  
				String message = intent.getStringExtra("message");
				if(message.equals("true")){
					Toast.makeText(context, "U盘已插入", Toast.LENGTH_SHORT).show();
					Intent toUpdate = new Intent(MainActivity.this, UpdateActivity.class);
					startActivity(toUpdate);
//					stopAd();
//					
//					filename = Util.doSearchVideo(TARGET_PATH);
//					playVideo(filename);
				}else{
					Toast.makeText(context, "U盘已移除", Toast.LENGTH_SHORT).show();
				}				 
	        } 
		}  
    }
	
	/** 
     * 动态注册广播 
     */  
    public void registerMessageReceiver() {  
        mMessageReceiver = new MessageReceiver();  
        IntentFilter filter = new IntentFilter();  
  
        filter.addAction(ACTION_INTENT_RECEIVER);  
        registerReceiver(mMessageReceiver, filter);  
    } 
    
    public void playVideo(final List<String> filename){
    	video_position = 0;
    	File file=new File(ParamStatic.VIDEO_PATH + "/" + filename.get(video_position));
    	String path = file.getAbsolutePath();
        vv_ad.setVideoPath(path);
        vv_ad.getHolder().setFixedSize(1080, 600);
        vv_ad.start();
        vv_ad.setOnPreparedListener(new OnPreparedListener() {
          
          @Override
          public void onPrepared(MediaPlayer arg0) {
            // TODO Auto-generated method stub
            arg0.setLooping(false);
            
          }
        });
        
        vv_ad.setOnCompletionListener(new OnCompletionListener() {
        	int length = filename.size();
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				video_position++;
				if(video_position>=length){
					video_position = 0;
					File vedio=new File(ParamStatic.VIDEO_PATH + "/" + filename.get(video_position));
					vv_ad.setVideoPath(vedio.getAbsolutePath());
					vv_ad.getHolder().setFixedSize(1080, 600);
			        vv_ad.start();
				}else{
					File vedio=new File(ParamStatic.VIDEO_PATH + "/" + filename.get(video_position));
					vv_ad.setVideoPath(vedio.getAbsolutePath());
					vv_ad.getHolder().setFixedSize(1080, 600);
			        vv_ad.start();
				}
			}
		});
    }
}
