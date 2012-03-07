package com.badideastudios.mrassassin;

import android.content.Context;
import android.util.AttributeSet;

import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Canvas;
import android.view.View;

public class RadarView extends View
{
	private float direction = 0;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean initialPoint;
	
	//Initializer for paint drawing; needed in order to draw.
	private void initialize()
	{
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(7);
		paint.setColor(Color.RED);
		paint.setTextSize(30);
		
		initialPoint = true;
	}

	//Constructors
	public RadarView(Context context)
	{
			super(context);
			initialize();
	}
	
	public RadarView(Context context, AttributeSet attr)
	{
			super(context, attr);
			initialize();
	}
	
	public RadarView(Context context, AttributeSet attr, int style)
	{
		super(context, attr, style);
		initialize();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) 
	{
		int cxCompass = getMeasuredWidth()/2;
		int cyCompass = getMeasuredHeight()/2;
		float radiusCompass;
	
		if(cxCompass > cyCompass)
		{
			radiusCompass = (float) (cyCompass * 0.95);
		}
		else
		{
			radiusCompass = (float) (cxCompass * 0.95);
		}
		//canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paint);
	
		if(!initialPoint)
		{
			canvas.drawLine(cxCompass, cyCompass,
						    (float)(cxCompass + radiusCompass * Math.sin((double)(-direction) * 3.14/180)),
						    (float)(cyCompass - radiusCompass * Math.cos((double)(-direction) * 3.14/180)),
						    paint);
		}
	}

	public void updateDirection(float dir)
	{
		initialPoint = false;
		direction = dir;
		invalidate();
	}
	
}
