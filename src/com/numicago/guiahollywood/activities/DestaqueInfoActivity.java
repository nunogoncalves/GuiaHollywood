package com.numicago.guiahollywood.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.numicago.guiahollywood.DrawableManager;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.objects.Destaque;

public class DestaqueInfoActivity extends Activity
{
	private ProgressBar pb;
	private ImageView image;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destaque_info);
		
		pb = (ProgressBar)findViewById(R.id.destaque_info_progress_bar_big_image);
		
		Destaque destaque = new Destaque();
		
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if(bundle != null)
		{
			destaque.setTitulo(bundle.getString(Utils.DESTAQUE_TITULO));
			destaque.setDescricao(bundle.getString(Utils.DESTAQUE_DESCRICAO));
			destaque.setImageURL(bundle.getString(Utils.DESTAQUE_IMAGEM));
			
			TextView tituloTV = (TextView)findViewById(R.id.destaque_info_title);
			tituloTV.setText(destaque.getTitulo());

			TextView descricaoTV = (TextView)findViewById(R.id.destaque_info_descricao);
			descricaoTV.setText(destaque.getDescricao());
			
			image = (ImageView)findViewById(R.id.destaque_info_image);
			new parseSite().execute(destaque.getImageURL());
		}
	}
	
	/**
	 * Metodo que faz o parsing to site no background e guarda a informação do a mostrar
	 * @author Para
	 *
	 */
	private class parseSite extends AsyncTask<String, Void, List<String>> 
	{
		protected List<String> doInBackground(String... arg) 
		{
			List<String> filmInfo = new ArrayList<String>();
			filmInfo.add(arg[0]);
			return filmInfo;
		}

		protected void onPostExecute(List<String> output) 
		{
			DrawableManager dm = new DrawableManager();
			dm.fetchDrawableOnThread(output.get(0), image, true);
			pb.setVisibility(View.GONE);
		}
	}
}
