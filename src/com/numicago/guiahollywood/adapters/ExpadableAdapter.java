package com.numicago.guiahollywood.adapters;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.numicago.guiahollywood.DayMovieHelper;
import com.numicago.guiahollywood.ImageLoader;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.FilmeInfoActivity;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Day;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.widgets.BadgeView;


public class ExpadableAdapter extends BaseExpandableListAdapter {

	Activity activity;
	
	List<Movie> listaFilmes;
	
	private static LayoutInflater inflaterParent = null;
	private static LayoutInflater inflaterChild = null;
	
	private ImageLoader imageLoader; 
	
//	private static final int droidGreen = Color.parseColor("#A4C639");
	private static final int droidBlue = Color.parseColor("#157DEC");
	
	public ExpadableAdapter(Activity estatisticaActivity, List<Movie> listaFilmes) {
		activity = estatisticaActivity;
		this.listaFilmes = listaFilmes; 
		inflaterParent = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflaterChild = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public Object getChild(int groupPosition, int childPosition) {
		List<Day> listDatas = DBUtils.getMovieDays(activity, 
				listaFilmes.get(groupPosition).getId());
		return listDatas.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		Movie movie = listaFilmes.get(groupPosition);
		
		View vi = convertView;
        if(convertView==null)
            vi = inflaterChild.inflate(R.layout.datas_filme_layout, null);
        
        List<Day> listDatas = DBUtils.getMovieDays(activity, movie.getId());
        
        ImageView image = (ImageView) vi.findViewById(R.id.datas_filmes_background_image);
        if (listDatas.get(childPosition).getStart().getDate().before(new Date())) {
        	image.setImageResource(R.drawable.date_bar_backgroud_past);
        } else {
        	image.setImageResource(R.drawable.date_bar_backgroud);
        }
        
        ImageView alarm = (ImageView) vi.findViewById(R.id.datas_filme_layout_alarm_image);
        if (listDatas.get(childPosition).hasAlarm()) {
        	alarm.setVisibility(View.VISIBLE);
        } else {
        	alarm.setVisibility(View.GONE);
        }
        
        final TextView textView = (TextView) vi.findViewById(R.id.datas_filme_data_textView);
		textView.setText(listDatas.get(childPosition).getStart().toString("EEEE, yyyy/MM/dd, 'às' HH:mm"));
		
		ImageButton addAlarm = (ImageButton) vi.findViewById(R.id.addAlarmDatasFilme);
		addAlarm.setOnClickListener(new OnClickListener() {
			
			public void onClick(View paramView) {
				Toast.makeText(activity, textView.getText().toString(), Toast.LENGTH_LONG).show();
			}
		});
		
		return vi;
	}

	public int getChildrenCount(int groupPosition) {
		return listaFilmes.get(groupPosition).getCount();
	}

	public Object getGroup(int groupPosition) {
		return listaFilmes.get(groupPosition);
	}

	public int getGroupCount() {
		return listaFilmes.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		
		View vi = convertView;
		
        if(vi == null) {
            vi = inflaterParent.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.horaTextView);
            holder.badge = new BadgeView(activity, holder.text);
            holder.badge.setBadgeBackgroundColor(droidBlue);
            holder.badge.setTextColor(Color.BLACK);
            holder.badge.setTextSize(10);
            vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag();        	
        }
        
        holder.text.setText("");
        holder.badge.setText("Contagem: " + listaFilmes.get(groupPosition).getCount());
        holder.badge.show();

        ImageView info = (ImageView) vi.findViewById(R.id.info_image_view);
        info.setVisibility(View.VISIBLE);
        
        info.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(activity, FilmeInfoActivity.class);
				DayMovie dm = new DayMovie();
				dm.setMovie(listaFilmes.get(groupPosition));
				List<Day> listDatas = DBUtils.getMovieDays(activity, dm.getMovie().getId());
				dm.setDay(listDatas.get(0));
				DayMovieHelper.dayMovieAsBundle = dm;
				activity.startActivity(i);
			}
		});
        
        TextView NomeOriginaltext=(TextView)vi.findViewById(R.id.titulo_original_text_view);;
        NomeOriginaltext.setText(listaFilmes.get(groupPosition).getOriginalName());
        
        TextView NomePTtext=(TextView)vi.findViewById(R.id.titulo_pt_text_view);
        NomePTtext.setText("" + listaFilmes.get(groupPosition).getLocalName()); 
       
//        TextView count = (TextView)vi.findViewById(R.id.horaTextView);
//        count.setText("(ID: " + listaFilmes.get(groupPosition).getId() + ") " 
//        	+ "Contagem: " + listaFilmes.get(groupPosition).getCount());
        
        int ano = listaFilmes.get(groupPosition).getYear();
        TextView anoTV =(TextView)vi.findViewById(R.id.generoTextView);
        anoTV.setText("Ano: " + (ano != 0 ? ano : "N/A"));
        
        ImageView onAirIcon = (ImageView)vi.findViewById(R.id.onAir_IV);
        onAirIcon.setVisibility(View.GONE);

        boolean net = GuiaHollywoodActivity.userPreferences.isConnected() 
				&& (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION 
				|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
					&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));
        
        ImageView imageView = (ImageView)vi.findViewById(R.id.imageView);
        imageLoader.DisplayImage(listaFilmes.get(groupPosition).getImageSmallUrl(), activity, imageView, net,
        		"" + listaFilmes.get(groupPosition).getLocalName());
        
		return vi;
	}

//	private View drawView(int position, View view) {
//		ViewHolder holder = null;
//
//		int viewType = getItemViewType(position);
//		switch (viewType) {
//		case all:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.all_list_view, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		case favs:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.favs_list_view, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		case vistos:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.vistos_list_view, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		case watchlist:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.watch_list_view, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		case ratings:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.ratings_list_view, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		case yearsProgress:
//			if(view == null) {
//				view = mInflater.inflate(R.layout.years_progress_scroller, null);
//
//				holder = new ViewHolder();
//
//				holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
//				holder.mContent = view;
//
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//			break;
//		}
//
//
//		final String o = getItem(position);
//		if (o != null) {
//			holder.mProgressBar.setVisibility(View.GONE);
//			holder.mContent.setVisibility(View.VISIBLE);
//		}
//		else {
//			new LoadContentTask().execute(position, view);
//
//			holder.mContent.setVisibility(View.GONE);
//			holder.mProgressBar.setVisibility(View.VISIBLE);
//		}
//
//		return view;
//	}
//	
//	private class LoadContentTask extends AsyncTask<Object, Object, Object> {
//
//		private Integer position;
//		private View view;
//
//		@Override
//		protected Object doInBackground(Object... arg) {
//			position = (Integer) arg[0];
//			view = (View) arg[1];
//
//			// long-term task is here 			
//			try {
//				Thread.sleep(3000); // do nothing for 3000 miliseconds (3 second)
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			return getTitle(position);
//		}
//
//		protected void onPostExecute(Object result) {
//			// process result    	 
//			content[position] = (String) result;
//
//			drawView(position, view);
//
//			view.postInvalidate();
//		}
//	}
	
	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	static class ViewHolder {
        TextView text;
        BadgeView badge;
    }
}