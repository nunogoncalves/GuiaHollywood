package com.numicago.guiahollywood.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.numicago.guiahollywood.DrawableManager;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.DayMovie;

public class FilmeInfoFromAlert extends Activity
{
//	private RelativeLayout relLayoutProgress;
//	private TextView filmeProgressTV;
//	private ProgressBar pbMovieNow;
	private TextView nomeOriginalTV;
	private TextView descricaoTv;
	private TextView duracaoTv;
	private TextView realizadorTv;
	private TextView horarioTv;
	private ImageView filmeImagem;
//	private String genero;

	private ProgressBar pb;
	private DayMovie movie;

	//		private String TAG = "Filme Info Activity";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filme_info);
		
		pb = (ProgressBar) findViewById(R.id.progress_bar_big_image);
		pb.setVisibility(View.GONE);
		Bundle bundle = getIntent().getExtras();
		
		movie = new DayMovie();
		movie.getMovie().setLocalName(bundle.getString("pt"));
		movie.getMovie().setImageBigUrl(bundle.getString("imagem"));
		movie.getMovie().setOriginalName(bundle.getString("original"));
		movie.getMovie().setDescription(bundle.getString("descricao"));
		movie.getMovie().setDirector(bundle.getString("realizador"));
		movie.getMovie().setYear(bundle.getInt("year"));
		NumiCal calInit = new NumiCal();
		calInit.setTimeInMillis(bundle.getLong("init"));
		NumiCal calEnd = new NumiCal();
		calEnd.setTimeInMillis(bundle.getLong("fim"));
		movie.getDay().setStart(calInit);
		movie.getDay().setEnd(calEnd);

		TextView agendaFilmeTv = (TextView)findViewById(R.id.agenda_filme_textView);
		agendaFilmeTv.setText(getString(R.string.das) + " " + movie.getDay().getStart().toString("kk:mm") 
				+ " " + getString(R.string.as) + " " + movie.getDay().getEnd().toString("kk:mm"));
		nomeOriginalTV = (TextView)findViewById(R.id.filme_info_titulo_original_textView);
		nomeOriginalTV.setText(movie.getMovie().getOriginalName());
		TextView nomePtTV = (TextView)findViewById(R.id.filme_info_titulo_en_textView);
		nomePtTV.setText(movie.getMovie().getLocalName());
		realizadorTv = (TextView)findViewById(R.id.filme_realizador_textView);
		realizadorTv.setText(getString(R.string.realizador) + movie.getMovie().getDirector());
		TextView anoTv = (TextView)findViewById(R.id.filme_ano_textView);
		anoTv.setText("Ano: " + movie.getMovie().getYear());
		
		duracaoTv = (TextView)findViewById(R.id.filme_duracao_textView);
		duracaoTv.setText(getString(R.string.duracao) + movie.getDurationInMinutes() + " min");
		descricaoTv = (TextView)findViewById(R.id.filme_info_descricao_textView);
		if(movie.getMovie().getDescription() != null && movie.getMovie().getDescription().length() != 0) {
			descricaoTv.setText(movie.getMovie().getDescription());
		} else {
			descricaoTv.setText("Não há descrição possível. Provavelmente guardou o filme sem " +
					"internet.");
		}
		horarioTv = (TextView)findViewById(R.id.filme_horario_textView);
		horarioTv.setText(bundle.getString("horario"));
		if(movie.getMovie().getImageBigUrl() != null) {
			filmeImagem = (ImageView)findViewById(R.id.imagem_filme);
			filmeImagem.setScaleType(ScaleType.FIT_XY);
			DrawableManager dm = new DrawableManager();
			dm.fetchDrawableOnThread(movie.getMovie().getImageBigUrl(), filmeImagem,
					GuiaHollywoodActivity.userPreferences.isConnectedAndImageLoad());
		}
	}
}
