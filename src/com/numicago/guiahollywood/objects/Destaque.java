package com.numicago.guiahollywood.objects;

public class Destaque 
{
	private String titulo;
	private String imageURL;
	private String descricao;
	private String verMaisLink;
	private byte[] imageBlob;
	
	public Destaque(String titulo, String imageURL, String descricao, String verMaisLink)
	{
		this.titulo = titulo;
		this.imageURL = imageURL;
		this.descricao = descricao;
		this.verMaisLink = verMaisLink;
	}

	public Destaque()
	{
		
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getVerMaisLink() {
		return verMaisLink;
	}

	public void setVerMaisLink(String verMaisLink) {
		this.verMaisLink = verMaisLink;
	}

	public byte[] getImageBlob() {
		return imageBlob;
	}

	public void setImageBlob(byte[] imageBlob) {
		this.imageBlob = imageBlob;
	}
}
