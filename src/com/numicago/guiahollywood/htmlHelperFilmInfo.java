package com.numicago.guiahollywood;

import java.io.IOException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.util.Log;

import com.numicago.guiahollywood.objects.Movie;

public class htmlHelperFilmInfo 
{
    TagNode rootNode;
    
    public htmlHelperFilmInfo(URL htmlPage) throws IOException
    {
        HtmlCleaner cleaner = new HtmlCleaner();
        MyLog.p(htmlPage.toString());
        rootNode = cleaner.clean(htmlPage);
        
        
    }

    /**
     * Retorna a descrição do filme presente na página
     * @param CSSClassname
     * @return 
     */
    public String getDescription(String CSSClassname)
    {
    	String descricao = "";
        
        TagNode divElements[] = rootNode.getElementsByName("div", true);
        for (int i = 0; divElements != null && i < divElements.length; i++)
        {
            String idAttribute = divElements[i].getAttributeByName("id");
            if (idAttribute != null && idAttribute.equals(CSSClassname))
            {
            	descricao = divElements[i].getText().toString();
            	break;
            }
        }            
        return descricao;
    }
    
    public String getYear(String CSSClassname)
    {
    	String year = "";
    	
    	TagNode divElements[] = rootNode.getElementsByName("div", true);
    	for (int i = 0; divElements != null && i < divElements.length; i++)
    	{
    		String idAttribute = divElements[i].getAttributeByName("id");
    		if (idAttribute != null && idAttribute.equals(CSSClassname))
    		{
    			year = divElements[i].getText().toString();
    			break;
    		}
    	}            
    	return year.substring((year.indexOf("(")+1), year.indexOf(")"));
    }

    /**
     * Metodo que retira do site do canal, a imagem do filme requisitado.
     * @param CSSClassname
     * @return String url da imagem
     */
    public String getImageURL(String CSSClassname)
    {
    	String movieImage ="";
    	TagNode linkElements[] = rootNode.getElementsByName("div", true);
        for (int i = 0; linkElements != null && i < linkElements.length; i++)
        {
            String classType = linkElements[i].getAttributeByName("id");
            if (classType != null && classType.equals(CSSClassname))
            {
            	//Pedir todos os links dos filmes do dia (neste link é só um)
            	TagNode[] filmeImageLink = linkElements[i].getElementsByName("img", true);
            	for (int j = 0; filmeImageLink != null && j < filmeImageLink.length; j++)
            		if (classType != null && classType.equals(CSSClassname))
                    {
            			movieImage = filmeImageLink[j].getAttributeByName("src");
                    }
            }
        }
        return movieImage;
    }

	public String getNomeOriginal(String CSSClassname) {
		String nomeOriginal ="";
		TagNode divElements[] = rootNode.getElementsByName("div", true);
        for (int i = 0; divElements != null && i < divElements.length; i++)
        {
            String idAttribute = divElements[i].getAttributeByName("id");
            if (idAttribute != null && idAttribute.equals(CSSClassname))
            {
            	nomeOriginal = divElements[i].getText().toString();
            	break;
            }
        }            
        nomeOriginal = nomeOriginal.replaceAll("  ", "").replace("\n", "-");
        nomeOriginal = nomeOriginal.substring(nomeOriginal.indexOf(("Título Original: "))+17, nomeOriginal.indexOf(("Duração: "))-2);
        Log.d("htmlHelperFilmInfo", "nome= " + nomeOriginal);
        return nomeOriginal;
	}

	public Movie getInfoSecundaria(String CSSClassname) 
	{
		Movie movie = new Movie();
		String info ="";
		TagNode divElements[] = rootNode.getElementsByName("div", true);
		for (int i = 0; divElements != null && i < divElements.length; i++)
		{
			String idAttribute = divElements[i].getAttributeByName("id");
			if (idAttribute != null && idAttribute.equals(CSSClassname))
			{
				info = divElements[i].getText().toString();
				break;
			}
		}
		info = info.replaceAll("  ", "").replace("\n", "-");
		movie.setOriginalName(info.substring(info.indexOf(("Título Original: "))+17, info.indexOf(("Duração: "))-2));
		int duracao = Integer.parseInt(info.substring(info.indexOf(("Duração: "))+9, info.indexOf((": minutos"))));
		movie.setLength("" + duracao);
		movie.setDirector(info.substring(info.indexOf(("Diretor: "))+9, info.indexOf(("Distribuidor"))-2));		 
		return movie;
	}
}
