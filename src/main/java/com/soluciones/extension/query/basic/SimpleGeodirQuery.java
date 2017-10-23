package com.soluciones.extension.query.basic;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soluciones.extension.core.model.ColumnModel;
import com.soluciones.extension.core.service.LayerColumnService;
import com.soluciones.extension.layer.GeodirLayerManager;
import com.soluciones.extension.layer.service.LayerColumnDetailsService;
import com.soluciones.extension.query.GeodirQuery;
import com.soluciones.extension.query.GeodirQueryProperties;

@Component
public class SimpleGeodirQuery extends GeodirQuery<SimpleQuery> {
	@Autowired
	private LayerColumnDetailsService layerColumnDetailsService;

	@Autowired
	private LayerColumnService columnService;
	
	@Autowired
	public SimpleGeodirQuery(GeodirLayerManager geodirLayerManager) {
		super(geodirLayerManager, new GeodirQueryProperties("general", "General", "filtro General"));
	}

	@Override
	public String generateQuery() {
		return null;
	}
	@Override
	public SimpleQuery loadColumnQuery(int id) {
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() != null) {
			File file = new File(this.geodirLayerManager.getLayerConfiguration().getLayer().getPath() + "/"
					+ this.queryDirectory + "/" + this.properties.getName() + ".xml");
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(SimpleQuery.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					this.query = (SimpleQuery) jaxbUnmarshaller.unmarshal(file);
				} catch (JAXBException e) {
					e.printStackTrace();
					this.query = new SimpleQuery();
				}
			} else {
				this.query = new SimpleQuery();
			}
			/*
			 * this.query.setAllColumns(layerColumnDetailsService
			 * .allColumns(this.geodirLayerManager.getLayerConfiguration().
			 * getLayer().getName())); if (this.query.getColumn() == null &&
			 * this.query.getAllColumns().size() > 0) {
			 * this.query.setColumn(this.query.getAllColumns().get(0)); }
			 */

			if (this.query.getColumn() != null) {
				this.query.setPrincipalValues(layerColumnDetailsService.obtainColumnMetadata(
						this.geodirLayerManager.getLayerConfiguration().getLayer().getName(), this.query.getColumn()));
			}
		} else {
			this.query = new SimpleQuery();
		}
		return this.query;
	}

	@Override
	public SimpleQuery initQuery(String column) {
		List<ColumnModel> all = columnService
				.allColumns(this.geodirLayerManager.getLayerConfiguration().getLayer().getName());
		this.query = new SimpleQuery();
		for (ColumnModel columnModel : all) {
			if (columnModel.getName().equalsIgnoreCase(column)) {
				this.query.setColumn(columnModel);
				break;
			}
		}
		if (this.query.getColumn() != null) {
			this.query.setPrincipalValues(layerColumnDetailsService.obtainColumnMetadata(
					this.geodirLayerManager.getLayerConfiguration().getLayer().getName(), this.query.getColumn()));
		}
		return this.query;
	}

}
