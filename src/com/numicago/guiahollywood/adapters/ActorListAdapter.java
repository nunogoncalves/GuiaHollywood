package com.numicago.guiahollywood.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.numicago.guiahollywood.ImageLoader;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;
import com.numicago.guiahollywood.provider.DBUtils;

public class ActorListAdapter extends BaseExpandableListAdapter
{
    private ImageLoader imageLoader; 
    private Activity activity;
    private List<Actor> actorList;
    private static LayoutInflater inflaterParent = null;
	private static LayoutInflater inflaterChild = null;
    
	public ActorListAdapter(Activity activity, List<Actor> listaFilmes)
	{
		this.actorList = listaFilmes;
		this.activity = activity;
    	inflaterParent = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	inflaterChild = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		List<Movie> actorMovies = DBUtils.getActorMovies(activity, 
				actorList.get(groupPosition).getId());
		return actorMovies.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		Actor actor = actorList.get(groupPosition);
		
		View vi = convertView;
        if(convertView == null)
            vi = inflaterChild.inflate(R.layout.actor_movie_list_item, null);
        
        List<Movie> listMovies = DBUtils.getActorMovies(activity, actor.getId());
        
        TextView originalView = (TextView) vi.findViewById(R.id.actor_movie_original_name);
        originalView.setText(listMovies.get(childPosition).getOriginalName());
        
        TextView localName = (TextView) vi.findViewById(R.id.actor_movie_local_name);
        localName.setText(listMovies.get(childPosition).getLocalName());
        
        ImageView image = (ImageView) vi.findViewById(R.id.actor_movie_image);
        boolean net = GuiaHollywoodActivity.userPreferences.isConnected() 
				&& (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION 
				|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
					&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));
        
        imageLoader.DisplayImage(listMovies.get(childPosition).getImageSmallUrl(), activity, image, net,
        		"" + listMovies.get(childPosition).getLocalName());
        
		return vi;
	}

	public int getChildrenCount(int groupPosition) {
		return DBUtils.getActorMovies(activity, actorList.get(groupPosition).getId()).size();
//				actorList.get(groupPosition).getCount();
	}

	public Object getGroup(int groupPosition) {
		return actorList.get(groupPosition);
	}

	public int getGroupCount() {
		return actorList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View vi = convertView;
		if(vi == null) {
			vi = inflaterParent.inflate(R.layout.actor_list_item, null);
		}

		Actor actor = actorList.get(groupPosition);

		TextView nome = (TextView)vi.findViewById(R.id.actor_list_item_name);
		nome.setTextColor(Color.WHITE);

		nome.setText(actor.getId() + " - " + actor.getName());
		nome.setSelected(true);

		TextView bday =(TextView)vi.findViewById(R.id.actor_list_item_bday_date);
		bday.setTextColor(Color.WHITE);
		NumiCal bdate = new NumiCal(actor.getBday());
		bday.setText(bdate.toString("yyyy-MM-dd")
				+ " (" + NumiCal.calculateAge(bdate.getDate()) + " anos)");

		ImageView image = (ImageView)vi.findViewById(R.id.actor_list_item_image);
		boolean net = GuiaHollywoodActivity.userPreferences.isConnected() &&
				(GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION
				|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
				&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));

		if (net && actor.getBigImageUrl() != null && actor.getBigImageUrl().length() != 0) {
			imageLoader.DisplayImage(actor.getBigImageUrl(), activity, image, net,
					actor.getName());
		}

		return vi;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
