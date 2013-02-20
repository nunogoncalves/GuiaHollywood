package com.numicago.guiahollywood.activities;

import com.numicago.guiahollywood.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		final Activity thisActivity = SplashActivity.this;

		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 10) 
					{
						sleep(1);
						waited += 1;
					}
				} 
				catch (InterruptedException e)  {
					// do nothing
				}
				finally {
					finish();
					Intent i = new Intent(thisActivity, GuiaHollywoodActivity.class);
					startActivity(i);
				}
			}
		};
		splashThread.start();
	}
}
