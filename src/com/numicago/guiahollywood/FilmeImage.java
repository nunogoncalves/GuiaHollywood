package com.numicago.guiahollywood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class FilmeImage extends View
{
	private final Drawable logo;

	  public FilmeImage(Context context)
	  {
	    super(context);
	    logo = context.getResources().getDrawable(R.id.imagem_filme);
	    setBackgroundDrawable(logo);
	  }

	  public FilmeImage(Context context, AttributeSet attrs) 
	  {
	    super(context, attrs);
	    logo = context.getResources().getDrawable(R.id.imagem_filme);
	    setBackgroundDrawable(logo);
	  }

	  public FilmeImage(Context context, AttributeSet attrs, int defStyle) 
	  {
	    super(context, attrs, defStyle);
	    logo = context.getResources().getDrawable(R.id.imagem_filme);
	    setBackgroundDrawable(logo);
	  }

	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	  {
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = width * logo.getIntrinsicHeight() / logo.getIntrinsicWidth();
	    setMeasuredDimension(width, height);
	  }

}
