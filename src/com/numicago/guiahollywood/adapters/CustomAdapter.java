package com.numicago.guiahollywood.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.numicago.guiahollywood.ImageLoader;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;

public class CustomAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    private ImageLoader imageLoader; 
    private Activity activity;
    private List<DayMovie> listaFilmes;
    private TextView moviePercentage;
    private int viewBackground;
    
	public CustomAdapter(Activity activity, List<DayMovie> listaFilmes, int viewBackground)
	{
		this.listaFilmes = listaFilmes;
		this.viewBackground = viewBackground;
		this.activity = activity;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
	}
	public int getCount() 
	{
		return listaFilmes.size();
	}

	public Object getItem(int position) 
	{
		return position;
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View vi=convertView;
        if(convertView==null)
        	if(GuiaHollywoodActivity.userPreferences.getTheme() == 4) {
        		vi = inflater.inflate(R.layout.list_item_white, null);
    		} else {
  				vi = inflater.inflate(R.layout.list_item, null);
  				LinearLayout ll = (LinearLayout)vi.findViewById(R.id.ll_itembase);
  				ll.setBackgroundResource(com.numicago.guiahollywood.objects.EnumBackground.getResourceFromId(viewBackground));
    		}

        
        DayMovie movie = listaFilmes.get(position);
        
        TextView nomeOriginaltext=(TextView)vi.findViewById(R.id.titulo_original_text_view);
        if (GuiaHollywoodActivity.userPreferences.getTheme() == 1) {
        	nomeOriginaltext.setTextColor(Color.WHITE);
        }
        nomeOriginaltext.setText(movie.getMovie().getOriginalName());
        nomeOriginaltext.setSelected(true);
        
        TextView NomePTtext=(TextView)vi.findViewById(R.id.titulo_pt_text_view);
        NomePTtext.setText(movie.getMovie().getLocalName()); 
        NomePTtext.setSelected(true);
        
        TextView horario=(TextView)vi.findViewById(R.id.horaTextView);
        horario.setText(movie.getDay().getStart().toString("HH:mm") + " - " + movie.getDay().getEnd().toString("HH:mm"));
        
        TextView genero=(TextView)vi.findViewById(R.id.generoTextView);
        genero.setText(movie.getMovie().getGenre());
        
        ImageView image = (ImageView)vi.findViewById(R.id.imageView);
        byte[] blob = movie.getMovie().getImageSmallBlob();
        
        boolean net = GuiaHollywoodActivity.userPreferences.isConnected() &&
        		(GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION
        		|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
        		&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));
        
        if(blob == null) {
        	imageLoader.DisplayImage(movie.getMovie().getImageSmallUrl(), activity, image, net,
        			movie.getMovie().getLocalName());
        } else {
        	Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
        	image.setImageBitmap(bmp);
        }

        ImageView alarmIcon = (ImageView)vi.findViewById(R.id.alarmBell);
        ImageView onAirIcon = (ImageView)vi.findViewById(R.id.onAir_IV);
        ImageView favouriteIcon = (ImageView)vi.findViewById(R.id.list_item_favorite_icon);
        ImageView viewdIcon = (ImageView)vi.findViewById(R.id.list_item_viewed);
        ImageView watchList = (ImageView)vi.findViewById(R.id.list_item_watch_list);
        
        //Se o filme estiver na lista de alarmes então vai ter imagem do alarme //nome é generico suf para o objecto
        alarmIcon.setImageResource(movie.getDay().hasAlarm() ? R.drawable.alarm_bell : 0);
        favouriteIcon.setImageResource(movie.getMovie().isFavourite() ? R.drawable.heart : 0);
        viewdIcon.setImageResource(movie.getMovie().isViewed() ? R.drawable.viewed : 0);
        watchList.setImageResource(movie.getMovie().isWatchList() ? R.drawable.watch : 0);

        RatingBar rating = (RatingBar) vi.findViewById(R.id.list_white_rating);
        if (movie.getMovie().getRating() != null) {
        	rating.setRating(movie.getMovie().getRating().floatValue());
        }
        
        FrameLayout rl = (FrameLayout)vi.findViewById(R.id.list_item_progress_relative_layout);
        
        //Verificar se o filme está no ar neste momento
        if(movie.isOnAir())
        {
        	moviePercentage = (TextView)vi.findViewById(R.id.list_item_percentage_movie);
        	onAirIcon.setVisibility(View.VISIBLE);        	
        	rl.setVisibility(View.VISIBLE);
        	ProgressBar pb = (ProgressBar)vi.findViewById(R.id.list_item_movie_progree_bar);
        	pb.setMax((int)movie.getDurationInMillis());
        	pb.setProgress((int)movie.getMoviePassed());
        	moviePercentage.setText("" + movie.getElapsedPercentage());
        }
        else 
        {
        	onAirIcon.setVisibility(View.GONE);
        	rl.setVisibility(View.GONE);
        }
        
        return vi;
	}
}
