package com.example.petrochina.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomView extends VideoView{
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CustomView(Context context) {
        super(context);
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		 int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	     int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	     int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	     int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	     if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY)
	     {
	         setMeasuredDimension(widthSize,heightSize);
	     }
	     else
	     {
	         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	     }
	}
}
