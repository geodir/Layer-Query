package com.soluciones.extension.query;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soluciones.extension.layer.GeodirLayerManager;
import com.soluciones.extension.layer.MapLayerExtension;
import com.soluciones.extension.layer.model.Condition;

public abstract class GeodirQuery<Q extends Query> extends MapLayerExtension {

	protected final String queryDirectory = "querys";
	protected final String queryFileConf = "queryConf";
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected GeodirLayerManager geodirLayerManager;

	// protected final String queryName;
	protected final GeodirQueryProperties properties;

	protected Q query;

	public GeodirQuery(GeodirLayerManager geodirLayerManager,
			/* String queryName, */ GeodirQueryProperties properties) {
		super("geodir_query");
		this.geodirLayerManager = geodirLayerManager;
		// this.queryName = queryName;
		this.properties = properties;

	}

	public QueryConfiguration loadLayerQuery() {
		QueryConfiguration queryConfiguration;
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() != null) {
			File file = new File(this.geodirLayerManager.getLayerConfiguration().getLayer().getPath() + "/"
					+ this.queryDirectory + "/" + this.queryFileConf + ".xml");
			if (file.exists()) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(QueryConfiguration.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					queryConfiguration = (QueryConfiguration) jaxbUnmarshaller.unmarshal(file);
				} catch (JAXBException e) {
					e.printStackTrace();
					queryConfiguration = new QueryConfiguration();
				}
			} else {
				queryConfiguration = new QueryConfiguration();
			}
		} else {
			queryConfiguration = new QueryConfiguration();
		}
		return queryConfiguration;
	}

	public String saveLayerQuery(QueryConfiguration queryConfiguration) {

		final String finalQuery = queryConfiguration
				.generateSQL(this.geodirLayerManager.getLayerConfiguration().getDafaultConditions());
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() == null) {
			log.info(queryConfiguration.toString());
			return finalQuery;
		}
		this.geodirLayerManager.getLayerConfiguration().getLayer().setQuery(finalQuery);
		this.geodirLayerManager.saveConfiguration();
		File parent_file = new File(
				this.geodirLayerManager.getLayerConfiguration().getLayer().getPath() + "/" + this.queryDirectory + "/");
		if (!parent_file.exists()) {
			parent_file.mkdirs();
		}
		File file = new File(parent_file, "/" + this.queryFileConf + ".xml");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(QueryConfiguration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(queryConfiguration, file);
			log.info("done..");
		} catch (JAXBException e) {
			log.error("fail creating file..", e);
		}
		return finalQuery;
	};

	public abstract String generateQuery();

	public abstract Q initQuery(String column);

	public abstract Q loadColumnQuery(int id);

	public String newColumnQuery(Q query) {
		this.query = query;
		QueryConfiguration queryConfiguration = loadLayerQuery();
		Condition condition = this.query.generateCondition();
		if (condition != null) {
			queryConfiguration.addCondition(condition);
			saveColumnQuery(query, condition.getId());
		}

		saveLayerQuery(queryConfiguration);
		return queryConfiguration.generateSQL(this.geodirLayerManager.getLayerConfiguration().getDafaultConditions());
	};

	public String updateColumnQuery(Q query, int id) {
		this.query = query;
		QueryConfiguration queryConfiguration = loadLayerQuery();
		Condition condition = this.query.generateCondition();
		if (condition != null) {
			queryConfiguration.updateCondition(condition, id);
			saveColumnQuery(query, id);
		}

		saveLayerQuery(queryConfiguration);
		return queryConfiguration.generateSQL(this.geodirLayerManager.getLayerConfiguration().getDafaultConditions());
	};

	public String deleteColumnQuery(int id) {
		QueryConfiguration queryConfiguration = loadLayerQuery();
		queryConfiguration.removeCondition(id);
		deleteColumnQuerys(id);
		saveLayerQuery(queryConfiguration);
		return queryConfiguration.generateSQL(this.geodirLayerManager.getLayerConfiguration().getDafaultConditions());
	};

	private String saveColumnQuery(Q query, int id) {
		this.query = query;
		final String finalQuery = query
				.generateSQL(this.geodirLayerManager.getLayerConfiguration().getDafaultConditions());
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() == null) {
			log.info(query.toString());
			return finalQuery;
		}
		this.geodirLayerManager.getLayerConfiguration().getLayer().setQuery(finalQuery);
		this.geodirLayerManager.saveConfiguration();
		File parent_file = new File(this.geodirLayerManager.getLayerConfiguration().getLayer().getPath() + "/"
				+ this.queryDirectory + "/" + id + "/");
		if (!parent_file.exists()) {
			parent_file.mkdirs();
		}
		File file = new File(parent_file, this.properties.getName() + ".xml");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(query.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(query, file);
			log.info("done..");
		} catch (JAXBException e) {
			log.error("fail creating file..", e);
		}
		return finalQuery;
	};

	private void deleteColumnQuerys(int id) {
		File parent_file = new File(this.geodirLayerManager.getLayerConfiguration().getLayer().getPath() + "/"
				+ this.queryDirectory + "/" + id + "/");
		if (parent_file.exists()) {
			parent_file.delete();
		}
	};

	// public String getQueryName() {
	// return queryName;
	// }

	public GeodirQueryProperties getProperties() {
		return properties;
	}

	public Q getQuery() {
		return query;
	}

	public void setQuery(Q query) {
		this.query = query;
	}

}
