package com.numicago.guiahollywood.adapters;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageBulkAdapter extends BaseAdapter {

	private List<File> listFiles;
	private Activity activity;
	
	public ImageBulkAdapter(Activity context, List<File> listFiles) {
		this.listFiles = listFiles;
		this.activity = context;
	}
	
	public int getCount() {
		return listFiles.size();
	}

	public Object getItem(int position) {
		return listFiles.get(position);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parentpos) {
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(listFiles.get(position).getAbsolutePath());
        imageView.setImageBitmap(bitmap);
        return imageView;
	}

}
