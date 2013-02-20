package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.adapters.ActorsListToMovieAdapter;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.widgets.IndexableListView;

public class AddActorsToMovieActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actors_to_movie_list);
		
		final int movieId = getIntent().getExtras().getInt("movieId");
		
		List<Actor> allActors = DBUtils.getAllActors(this, 
				GuiaHollywoodActivity.userPreferences.getActorSortId());
		
		IndexableListView lv = (IndexableListView) findViewById(R.id.add_actors_list_to_movie_indexable_view);
		lv.setFastScrollEnabled(true);
		
		final ActorsListToMovieAdapter adapter = new ActorsListToMovieAdapter(AddActorsToMovieActivity.this, allActors, movieId);
		lv.setAdapter(adapter);
		
		Button saveButton = (Button) findViewById(R.id.actor_to_movie_save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Updating movie actors");
				DBUtils.updateMovieActors(AddActorsToMovieActivity.this, adapter.getSelectedActorsIds(), movieId);
				System.out.println("Getting all movie actors");
				DBUtils.getMovieActors(AddActorsToMovieActivity.this, movieId);
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.actor_to_movie_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
