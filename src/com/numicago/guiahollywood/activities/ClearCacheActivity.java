package com.numicago.guiahollywood.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.numicago.guiahollywood.FileCache;
import com.numicago.guiahollywood.R;

public class ClearCacheActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.clear_cache);
		
		Button button = (Button) findViewById(R.id.view_cached_images);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ClearCacheActivity.this, ImageBulkActivity.class);
			    startActivity(i);
			}
		});
		
		ImageButton clear = (ImageButton) findViewById(R.id.clearCacheButton);
		clear.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ClearCacheActivity.this);
				builder.setMessage("Tem a certeza de que quer apagar a cache?");
				builder.setCancelable(false); 
				builder.setPositiveButton(ClearCacheActivity.this.getString(R.string.sim), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						FileCache fCache = new FileCache(ClearCacheActivity.this);
			    		fCache.clear();
			    		Toast.makeText(ClearCacheActivity.this, getString(R.string.cache_apagada), Toast.LENGTH_LONG).show();
				    }
				 });
				builder.setNegativeButton(ClearCacheActivity.this.getString(R.string.nao),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) 
					{
						
					}
				});
				
				AlertDialog alert = builder.create();
				alert.setTitle("Confirme a acção");
				alert.show();
			}
		});
	}
}
