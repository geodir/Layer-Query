package com.soluciones.extension.query.basic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.soluciones.extension.core.model.ColumnModel;
import com.soluciones.extension.core.type.ColumnType;
import com.soluciones.extension.layer.model.Condition;
import com.soluciones.extension.layer.model.Operator;
import com.soluciones.extension.layer.model.PrincipalValues;
import com.soluciones.extension.layer.model.QueryValue;
import com.soluciones.extension.query.Query;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleQuery extends Query {

	private static final String EXCLUDETEMPATE = " IN (%s)";
	private static final String NOTEXCLUDETEMPATE = " NOT IN (%s)";

	private PrincipalValues principalValues;
	private ColumnModel column;
	private String filter = "";
	private List<QueryValue> values;
	private boolean exclude;

	public SimpleQuery() {
		this.principalValues = new PrincipalValues();
		this.values = new ArrayList<>();
	}

	@XmlElement
	public ColumnModel getColumn() {
		return column;

	}

	public void setColumn(ColumnModel column) {
		this.column = column;
	}

	@XmlElement
	public List<QueryValue> getValues() {
		return values;
	}

	public void setValues(List<QueryValue> values) {
		this.values = values;
	}

	public PrincipalValues getPrincipalValues() {
		return principalValues;
	}

	public void setPrincipalValues(PrincipalValues principalValues) {
		this.principalValues = principalValues;
	}

	@XmlElement
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@XmlElement
	public boolean isExclude() {
		return exclude;
	}

	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	@Override
	public String toString() {
		return "SimpleQuery [column=" + column + ", filter=" + filter + ", values=" + values + "]";
	}

	@Override
	public String generateSQL(List<Condition> dafaultConditions) {
		StringBuilder SQL = new StringBuilder();
		List<String> defCon = new ArrayList<>();
		for (Condition def : dafaultConditions) {
			String defSQL = def.toSQL(def.getColumn().getName());
			if (defSQL != null) {
				defCon.add(defSQL);
			}
		}
		SQL.append(String.join(" AND ", defCon));
		if (this.column == null)
			return SQL.toString();

		if (dafaultConditions.size() > 0 && values.size() > 0) {
			SQL.append(" AND ");
		}
		List<String> _valuesSQL = new ArrayList<>();
		for (QueryValue _v : values) {
			if (this.column.getType() == ColumnType.DATE) {
				_valuesSQL.add("'" + _v.getValue() + "'");
			} else if (this.column.getType() == ColumnType.TEXT || this.column.getType() == ColumnType.STRING) {
				_valuesSQL.add("'" + _v.getValue() + "'");
			} else {
				_valuesSQL.add(_v.getValue());
			}
		}
		if (values.size() > 0) {
			if (this.exclude) {
				SQL.append(this.column.getName() + String.format(NOTEXCLUDETEMPATE, String.join(",", _valuesSQL)));
			} else {
				SQL.append(this.column.getName() + String.format(EXCLUDETEMPATE, String.join(",", _valuesSQL)));
			}
		}

		return SQL.toString();
	}

	@Override
	public Condition generateCondition() {
		Condition condition = new Condition();
		condition.setColumn(this.column);
		List<String> _valuesSQL = new ArrayList<>();
		List<String> _valuesSQLALIAS = new ArrayList<>();
		for (QueryValue _v : values) {
			if (this.column.getType() == ColumnType.TEXT || this.column.getType() == ColumnType.STRING) {
				_valuesSQLALIAS.add("'" + _v.getAlias() + "'");
				_valuesSQL.add("'" + _v.getValue() + "'");
			} else if (this.column.getType() == ColumnType.DATE) {
				_valuesSQLALIAS.add("'" + _v.getAlias() + "'");
				_valuesSQL.add("'" + _v.getValue() + "'");
			} else {
				_valuesSQLALIAS.add(_v.getAlias());
				_valuesSQL.add(_v.getValue());
			}
		}
		if (values.size() > 0) {
			QueryValue[] queryValues = new QueryValue[values.size()];
			for (int i = 0; i < values.size(); i++) {
				queryValues[i] = new QueryValue(values.get(i).getValue(), values.get(i).getAlias());
			}
			condition.setValue(queryValues);
			if (this.exclude) {
				condition.setOperator(Operator.NOT_IN);
			} else {
				condition.setOperator(Operator.IN);
			}
			return condition;
		}
		return null;
	}

}
