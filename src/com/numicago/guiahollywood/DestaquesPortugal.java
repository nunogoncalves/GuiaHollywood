package com.numicago.guiahollywood;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.TagNode;

import com.numicago.guiahollywood.objects.Destaque;

public class DestaquesPortugal 
{
//	private String TAG = "DestaquesPortugal";
	private HtmlHelper hh;
	private List<Destaque> listaDestaque;
	
	public List<Destaque> getDestaques(String site)
	{
		try
		{
			listaDestaque = new ArrayList<Destaque>();
			hh = new HtmlHelper(new URL(site)) 
			{	//Método responsável por carregar toda a informação do site para as instancias de Filme.			
				public void getContents() 
				{
					String descricao ="";
					String titulo = "";
					String imageURL = "";
					String verMaisLink = "";
					
					TagNode divElements[] = rootNode.getElementsByName("div", true);
					String divTitleDescricao = "post"; //Tinha post Buscar a descricao //entre é do html no site 
					for (int i = 0; divElements != null && i < divElements.length; i++)
					{
						String idAttribute = divElements[i].getAttributeByName("class");
						if (idAttribute != null && idAttribute.equals(divTitleDescricao))
						{
							descricao = divElements[i].getText().toString().replaceAll("\n", " ").replaceAll("  ", "");
							
							TagNode descricaoAttribute[] = divElements[i].getElementsByName("div", true);
							TagNode imgAttribute[] = divElements[i].getElementsByName("img", true);
							TagNode linkAttribute[] = divElements[i].getElementsByName("a", true);
							String divDescricao = "title";
							for(int j = 0; descricaoAttribute != null && j < descricaoAttribute.length; j++)
							{
								String idDescriptionAttribute = descricaoAttribute[j].getAttributeByName("class");
								if (idDescriptionAttribute != null && idDescriptionAttribute.equals(divDescricao))
								{
									titulo = descricaoAttribute[j].getText().toString().replaceAll("\n", " ").replaceAll("  ", "");
									
									while(titulo.contains("&#"))
									{
										int x = titulo.indexOf("&#");
										int y = (titulo.indexOf("&#"))+7;
										String aRemoverTitulo = titulo.substring(x, y);
										titulo = titulo.replace(aRemoverTitulo, "");
									}
									
									int startPoint = titulo.length() + 1;
									descricao = descricao.substring(startPoint,descricao.length());
									
									while(descricao.contains("&#"))
									{
										int x = descricao.indexOf("&#");
										int y = (descricao.indexOf("&#"))+7;
										String aRemoverDescricao = descricao.substring(x, y);
										descricao = descricao.replace(aRemoverDescricao, "");
									}
									
									imageURL = imgAttribute[0].getAttributeByName("src");
									verMaisLink = linkAttribute[0].getAttributeByName("href");
									
									Destaque destaque = new Destaque(titulo, imageURL, descricao, verMaisLink);
									listaDestaque.add(destaque);
									break;
								}
							}
						}
					}
					
				}
			};

			hh.getContents();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return listaDestaque;
	}
}
