package com.numicago.guiahollywood.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.numicago.guiahollywood.DrawableManager;
import com.numicago.guiahollywood.R;

public class MovieAlertActivity extends Activity
{
	MediaPlayer mMediaPlayer = new MediaPlayer();
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_alert);

		final Bundle bundle = getIntent().getExtras();
		String nomeFilme = bundle.getString("filme");
		String imageURL = bundle.getString("imagem");
		playAlarm();
		
		TextView movieName = (TextView)findViewById(R.id.movieNameAlertText);
		movieName.setText("Não perca já a seguir no Canal Hollywood o filme\n" + nomeFilme);
		
		ImageView movieImage = (ImageView)findViewById(R.id.movieImageAlert);
		DrawableManager dm = new DrawableManager();
		dm.fetchDrawableOnThread(imageURL, movieImage, true);
		
		
		Button cancelAlarmButton = (Button)findViewById(R.id.alarm_cancel_button);
		cancelAlarmButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				mMediaPlayer.stop();
				bundle.clear();
				finish();
			}
		});
	}
	public void playAlarm()
	{
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); 
		try {
			mMediaPlayer.setDataSource(this, alert);
		} catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} catch (IllegalStateException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) 
		{
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mMediaPlayer.setLooping(true);
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			mMediaPlayer.start();
		}
	}
}
