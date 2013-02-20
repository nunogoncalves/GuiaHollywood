package com.numicago.guiahollywood.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.numicago.guiahollywood.R;

public class AlarmService extends Service {


	public int onStartCommand (Intent intent, int flags, int startId) {
		
		super.onStartCommand(intent, flags, startId);
		Bundle bundle = intent.getExtras();

		Intent i = new Intent(getApplicationContext(), FilmeInfoActivity.class);
		i.putExtras(bundle);

		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
		notification.contentView = contentView;

		notification.icon = com.numicago.guiahollywood.R.drawable.hollywood_icon;
		//			notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		notification.setLatestEventInfo(this, "Alertas Canal Hollywood",
				bundle.getString("pt")+ " já a seguir!", pi);
		notification.defaults |= Notification.DEFAULT_SOUND;
		startForeground(9, notification);

		return 0;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.d("service", "onStart");
	}

	@Override
	public void onDestroy() {
		Log.d("destroy", "onDestroy");
	}
}
