package com.soluciones.extension.query.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.soluciones.extension.query.GeodirQuery;
import com.soluciones.extension.query.GeodirQueryProperties;

@Component
public class GeodirQueryManagerImpl {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private List<String> queryNames;
	private List<GeodirQueryProperties> queryProperties;
	private Map<String, GeodirQuery<?>> queryModels;

	@Autowired
	public GeodirQueryManagerImpl(ApplicationContext ctx) {
		this.queryModels = new TreeMap<>();
		this.queryNames = new ArrayList<>();
		this.queryProperties = new ArrayList<>();
		@SuppressWarnings("rawtypes")
		Map<String, GeodirQuery> producers = ctx.getBeansOfType(GeodirQuery.class);
		Iterator<String> it = producers.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			queryNames.add(producers.get(key).getProperties().getName());
			queryProperties.add(producers.get(key).getProperties());
			queryModels.put(producers.get(key).getProperties().getName(), producers.get(key));
		}
		for (String th : queryNames) {
			log.info("Query name: " + th);
		}
	}

	public List<String> getQueryNames() {
		return queryNames;
	}

	public List<GeodirQueryProperties> getQueryProperties() {
		return queryProperties;
	}

	public void setQueryProperties(List<GeodirQueryProperties> queryProperties) {
		this.queryProperties = queryProperties;
	}

	public void setQueryNames(List<String> queryNames) {
		this.queryNames = queryNames;
	}

	public Map<String, GeodirQuery<?>> generateModels() {
		return queryModels;
	}
}
