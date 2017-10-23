package com.soluciones.extension.query;

public class GeodirQueryProperties {
	private String name;
	private String title;
	private String description;

	public GeodirQueryProperties(String name, String title, String description) {
		this.name = name;
		this.title = title;
		this.description = description;
	}
 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
