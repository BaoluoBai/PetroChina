package com.example.petrochina;


import com.example.petrochina.util.DataHexUtil;
import com.example.petrochina.util.LogUtil;
import com.example.petrochina.util.SerialPortUtil;
import com.example.petrochina.util.SerialPortUtil.OnDataReceiveListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	private static final String PORT_ONE = "/dev/ttymxc1";
	private static final String PORT_TWO = "/dev/ttymxc2";
	private static final int BAUDRATE = 9600;
	
	private ImageView iv_oilcard_one, iv_visacard_one, iv_cash_one, iv_mobilepay_one,
	iv_oilcard_two, iv_visacard_two, iv_cash_two, iv_mobilepay_two;
	
	public SerialPortUtil serialPortOne = null;
	public SerialPortUtil serialPortTwo = null;
	public DataHexUtil dataUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        serialPortOne = new SerialPortUtil(PORT_ONE, BAUDRATE);
        dataUtil = new DataHexUtil();
        initView();
        serialPortOne.setOnDataReceiveListener(new OnDataReceiveListener() {
			
			@Override
			public void onDataReceive(byte[] buffer, int size) {
				// TODO Auto-generated method stub
				switch (buffer[2]) {
				case 0x20:
					dataUtil.handleOilData(buffer);
					break;

				default:
					break;
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initView(){
    	iv_oilcard_one = (ImageView) findViewById(R.id.tv_oilcard_one);
    	iv_visacard_one = (ImageView) findViewById(R.id.tv_visacard_one);
    	iv_cash_one = (ImageView) findViewById(R.id.tv_cash_one);
    	iv_mobilepay_one = (ImageView) findViewById(R.id.tv_mobilepay_one);
    	iv_oilcard_two = (ImageView) findViewById(R.id.tv_oilcard_two);
    	iv_visacard_two = (ImageView) findViewById(R.id.tv_visacard_two);
    	iv_cash_two = (ImageView) findViewById(R.id.tv_cash_two);
    	iv_mobilepay_two = (ImageView) findViewById(R.id.tv_mobilepay_two);
    	iv_oilcard_one.setOnClickListener(this);
    	iv_visacard_one.setOnClickListener(this);
    	iv_cash_one.setOnClickListener(this);
    	iv_mobilepay_one.setOnClickListener(this);
    	iv_oilcard_two.setOnClickListener(this);
    	iv_visacard_two.setOnClickListener(this);
    	iv_cash_two.setOnClickListener(this);
    	iv_mobilepay_two.setOnClickListener(this);
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_oilcard_one:
			
			break;
		case R.id.tv_visacard_one:
			
			break;
		case R.id.tv_cash_one:
			
			break;
		case R.id.tv_mobilepay_one:
			
			break;
		case R.id.tv_oilcard_two:
			
			break;
		case R.id.tv_visacard_two:
			
			break;
		case R.id.tv_cash_two:
			
			break;
		case R.id.tv_mobilepay_two:
			
			break;

		default:
			break;
		}
	}
}
