package com.soluciones.extension.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class QueryLayerConf {
	private String queryName = "general";
	private String columnSelected;

	public QueryLayerConf() {

	}

	@XmlElement
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	@XmlElement
	public String getColumnSelected() {
		return columnSelected;
	}

	public void setColumnSelected(String columnSelected) {
		this.columnSelected = columnSelected;
	}

}
