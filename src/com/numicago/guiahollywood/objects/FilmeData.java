package com.numicago.guiahollywood.objects;

import com.numicago.guiahollywood.NumiCal;

public class FilmeData {

	private int id;
	
	private String nomePt;
	
	private NumiCal inicio;
	
	private NumiCal fim;
	
	public FilmeData() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NumiCal getInicio() {
		return inicio;
	}

	public void setInicio(NumiCal inicio) {
		this.inicio = inicio;
	}

	public NumiCal getFim() {
		return fim;
	}

	public void setFim(NumiCal fim) {
		this.fim = fim;
	}

	public String getNomePt() {
		return nomePt;
	}

	public void setNomePt(String nomePt) {
		this.nomePt = nomePt;
	}
	
	
	
}
