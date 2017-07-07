package com.example.petrochina;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.petrochina.model.Payment;
import com.example.petrochina.util.DataHexUtil;
import com.example.petrochina.util.LogUtil;
import com.example.petrochina.util.SerialPortUtil;
import com.example.petrochina.util.SerialPortUtil.OnDataReceiveListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	private static final String PORT_ONE = "/dev/ttymxc1";
	private static final String PORT_TWO = "/dev/ttymxc2";
	private static final int BAUDRATE = 9600;
	
	public String param = "";
	
	int i = 0;
	int j = 0;
	
	
	private RelativeLayout mRelaytiveLayout_one, mRelaytiveLayout_two;
	private View mView_one, mView_two;
	private ImageView iv_oilcard_one, iv_visacard_one, iv_cash_one, iv_mobilepay_one,
	iv_oilcard_two, iv_visacard_two, iv_cash_two, iv_mobilepay_two, iv_setmoney_one,
	iv_setfill_one, iv_full_one;
	
	private Button btn_outcard_one, btn_100_one, btn_200_one, btn_500_one, btn_1000_one,
	btn_cancel_one, btn_confirm_one, btn_1_one, btn_2_one, btn_3_one, btn_4_one, btn_5_one,
	btn_6_one, btn_7_one, btn_8_one, btn_9_one, btn_0_one, btn_delete_one, btn_sure_one;
	
	private TextView tv_param_one, tv_password_one, tv_tips_one, tv_lastmoney_one, tv_kindoftext_one,
	tv_danwei_one;
	
	public SerialPortUtil serialPortOne = null;
	public SerialPortUtil serialPortTwo = null;
	public DataHexUtil dataUtil = null;
	public Payment payment;

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
						handle_02_view(map);	
					}else if(view.equals("3")){
						//处理预置量输入页面逻辑
						handle_03_view(map);
					}
					break;
				case 0x20:
					dataUtil.handleOilData(buffer);
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
    }
    
    private void initView(){
    	mRelaytiveLayout_one = (RelativeLayout) findViewById(R.id.view_number_one);
    	mRelaytiveLayout_two = (RelativeLayout) findViewById(R.id.view_number_two);

//    	iv_oilcard_two = (ImageView) findViewById(R.id.tv_oilcard_two);
//    	iv_visacard_two = (ImageView) findViewById(R.id.tv_visacard_two);
//    	iv_cash_two = (ImageView) findViewById(R.id.tv_cash_two);
//    	iv_mobilepay_two = (ImageView) findViewById(R.id.tv_mobilepay_two);
//    	iv_oilcard_one.setOnClickListener(this);
//    	iv_visacard_one.setOnClickListener(this);
//    	iv_cash_one.setOnClickListener(this);
//    	iv_mobilepay_one.setOnClickListener(this);
//    	iv_oilcard_two.setOnClickListener(this);
//    	iv_visacard_two.setOnClickListener(this);
//    	iv_cash_two.setOnClickListener(this);
//    	iv_mobilepay_two.setOnClickListener(this);
    	display_one(3);
    	display_two(3);
//    	Timer tb = new Timer();
//    	tb.schedule(ts, 2000, 5000);
//    	
//    	Timer ss = new Timer();
//    	ss.schedule(tt, 1000, 7000);
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_oilcard_one:
			serialPortOne.sendBuffer(payment.MsgPayment(1));
			break;
		case R.id.tv_visacard_one:
			serialPortOne.sendBuffer(payment.MsgPayment(2));
			break;
		case R.id.tv_cash_one:
			serialPortOne.sendBuffer(payment.MsgPayment(0));
			break;
		case R.id.tv_mobilepay_one:
			serialPortOne.sendBuffer(payment.MsgPayment(3));
			break;
		case R.id.tv_oilcard_two:
			serialPortTwo.sendBuffer(payment.MsgPayment(1));
			break;
		case R.id.tv_visacard_two:
			serialPortTwo.sendBuffer(payment.MsgPayment(2));
			break;
		case R.id.tv_cash_two:
			serialPortTwo.sendBuffer(payment.MsgPayment(0));
			break;
		case R.id.tv_mobilepay_two:
			serialPortTwo.sendBuffer(payment.MsgPayment(3));
			break;

		default:
			break;
		}
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
			break;
		case 4://请提枪加油
			mView_one = View.inflate(this, R.layout.one_pickgun_view_04, null);
			break;
		case 5:
			mView_one = View.inflate(this, R.layout.one_confirmoilagain_view_05, null);
			break;
		case 6:
			mView_one = View.inflate(this, R.layout.one_fillingup_view_06, null);
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
				i++;
		    	mRelaytiveLayout_one.removeAllViews();
		    	mRelaytiveLayout_one.addView(mView_one);
		    	if(i>6){
		    		i=0;
		    	}
			}
		});
    }
    
    public void display_two(int view_number){
    	switch (view_number) {
		case 0://支付方式
			mView_two = View.inflate(this, R.layout.two_payment_view_01, null);
			break;
		case 1://请插卡
			mView_two = View.inflate(this, R.layout.two_insertcard_view_30, null);
			break;
		case 2://请输入密码
			mView_two = View.inflate(this, R.layout.two_password_view_02, null);
			break;
		case 3://预制量输入
			mView_two = View.inflate(this, R.layout.two_setting_view_03, null);
			break;
		case 4://请提枪加油
			mView_two = View.inflate(this, R.layout.two_pickgun_view_04, null);
			break;
		case 5://再次确认油品
			mView_two = View.inflate(this, R.layout.two_confirmoilagain_view_05, null);
			break;
		case 6://加油中
			mView_two = View.inflate(this, R.layout.two_fillingup_view_06, null);
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
				j++;
				mRelaytiveLayout_two.removeAllViews();
		    	mRelaytiveLayout_two.addView(mView_two);
		    	if(j>6){
		    		j=0;
		    	}
			}
		});
    }
    
    TimerTask ts = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			switch (i) {
			case 0:
				display_one(R.layout.one_payment_view_01);
				break;
			case 1:
				display_one(R.layout.one_password_view_02);
				break;
			case 2:
				display_one(R.layout.one_pickgun_view_04);
				break;
			case 3:
				display_one(R.layout.one_confirmoilagain_view_05);
				break;
			case 4:
				display_one(R.layout.one_fillingup_view_06);
				break;
			case 5:
				display_one(R.layout.one_fillfinish_view_07);
				break;
			case 6:
				display_one(R.layout.one_payinfo_view_08);
				break;

			default:
				break;
			}

		}
		
	};
	
	TimerTask tt = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			switch (j) {
			case 0:
				display_two(R.layout.two_payment_view_01);
				break;
			case 1:
				display_two(R.layout.two_password_view_02);
				break;
			case 2:
				display_two(R.layout.two_pickgun_view_04);
				break;
			case 3:
				display_two(R.layout.two_confirmoilagain_view_05);
				break;
			case 4:
				display_two(R.layout.two_fillingup_view_06);
				break;
			case 5:
				display_two(R.layout.two_fillfinish_view_07);
				break;
			case 6:
				display_two(R.layout.two_payinfo_view_08);
				break;

			default:
				break;
			}

		}
		
	};
	//处理输入密码页面逻辑
	private void handle_02_view(Map<String, String> map){
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
	
	//处理预置量输入页面逻辑
	private void handle_03_view(Map<String, String> map){
		display_one(3);
		final String money = map.get("money");
		tv_lastmoney_one = (TextView) mView_one.findViewById(R.id.tv_lastmoney_one);
		initPageThree();
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_lastmoney_one.setText("卡余额:"+money);
			}
		});
	}
	
	private void initPageThree(){
		
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
			}
		});
		btn_1_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				param = tv_param_one.getText().toString();
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
				if(param.length()<=4){
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
					if(param.length()<=4){
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
			}
		});
	}
}
