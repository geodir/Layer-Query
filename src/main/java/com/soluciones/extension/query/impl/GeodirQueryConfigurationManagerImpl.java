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

@Component
public class GeodirQueryConfigurationManagerImpl {

//	protected final Logger log = LoggerFactory.getLogger(this.getClass());
//	private List<String> queryNames;
//	private Map<String, GeodirQuery<?>> queryModels;
//
//	@Autowired
//	public GeodirQueryConfigurationManagerImpl(ApplicationContext ctx) {
//		this.queryModels = new TreeMap<>();
//		this.queryNames = new ArrayList<>();
//
//		@SuppressWarnings("rawtypes")
//		Map<String, GeodirQuery> producers = ctx.getBeansOfType(GeodirQuery.class);
//		Iterator<String> it = producers.keySet().iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			queryNames.add(producers.get(key).getProperties().getName());
//			queryModels.put(producers.get(key).getProperties().getName(), producers.get(key));
//		}
//		for (String th : queryNames) {
//			log.info("Query name: " + th);
//		}
//	}
//
//	public List<String> getQueryNames() {
//		return queryNames;
//	}
//
//	public void setQueryNames(List<String> queryNames) {
//		this.queryNames = queryNames;
//	}
//
//	public Map<String, GeodirQuery<?>> generateModels() {
//		return queryModels;
//	}
}
