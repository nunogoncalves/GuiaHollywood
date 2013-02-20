package com.numicago.guiahollywood.activities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.numicago.guiahollywood.adapters.ImageBulkAdapter;

public class ImageBulkActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String root_sd = Environment.getExternalStorageDirectory().toString();
	    File file = new File( root_sd + "/CanalHollywood" ) ;       
	    File list[] = file.listFiles();

	    final List<File> fileList = Arrays.asList(list);
	    float size = 0;
	    for (File file2 : fileList) {
	    	size += file2.length();
		}
	    String sizeString = "";
	    if (size > 1024 && size < (1024 * 1024)) {
	    	double sizeFinal = size / 1024;
	    	String sizeFinalString = ("" + sizeFinal);
	    	sizeFinalString = sizeFinalString.substring(0, sizeFinalString.indexOf(".") + 3);
	    	sizeString = "Tamanho total: " + sizeFinalString + "KB";
	    } else if (size < 1024) {
	    	sizeString = "Tamanho total: " + size + " Bytes";	    	
	    } else if (size > (1024 * 1024)) {
	    	double sizeFinal = size / (1024 * 1024);
	    	String sizeFinalString = ("" + sizeFinal);
	    	sizeFinalString = sizeFinalString.substring(0, sizeFinalString.indexOf(".") + 3);
	    	sizeString = "Tamanho total: " + sizeFinalString + "MB";	    	
	    }
	    TextView tv = new TextView(this);
	    tv.setText("Número de imagens: " + fileList.size() + "\n" + sizeString);
	    
	    GridView gv = new GridView(this);
	    gv.setColumnWidth(100);
	    gv.setNumColumns(GridView.AUTO_FIT);
	    gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gv.setAdapter(new ImageBulkAdapter(this, fileList));
		gv.setFastScrollEnabled(true);
		
		LinearLayout ll = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(params);
		ll.addView(tv);
		ll.addView(gv);
		setContentView(ll);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(ImageBulkActivity.this, fileList.get(arg2).getName(), Toast.LENGTH_LONG).show();
			}
		});
	}
}
