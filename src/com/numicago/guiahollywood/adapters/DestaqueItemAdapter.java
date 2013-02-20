package com.numicago.guiahollywood.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.numicago.guiahollywood.ImageLoader;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.activities.DestaqueInfoActivity;
import com.numicago.guiahollywood.objects.Destaque;

public class DestaqueItemAdapter extends BaseAdapter
{
	LayoutInflater layoutInflater;
	Activity activity;
	List<Destaque> destaquesList;
	ImageLoader imageLoader; 
	
	public DestaqueItemAdapter(Activity activity, List<Destaque> destaquesList)
	{
		this.activity = activity;
		this.destaquesList = destaquesList;
		layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
	}
	
	public int getCount() 
	{
		return destaquesList.size();
	}

	public Object getItem(int arg0)
	{
		return destaquesList.get(arg0);
	}

	public long getItemId(int arg0) 
	{
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View vi = convertView;
        if(convertView == null)
            vi = layoutInflater.inflate(R.layout.list_destaque, null);
        
        TextView tituloTV = (TextView)vi.findViewById(R.id.item_destaque_title);
        tituloTV.setText(destaquesList.get(position).getTitulo());
        
        ImageView imageBar = (ImageView)vi.findViewById(R.id.item_destaque_bar);
        imageBar.setMinimumHeight(tituloTV.getHeight());
        
        TextView descricaoTV = (TextView)vi.findViewById(R.id.item_destaque_descricao);
        descricaoTV.setText(destaquesList.get(position).getDescricao());
        
        ImageView image = (ImageView)vi.findViewById(R.id.item_destaque_image);
        imageLoader.DisplayImage(destaquesList.get(position).getImageURL(), activity, image, true,
        		tituloTV.getText().toString());
        
        ImageButton button = (ImageButton)vi.findViewById(R.id.item_destaque_more_button);
        final int pos = position;
        button.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Bundle bundle = new Bundle();
				bundle.putString(Utils.DESTAQUE_TITULO, destaquesList.get(pos).getTitulo());
				bundle.putString(Utils.DESTAQUE_DESCRICAO, destaquesList.get(pos).getDescricao());
				bundle.putString(Utils.DESTAQUE_IMAGEM, destaquesList.get(pos).getImageURL());
				
				Intent i = new Intent(activity, DestaqueInfoActivity.class);
				i.putExtras(bundle);
				activity.startActivity(i);
//				Toast.makeText(activity, destaquesList.get(pos).getVerMaisLink(), Toast.LENGTH_LONG).show();
			}
		});
		return vi;
	}
	
}
