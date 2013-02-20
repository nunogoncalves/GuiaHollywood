package com.numicago.guiahollywood.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.FilmeInfoFromAlert;

public class MovieAlarm extends BroadcastReceiver { 
	
	    // this constructor is called by the alarm manager.
	    public MovieAlarm(){ }

	    // you can use this constructor to create the alarm. 
	    //  Just pass in the main activity as the context, 
	    //  any extras you'd like to get later when triggered 
	    //  and the timeout
	     public MovieAlarm(Context context, Bundle extras, NumiCal calendar){
	    	 AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	         Intent intent = new Intent(context, MovieAlarm.class);
	    	 intent.setData(Uri.parse("timer:" + calendar.getTimeInMillis()));
	         intent.putExtras(extras);
	         PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 
	             PendingIntent.FLAG_UPDATE_CURRENT);
	         alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
	                      pendingIntent);
	     }

	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		
		Intent i = new Intent(context.getApplicationContext(), FilmeInfoFromAlert.class);
		i.putExtras(bundle);

		PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 
				0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();

		RemoteViews contentView = new RemoteViews(context.getPackageName(), 
				R.layout.notification_layout);
		notification.contentView = contentView;

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nm = (NotificationManager) context.getSystemService(ns);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notification.icon = R.drawable.hollywood_icon;
		notification.setLatestEventInfo(context, "Alertas Canal Hollywood",
				bundle.getString("pt")+ " já a seguir!", pi);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		nm.notify(1, notification);
	}
}