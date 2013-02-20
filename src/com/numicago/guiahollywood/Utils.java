package com.numicago.guiahollywood;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Class de métodos estáticos utilitários em várias situações na aplicação
 * @author Para
 */
public class Utils 
{
	public static final int NO_INTERNET_CONNECTION = -1;
	public static final int WIFI_CONNECTION = 1;
	public static final int MOBILE_CONNECTION = 0;
	public static final String SELECTED_DATE = "selectedDate";

	public static final String SITE_BASE = "siteBase";
	public static final String SITE_GUIA_BASE = "siteGuiaBase";

	public static final String REINICIO_STR = "reinicio";

	//	public static final String SITE_DATE_FORMAT_PT = "yyyy-MM-dd";
	//	public static final String SITE_DATE_FORMAT_ES = "yyyy/MM/dd";

	public static final String DESTAQUE_TITULO = "destaqueTitulo";
	public static final String DESTAQUE_IMAGEM = "destaqueImagem";
	public static final String DESTAQUE_DESCRICAO = "destaqueDescricao";
	public static final String DESTAQUE_IMAGE_BLOB = "destaqueImageBlob";

	public static final String SEASON_PREVAILS_KEY = "seasonPrevailsKey";
	public static final String DATABASE_VERSION_STRING = "databaseVersion";

	public static final String TIME_BEFORE = "timeBefore";

	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	/**
	 * Colocar a primeira letra de cada palavra maíuscula
	 * @param str
	 * @return
	 */
	 public static String UppercaseFirstLetters(String str) 
	 {
		 boolean prevWasWhiteSp = true;
		 char[] chars = str.toCharArray();
		 for (int i = 0; i < chars.length; i++) {
			 if (Character.isLetter(chars[i])) {
				 if (prevWasWhiteSp) {
					 chars[i] = Character.toUpperCase(chars[i]);    
				 }
				 prevWasWhiteSp = false;
			 } else {
				 prevWasWhiteSp = Character.isWhitespace(chars[i]) || Character.isDefined('-');
			 }
		 }
		 return new String(chars);
	 }

	 /**
	  * Verificar o estado da internet antes de ir ao site
	  * @param ConnectivityManager
	  * @return
	  */
	 public static boolean checkInternetConnection(ConnectivityManager cm) 
	 {
		 // test for connection
		 if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) 
			 return true;
		 else
			 return false;
	 }

	 /**
	  * Retorna o tipo de Internet que a ligação tem
	  * 0 -> Trafego de dados
	  * 1 -> WIFI
	  * -1 -> Não ligado
	  * @param cm
	  * @return
	  */
	 public static int checkInternetConnectionType(ConnectivityManager cm)
	 {
		 // test for connection
		 //cm.getActiveNetworkInfo().getType() - 0 - Mobile - 1 WIFI
		 if(cm.getActiveNetworkInfo() != null)
		 {
			 if (cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())
			 {
				 if(cm.getActiveNetworkInfo().getType() == WIFI_CONNECTION)
					 return WIFI_CONNECTION;
				 else if (cm.getActiveNetworkInfo().getType() == MOBILE_CONNECTION)
					 return MOBILE_CONNECTION;
				 else
					 return NO_INTERNET_CONNECTION;
			 }
			 else return NO_INTERNET_CONNECTION;
		 }
		 else
			 return NO_INTERNET_CONNECTION;
	 }

	 /**
	  * Retornar um Bitmap com imagem da net através do URL
	  * @param aURL
	  * @return
	  */
	 public static Bitmap getRemoteImage(final URL aURL) {
		 try {
			 final URLConnection conn = aURL.openConnection();
			 conn.setConnectTimeout(600);
			 conn.connect();
			 Log.d("UTILS", aURL.toString());
			 final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			 final Bitmap bm = BitmapFactory.decodeStream(bis);
			 bis.close();
			 return bm;
		 } catch (IOException e) {
			 Log.d("DEBUGTAG", "Oh noooz an error in tha image...");
		 }
		 return null;
	 }


	 /**
	  * Descarregar imagem da internet para ByteArrayBuffer
	  * @param imageLink
	  * @return
	  */
	 public static ByteArrayBuffer getImageBlob(String imageLink)
	 {
		 //where we want to download it from
		 URL url;
		 InputStream is = null;
		 try {
			 url = new URL(imageLink);
			 URLConnection ucon;
			 try 
			 {
				 ucon = url.openConnection();
				 //buffer the download
				 try 
				 {
					 is = ucon.getInputStream();
				 } catch (IOException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
			 catch (IOException e) 
			 {
				 e.printStackTrace();
			 }
		 }
		 catch (MalformedURLException e1) 
		 {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 }  //http://example.com/image.jpg
		 //open the connection

		 BufferedInputStream bis = new BufferedInputStream(is,128);
		 ByteArrayBuffer baf = new ByteArrayBuffer(128);
		 //get the bytes one by one
		 int current = 0;
		 try {
			 while ((current = bis.read()) != -1) 
			 {
				 baf.append((byte) current);
			 }
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 return baf;
	 }

	 static int[] numbers = new int[] {
			 R.drawable.n_zero, //1
			 R.drawable.n_one, //1
			 R.drawable.n_two, //2
			 R.drawable.n_three, //3
			 R.drawable.n_four, //4
			 R.drawable.n_five, //5
			 R.drawable.n_six, //6
			 R.drawable.n_seven, //7
			 R.drawable.n_eight, //8
			 R.drawable.n_nine  //9
	 };
	 
	 public static LinearLayout createImageNumber(final Activity context, final int number) {
		 
		 String val = "" + number;
		 LinearLayout hL = new LinearLayout(context);
		 hL.setOrientation(LinearLayout.HORIZONTAL);
		 for (int j = 0; j < val.length(); j++) {
			 ImageView iv = new ImageView(context);
			 LayoutParams params = new LayoutParams(30, 30);
			 int index = Integer.parseInt(""+val.charAt(j));
			 iv.setBackgroundDrawable(context.getResources().getDrawable(numbers[index]));
			 iv.setLayoutParams(params);	
			 hL.addView(iv);
		 }
		 return hL;
	 }
}