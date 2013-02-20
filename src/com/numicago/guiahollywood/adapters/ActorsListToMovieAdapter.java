package com.numicago.guiahollywood.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.numicago.guiahollywood.ImageLoader;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;
import com.numicago.guiahollywood.provider.DBUtils;

public class ActorsListToMovieAdapter extends BaseAdapter implements SectionIndexer {
    private static LayoutInflater inflater = null;
    private ImageLoader imageLoader; 
    private Activity activity;
    private List<Actor> actorList;
    
    private List<Integer> selectedActors;
    
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
	public ActorsListToMovieAdapter(Activity activity, List<Actor> listaFilmes, final int movieId)
	{
		this.actorList = listaFilmes;
		this.activity = activity;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        selectedActors = new ArrayList<Integer>();
        List<Actor> movieActors = DBUtils.getMovieActors(activity, movieId);
        for (Actor actor : movieActors) {
			selectedActors.add(actor.getId());
		}
	}
	public int getCount() 
	{
		return actorList.size();
	}

	public String getItem(int position) {
		return actorList.get(position).getName();
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View vi = convertView;
        if(vi == null) {
        	vi = inflater.inflate(R.layout.actor_selection_list_item, null);
        }
        
        final Actor actor = actorList.get(position);
        
        TextView nome = (TextView)vi.findViewById(R.id.actor_to_movie_list_item_name);
       	nome.setTextColor(Color.WHITE);
       	
        nome.setText(actor.getId() + " - " + actor.getName());
        nome.setSelected(true);
        
        TextView bday =(TextView)vi.findViewById(R.id.actor_to_movie_list_item_bday_date);
        bday.setTextColor(Color.WHITE);
        NumiCal bdate = new NumiCal(actor.getBday());
        bday.setText(bdate.toString("yyyy-MM-dd")
        		+ " (" + NumiCal.calculateAge(bdate.getDate()) + " anos)");
        
        ImageView image = (ImageView)vi.findViewById(R.id.actor_to_movie_list_item_image);
        boolean net = GuiaHollywoodActivity.userPreferences.isConnected() &&
        		(GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION
        		|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
        		&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));

        if (net && actor.getBigImageUrl() != null && actor.getBigImageUrl().length() != 0) {
        	imageLoader.DisplayImage(actor.getBigImageUrl(), activity, image, net,
        			actor.getName());
        }
        
        final CheckBox selectedCB = (CheckBox) vi.findViewById(R.id.actor_to_movie_selected_check_box);
        selectedCB.setChecked(selectedActors.contains(actor.getId()));
        
        selectedCB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int actorId = actor.getId();
				if (selectedCB.isChecked()) {
					if (!selectedActors.contains(actorId)) {
						selectedActors.add(actorId);
					}
				} else {
					if (selectedActors.contains(actorId)) {
						selectedActors.remove((Object) actorId);
					}
				}
			}
		});
        
        return vi;
	}
	
	public List<Integer> getSelectedActorsIds() {
		return selectedActors;
	}
	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
}
