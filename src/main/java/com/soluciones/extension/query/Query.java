package com.soluciones.extension.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.soluciones.extension.layer.model.Condition;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class Query {
	protected SimpleDateFormat dtFrom = new SimpleDateFormat("dd/MM/yyyy");
	protected SimpleDateFormat dtTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	protected List<Condition> conditions;

	public Query() {
		this.conditions = new ArrayList<>();
	}

	@XmlElement
	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	abstract public String generateSQL(List<Condition> dafaultConditions);

	abstract public Condition generateCondition();
}
