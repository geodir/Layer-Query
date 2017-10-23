package com.soluciones.extension.query;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.soluciones.extension.layer.model.Condition;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class QueryConfiguration {
	private List<Condition> conditions;
	private int idSequence = 0;

	public QueryConfiguration() {
		this.conditions = new ArrayList<>();
	}

	@XmlElement
	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	@XmlElement
	public int getIdSequence() {
		return idSequence;
	}

	public void setIdSequence(int idSequence) {
		this.idSequence = idSequence;
	}

	public void addCondition(Condition condition) {
		if (condition == null)
			return;
		this.idSequence++;
		condition.setId(idSequence);
		this.conditions.add(condition);
	}

	public void updateCondition(Condition condition, int id) {
		if (condition == null || id == 0)
			return;
		condition.setId(id);
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).getId() == condition.getId()) {
				conditions.set(i, condition);
				break;
			}
		}
	}

	public void removeCondition(int id) {
		if (id == 0)
			return;
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).getId() == id) {
				conditions.remove(i);
				break;
			}
		}
	}

	public String generateSQL(List<Condition> dafaultConditions) {
		StringBuilder SQL = new StringBuilder();
		List<String> defCon = new ArrayList<>();
		for (Condition def : dafaultConditions) {
			String defSQL = def.toSQL(def.getColumn().getName());
			if (defSQL != null) {
				defCon.add(defSQL);
			}
		}
		for (Condition def : this.conditions) {
			String defSQL = def.toSQL(def.getColumn().getName());
			if (defSQL != null) {
				defCon.add(defSQL);
			}
		}
		SQL.append(String.join(" AND ", defCon));
		return SQL.toString();
	}

}
