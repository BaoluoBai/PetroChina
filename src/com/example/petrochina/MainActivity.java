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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	private static final String PORT_ONE = "/dev/ttymxc1";
	private static final String PORT_TWO = "/dev/ttymxc2";
	private static final int BAUDRATE = 9600;
	
	int i = 0;
	int j = 0;
	
	
	private RelativeLayout mRelaytiveLayout_one, mRelaytiveLayout_two;
	private View mView_one, mView_two;
	private ImageView iv_oilcard_one, iv_visacard_one, iv_cash_one, iv_mobilepay_one,
	iv_oilcard_two, iv_visacard_two, iv_cash_two, iv_mobilepay_two, iv_setmoney_one,
	iv_setfill_one, iv_full_one;
	
	private TextView tv_password_one, tv_tips_one, tv_lastmoney_one, tv_kindoftext_one,
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
		iv_setmoney_one = (ImageView) mView_one.findViewById(R.id.iv_setmoney_one);
		iv_setfill_one = (ImageView) mView_one.findViewById(R.id.iv_setfill_one);
		iv_full_one = (ImageView) mView_one.findViewById(R.id.iv_full_one);
		
		tv_kindoftext_one = (TextView) mView_one.findViewById(R.id.tv_kindoftext_one);
		tv_danwei_one = (TextView) mView_one.findViewById(R.id.tv_danwei_one);
		
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
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_lastmoney_one.setText("卡余额:"+money);
			}
		});
	}
}
