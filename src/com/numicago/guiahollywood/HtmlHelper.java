package com.numicago.guiahollywood;

import java.io.IOException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * HtmlCleaner is open-source HTML parser written in Java. HTML found on Web is usually dirty, 
 * ill-formed and unsuitable for further processing. For any serious consumption of such documents, 
 * it is necessary to first clean up the mess and bring the order to tags, attributes and ordinary text. 
 * For the given HTML document, HtmlCleaner reorders individual elements and produces well-formed XML. 
 * By default, it follows similar rules that the most of web browsers use in order to create Document Object Model.
 * However, user may provide custom tag and rule set for tag filtering and balancing.  
 * @author http://htmlcleaner.sourceforge.net/
 *
 */
public abstract class HtmlHelper 
{
	TagNode rootNode;

	public HtmlHelper(URL htmlPage) throws IOException
	{
		HtmlCleaner cleaner = new HtmlCleaner();
		rootNode = cleaner.clean(htmlPage);
	}


/**
 * Método abstracto para receber conteudo de site
 */
	public abstract void getContents();
	
}
