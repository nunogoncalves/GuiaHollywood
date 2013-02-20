package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.adapters.ActorListAdapter;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.provider.DBUtils;

public class ActorsListActivity extends ExpandableListActivity {

	List<Actor> allActors;
	
	ExpandableListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actors_list);
		
		allActors = DBUtils.getAllActors(this, 
				GuiaHollywoodActivity.userPreferences.getActorSortId());
		
		lv = getExpandableListView();
		lv.setAdapter(new ActorListAdapter(this, allActors));
		registerForContextMenu(lv);
		lv.setFastScrollEnabled(true);
		lv.setGroupIndicator(null);
		
		Button addNew = (Button) findViewById(R.id.actors_list_add_actor_button);
		addNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addActorI = new Intent(getBaseContext(), AddActorActivity.class);
				startActivity(addActorI);
			}
		});
	}
	
	/**
	 * Long click menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		if (v.getId() == android.R.id.list) {
			menu.add(0, 0, 0, "Editar");
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = 
			    (ExpandableListContextMenuInfo) item.getMenuInfo();
		
		int menuItemIndex = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		System.out.println("packed  " + info.packedPosition);
		System.out.println("menu item index " + menuItemIndex);
		
		Actor actor = allActors.get(menuItemIndex);
			Intent allActorsI = new Intent(ActorsListActivity.this, AddActorActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("actorId", actor.getId());
			allActorsI.putExtras(bundle);
			startActivity(allActorsI);
			
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		allActors = DBUtils.getAllActors(this,  
				GuiaHollywoodActivity.userPreferences.getActorSortId());
		if (lv.getAdapter() instanceof ActorListAdapter) {
			((ActorListAdapter) lv.getAdapter()).notifyDataSetChanged ();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_actors_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actors_sort_order:
			Intent UIIntent = new Intent(getBaseContext(), ActorPreferencesActivity.class);
			startActivity(UIIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
