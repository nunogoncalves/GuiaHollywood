package com.numicago.guiahollywood.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.numicago.guiahollywood.R;

public class SobreActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final Activity thisActivity = SobreActivity.this;
		setContentView(R.layout.sobre_xml);
		
		WebView wb = (WebView)findViewById(R.id.sobre_autor_web_view);
		
		wb.setBackgroundColor(0x000000);
		String string = getString(R.string.sobre_autor).toString();
		string = "<div align=\"justify\"><font color = \"black\">" + string +  "</font></div>";
		
		wb.loadData(string, "text/html", "utf-8");
		
		final TextView sendEmail = (TextView)findViewById(R.id.sobre_email);
		sendEmail.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
				sendIntent.setType("text/plain");
				sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
				sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Guia Hollywood");
				thisActivity.startActivity(sendIntent);
			}
		});
	}
}
