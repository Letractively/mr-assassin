package com.badideastudios.mrassassin;

import android.content.Context;
import android.util.AttributeSet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.hardware.GeomagneticField;
import android.location.Location;

import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;


public class RadarView extends View
{
	private float direction = 0;
	private float distance = 1000;
	private RectF arc;
	Display ourDisplay;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean initialPoint;
	
	//Initializer for paint drawing; needed in order to draw.
	private void initialize()
	{
		paint.setStrokeWidth(8);
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
	    	break;
		case Surface.ROTATION_180:
	    	canvas.rotate(180, cxCompass, cyCompass);
	    	break;
		case Surface.ROTATION_270:
	    	canvas.rotate(90, cxCompass, cyCompass);
	    	break;
		}
		if(!initialPoint)
		{
			double sliceSize = 300*Math.exp(-distance/40);
			if(sliceSize > 5)
			{
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawLine(cxCompass, cyCompass,
			    (float)(cxCompass + radiusCompass * Math.sin(direction)),
			    (float)(cyCompass - radiusCompass * Math.cos(direction)),
			    paint);
			}
			else
			{
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				arc = new RectF(cxCompass - radiusCompass, cyCompass - radiusCompass,
		  						cxCompass + radiusCompass, cyCompass + radiusCompass);
				canvas.drawArc(arc, (float)(Math.toDegrees(direction) - sliceSize/2), (float)sliceSize, true, paint);
			}
		}
		
	     super.onDraw(canvas);
	     canvas.restore();
	}

	public void update(float[] directionMatrix, Location ourLoc, Location targetLoc, GeomagneticField geo)
	{
		initialPoint = false;
		float heading = directionMatrix[0];
		float bearing = ourLoc.bearingTo(targetLoc);
		distance = ourLoc.distanceTo(targetLoc);
		heading += geo.getDeclination();
		direction = (float) Math.toRadians(bearing - heading);
		invalidate();
	}
	
}
