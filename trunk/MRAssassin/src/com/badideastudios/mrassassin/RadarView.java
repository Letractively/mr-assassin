package com.badideastudios.mrassassin;

import android.content.Context;
import android.util.AttributeSet;

import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Canvas;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;


public class RadarView extends View
{
	private float direction = 0;
	Display ourDisplay;
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
			ourDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			initialize();
	}
	
	public RadarView(Context context, AttributeSet attr)
	{
			super(context, attr);
			ourDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			initialize();
	}
	
	public RadarView(Context context, AttributeSet attr, int style)
	{
		super(context, attr, style);
		ourDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
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
	    canvas.save();
		/** Set compass dimensions*/
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
		
		/*Rotate the compass with the screen to fix errors on landscape*/
		switch(ourDisplay.getRotation())
		{
		case Surface.ROTATION_90:
	    	canvas.rotate(270, cxCompass, cyCompass);
		case Surface.ROTATION_180:
	    	canvas.rotate(180, cxCompass, cyCompass);
		case Surface.ROTATION_270:
	    	canvas.rotate(90, cxCompass, cyCompass);
		}
	
		if(!initialPoint)
		{
			canvas.drawLine(cxCompass, cyCompass,
						    (float)(cxCompass + radiusCompass * Math.sin((double)(-direction) * 3.14/180)),
						    (float)(cyCompass - radiusCompass * Math.cos((double)(-direction) * 3.14/180)),
						    paint);
		}
		
	     super.onDraw(canvas);
	     canvas.restore();
	}

	public void updateDirection(float[] directionMatrix)
	{
		initialPoint = false;
		direction = directionMatrix[0];
		invalidate();
	}
	
}
