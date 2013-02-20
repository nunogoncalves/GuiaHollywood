package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;

public abstract class AlertUtils {

	public static void onBackPressed(final Activity context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.certeza_sair));
		builder.setCancelable(false); 
		builder.setPositiveButton(context.getString(R.string.sim), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
		    {
				context.finish();
		    }
		 });
		builder.setNegativeButton(context.getString(R.string.nao), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
			}
		});		
		AlertDialog alert = builder.create();
		alert.setTitle(context.getString(R.string.sair)+"?");
		alert.show();
	}


	/**
	 * Escolher entre utilizar internet movel ou sair da aplicação
	 * @param site
	 * @param context
	 */
	public static void alertPlanoDados(final Activity context, 
			final NumiCal calendar)
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.data_plan));
		builder.setCancelable(false); 
		builder.setPositiveButton(context.getString(R.string.sim), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
		    {
				List<DayMovie> listaFilmesDB = DBUtils.getMoviesOfDay(context, calendar,
						GuiaHollywoodActivity.userPreferences.getDisplayChannel()); 
				//hoje e a 
				if (calendar.isToday() && listaFilmesDB.size() > 0) {
				((GuiaHollywoodActivity)context).dataPlanSelected(
						EnumUseType.USE_DATA_PLAN_DB_MOVIES);
		    } else {
					((GuiaHollywoodActivity)context).dataPlanSelected(
							EnumUseType.USE_DATA_PLAN_DB_EMPTY);
				}
		    }
		 });
		builder.setNegativeButton(context.getString(R.string.nao),
				new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				//Se não tiver filmes guardados do dia, então não carrega nada.
				if(!DBUtils.moviesOfDayExist(context, calendar, GuiaHollywoodActivity.userPreferences.getDisplayChannel())) {
					((GuiaHollywoodActivity)context).dataPlanSelected(
							EnumUseType.NO_USE_DATA_PLAN_DB_EMPTY);
				} else { //Se tiver filmes, carrega-os.
					((GuiaHollywoodActivity)context).dataPlanSelected(
							EnumUseType.NO_USE_DATA_PLAN_DB_MOVIES);
				}
			}
		});
		
		AlertDialog alert = builder.create();
		alert.setTitle(context.getString(R.string.usar_plano_dados));
		alert.show();
    }
	
	/**
	 * Se o utilizador não tiver internet aparece um diálogo a avisar
	 * @param context
	 */
	public static void alertNoInternet(final Context context)
    {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.no_net_try_again)); 
		builder.setCancelable(false);
		builder.setPositiveButton(context.getString(R.string.sair), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				((GuiaHollywoodActivity)context).finish();
			}
		});
		builder.setNegativeButton(context.getString(R.string.try_again), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				Bundle bundle = new Bundle();
				bundle.putBoolean(Utils.REINICIO_STR, true);
				((GuiaHollywoodActivity)context).onCreate(null);
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.setTitle(context.getString(R.string.no_connection));
		alert.show();
    }
	
	public static void createRatingAlert(final Activity context, Float ratVal, final Movie movie) {
		final Dialog dialog = new Dialog(context);

		dialog.setContentView(R.layout.add_rating);
		dialog.setTitle("Rating: ");

		final RatingBar rating = (RatingBar) dialog.findViewById(R.id.add_rating_rating);
		if (ratVal != null) {
			rating.setRating(ratVal.floatValue());
		}
		
		Button okbutton = (Button) dialog.findViewById(R.id.add_rating_gravar);
		okbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.hide();
				((GuiaHollywoodActivity) context).resetMovieRating(movie, rating.getRating());
			}
		});
		Button cancelbutton = (Button) dialog.findViewById(R.id.add_rating_cancelar);
		cancelbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
		
	}
	
	public enum EnumUseType {
		NO_USE_DATA_PLAN_DB_MOVIES,
		
		NO_USE_DATA_PLAN_DB_EMPTY,
		
		USE_DATA_PLAN_DB_MOVIES,
		
		USE_DATA_PLAN_DB_EMPTY;
	}
}
