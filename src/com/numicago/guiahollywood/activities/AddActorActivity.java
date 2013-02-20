package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.provider.DBUtils;

public class AddActorActivity extends Activity {

	private EditText nameET;
	private EditText imdbET;
	private EditText imageUrlET;
	private DatePicker bdayPicker;
	private CheckBox favCb;
	
	private Integer actorId = null;
	private boolean update = false;
	
	private static final String DROP_BOX_BASE = "https://dl.dropbox.com/u/2001692/actor/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_actor);
		
		Bundle bundle = getIntent().getExtras();
		Actor actor = null;
		if (bundle != null) {
			actorId = bundle.getInt("actorId");
			actor = DBUtils.getActorFullInfo(AddActorActivity.this, actorId);
		}
		
		
		nameET = (EditText) findViewById(R.id.add_actor_actor_name_et);
		imdbET = (EditText) findViewById(R.id.add_actor_imdb_link_et);
		imdbET.setText("http://www.imdb.com/name/nm");
		imageUrlET = (EditText) findViewById(R.id.add_actor_image_url_et);
		imageUrlET.setText(DROP_BOX_BASE);
		bdayPicker = (DatePicker) findViewById(R.id.add_actor_bdate_picker);
		favCb = (CheckBox) findViewById(R.id.add_actor_favourite_cb);
		
		if (actor != null) {
			update = true;
			nameET.setText(actor.getName());
			imdbET.setText(actor.getImdbUrl());
			imageUrlET.setText(actor.getBigImageUrl());
			NumiCal cal = new NumiCal(actor.getBday());
			bdayPicker.updateDate(cal.getYear(), cal.getMonth(), cal.getDayOfMonth());			
		}
		
		Button saveButton = (Button) findViewById(R.id.add_actor_guardar_novo_button);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Actor actor = new Actor();
				actor.setName(nameET.getText().toString());
				if (imdbET.getText().toString().contains("imdb.com")) {
					actor.setImdbUrl(imdbET.getText().toString());
				} else {
					actor.setImdbUrl("http://www.imdb.com/name/" + imdbET.getText().toString() + "/");
				}
				actor.setBday(new NumiCal(bdayPicker.getYear(), bdayPicker.getMonth(), 
						bdayPicker.getDayOfMonth()).getDate());
				actor.setBigImageUrl(DROP_BOX_BASE + getActorDropBoxImageUrl());
				if (update) {
					actor.setId(actorId);
					DBUtils.updateActor(AddActorActivity.this, actor);
				} else {
					DBUtils.addNewActor(AddActorActivity.this, actor);
				}
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.add_actor_cancelar_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button viewAllButton = (Button) findViewById(R.id.add_actor_ver_todos_button);
		viewAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent allActorsI = new Intent(AddActorActivity.this, ActorsListActivity.class);
				startActivity(allActorsI);
			}
		});
	}
	private String getActorDropBoxImageUrl() {
		return Utils.UppercaseFirstLetters(nameET.getText().toString()).replaceAll("\\s", "") + ".jpg";
	}	
}
