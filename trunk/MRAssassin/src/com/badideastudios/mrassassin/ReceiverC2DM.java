package com.badideastudios.mrassassin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ReceiverC2DM extends BroadcastReceiver {

	private static String KEY = "c2dmPref";
	private static String REGISTRATION_KEY = "registrationKey";
	
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Message receieved");
		this.context = context;
		if(intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION"))
		{
			handleRegistration(context, intent);
		}else if(intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE"))
		{
			handleMessage(context, intent);
		}		
	}
	
	private void handleRegistration(Context context, Intent intent)
	{
		String registration = intent.getStringExtra("registration_id");
		if(intent.getStringExtra("eror") != null)
		{
			Log.d("c2dm", "registration failed");
			String error = intent.getStringExtra("error");
			if(error == "SERVICE_NOT_AVAILABLE"){
		    	Log.d("c2dm", "SERVICE_NOT_AVAILABLE");
		    }else if(error == "ACCOUNT_MISSING"){
		    	Log.d("c2dm", "ACCOUNT_MISSING");
		    }else if(error == "AUTHENTICATION_FAILED"){
		    	Log.d("c2dm", "AUTHENTICATION_FAILED");
		    }else if(error == "TOO_MANY_REGISTRATIONS"){
		    	Log.d("c2dm", "TOO_MANY_REGISTRATIONS");
		    }else if(error == "INVALID_SENDER"){
		    	Log.d("c2dm", "INVALID_SENDER");
		    }else if(error == "PHONE_REGISTRATION_ERROR"){
		    	Log.d("c2dm", "PHONE_REGISTRATION_ERROR");
		    }
		}else if(intent.getStringExtra("unregistered") != null)
		{
			Log.d("c2dm", "unregistered");
		}else if(registration != null)
		{
			Log.d("c2dm", registration);
			Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
			editor.putString(REGISTRATION_KEY, registration);
			editor.commit();
		}
	}
	
	private void handleMessage(Context context, Intent intent)
	{
		System.out.println("Message handled");
		String message = intent.getExtras().getString("payload");
		if(message == "killed")
		{
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.dagger;
			CharSequence tickerText = "DEAD";
			long when = System.currentTimeMillis();
			
			Notification notification = new Notification(icon, tickerText, when);
			
			Context context2 = context.getApplicationContext();
			CharSequence contentTitle = "MRAssassins";
			CharSequence contentText = "You have been killed by a pursuer!";
			Intent notificationIntent = new Intent(context, ReceiverC2DM.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			
			final int KILL_ID = 1;
			nManager.notify(KILL_ID, notification);			
			
		}
		Log.e("", "message: " + message);
		// Handle the message here
	}
}
