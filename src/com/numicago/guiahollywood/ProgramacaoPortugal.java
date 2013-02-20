package com.numicago.guiahollywood;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.TagNode;

import com.numicago.guiahollywood.objects.Day;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.objects.Movie;

public class ProgramacaoPortugal extends ProgramacaoCanal
{
	public static List<DayMovie> getProgramacaoPortugal(TagNode rootNode, String site)
	{
		List<DayMovie> listaFilmesDoDia = new ArrayList<DayMovie>();
		
		TagNode a = ((TagNode)((TagNode) rootNode.getElementsByName("div", true)[1].getChildren().get(1)).getChildren().get(5));
		TagNode b = a.findElementByName("div", true).getChildTags()[0].getChildTags()[2];
		
		@SuppressWarnings("unchecked")
		List<TagNode> filmes = b.getElementListByAttValue("id", "parrilla_registro", true, false);
		Iterator<TagNode> fIterator = filmes.iterator();
		
		int ano = Integer.parseInt(site.substring(((site.length())-10), site.length()-6));
		int mes = Integer.parseInt(site.substring(((site.length())-5), site.length()-3)) - 1; 
		int dia = Integer.parseInt(site.substring(((site.length())-2), site.length())); 
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		int j = 0;
		while (fIterator.hasNext()) {
			TagNode node = fIterator.next();
			DayMovie dayMovie = new DayMovie();
			Movie movie = new Movie();
			Day day = new Day();
			movie.setId(j++);
			movie.setHollywoodUrl(((TagNode) node.getChildren().get(3)).getElementsByName("a", true)[0].getAttributeByName("href"));
			movie.setOriginalName(((TagNode) node.getChildren().get(5)).getChildren().get(3).toString().replace("<!--<br />", "").replace("-->", "").replace("Título Original: ",""));
			movie.setLocalName(((TagNode)(((TagNode)(((TagNode) node.getChildren().get(5)).getChildren().get(0))).getChildren().get(0))).getChildren().get(0).toString());
			movie.setImagemSmallUrl(((TagNode) node.getChildren().get(3)).getElementsByName("a", true)[0].getElementsByName("img", true)[0].getAttributeByName("src"));
			movie.setGenre(((TagNode) node.getChildren().get(1)).getChildren().get(1).toString().replace("<!-- -", "").replace("-->", ""));
			movie.setImageSmallBlob(Utils.getImageBlob(movie.getImageSmallUrl()).toByteArray());
			movie.setChannelId(EnumCountryCanal.PORTUGAL.getId());
			try {
				NumiCal tempCal = new NumiCal(sdf.parse(((TagNode) node.getChildren().get(1)).getChildren().get(0).toString()));
				tempCal.set(ano, mes, dia);
				int hour = tempCal.getHourOfDay();
				if(hour >= 0 && hour < 6) {
					tempCal.addDays(1);
				}
				
				day.setStart(tempCal);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dayMovie.setMovie(movie);
			dayMovie.setDay(day);
			System.out.println(movie.toString());
			listaFilmesDoDia.add(dayMovie);
		}
		
//		setEndTimes(listaFilmesDoDia, ano, mes, dia);
		
		for (int i = 0; i < listaFilmesDoDia.size(); i++) {
			NumiCal end = new NumiCal(ano, mes, dia);
			//if index is the last one add two hours to the start date.
			if (i == listaFilmesDoDia.size() - 1) {
				end.setCalendar(listaFilmesDoDia.get(i).getDay().getStart());
				end.addHours(2);
				listaFilmesDoDia.get(i).getDay().setEnd(end);
			} else { //else put the end time of the current movie = next movie start time
				end.setTime(listaFilmesDoDia.get(i+1).getDay().getStart().getTime());
				listaFilmesDoDia.get(i).getDay().setEnd(end);
			}
		}
		return listaFilmesDoDia;
	}
}
