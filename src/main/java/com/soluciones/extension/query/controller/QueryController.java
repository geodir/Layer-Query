package com.soluciones.extension.query.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soluciones.extension.core.model.ColumnModel;
import com.soluciones.extension.core.service.LayerColumnService;
import com.soluciones.extension.layer.GeodirLayerManager;
import com.soluciones.extension.layer.XMLProvider;
import com.soluciones.extension.layer.model.GenericResponse;
import com.soluciones.extension.layer.model.PrincipalValues;
import com.soluciones.extension.layer.service.LayerColumnDetailsService;
import com.soluciones.extension.query.Query;
import com.soluciones.extension.query.QueryConfiguration;
import com.soluciones.extension.query.QueryLayerConf;
import com.soluciones.extension.query.basic.SimpleGeodirQuery;
import com.soluciones.extension.query.basic.SimpleQuery;
import com.soluciones.extension.query.impl.GeodirQueryManagerImpl;

@Controller
@RequestMapping("${geodir.ext.query.base:extension/query}")
public class QueryController {

	@Value("${geodir.ext.query.model:extension/query/query-layout}")
	private String baseTh;

	@Autowired
	private GeodirQueryManagerImpl geodirQueryManagerImpl;

	@Autowired
	private SimpleGeodirQuery simpleGeodirQuery;

	@Autowired
	protected GeodirLayerManager geodirLayerManager;
	
	@Autowired
	private LayerColumnService columnService;

	@Autowired
	private LayerColumnDetailsService layerColumnDetailsService;
	
	@RequestMapping("/all/{column}")
	public String ajaxFilters(Model model,@PathVariable("column") String column) {
		Iterator<String> it = geodirQueryManagerImpl.generateModels().keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Query query= geodirQueryManagerImpl.generateModels().get(key).initQuery(column);
			model.addAttribute("gquery_" + key, query);
		}
		QueryLayerConf conf = null;
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() != null) {
			String pathConfiguracion = this.geodirLayerManager.getLayerConfiguration().getLayer().getPath()
					+ "/querys/conf.xml";
			conf = XMLProvider.load(QueryLayerConf.class, pathConfiguracion);
		}
		if (conf == null) {
			conf = new QueryLayerConf();
		}
		model.addAttribute("queryconf", conf);
		model.addAttribute("geodir_querys", geodirQueryManagerImpl.getQueryProperties());
		return this.baseTh + " :: geodir_query";
	}

	@GetMapping("/{queryName}/data/{id}")
	@ResponseBody
	public Query obtainQueryModel(@PathVariable("queryName") String queryName, @PathVariable("id") int id,
			Model model) {
		Query query = geodirQueryManagerImpl.generateModels().get(queryName).loadColumnQuery(id);
		return query;
	}
	
	@GetMapping("/conf")
	@ResponseBody
	public ResponseEntity<QueryConfiguration> obtainQueryModel(Model model) {
		QueryConfiguration queryConfiguration = simpleGeodirQuery.loadLayerQuery();
		return ResponseEntity.ok(queryConfiguration);
	}
	
	@RequestMapping("/configuration")
	public String configuration(Model model) {
		model.addAttribute("query_configuration", simpleGeodirQuery.loadLayerQuery());
		return this.baseTh + " :: geodir_query_configuration";
	}
	
	@RequestMapping(value = "/columns/all", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ColumnModel>> columns() {
		List<ColumnModel> all = new ArrayList<>();
		if (this.geodirLayerManager.getLayerConfiguration().getLayer() != null) {
			all = columnService.allColumns(this.geodirLayerManager.getLayerConfiguration().getLayer().getName());
		}
		return ResponseEntity.ok(all);
	}
	
	@RequestMapping(value = "/general/save", method = RequestMethod.PUT)
	@ResponseBody
	public GenericResponse saveSimple(@RequestBody final SimpleQuery gquery_general) {
		return new GenericResponse(simpleGeodirQuery.newColumnQuery(gquery_general));
	}

	@RequestMapping(value = "/general/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public GenericResponse updateSimple(@RequestBody final SimpleQuery gquery_general, @PathVariable("id") int id) {
		return new GenericResponse(simpleGeodirQuery.updateColumnQuery(gquery_general, id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public GenericResponse updateSimple(@PathVariable("id") int id) {
		return new GenericResponse(simpleGeodirQuery.deleteColumnQuery(id));
	}
	
	@RequestMapping(value = "/defaultdilter", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse generateDefaulFilter() {
		SimpleQuery simple = new SimpleQuery();
		return new GenericResponse(simpleGeodirQuery.newColumnQuery(simple));
	}

	@PostMapping("/column/values")
	@ResponseBody
	public PrincipalValues saveSimple(@RequestBody final ColumnModel column) {
		return layerColumnDetailsService
				.obtainColumnMetadata(this.geodirLayerManager.getLayerConfiguration().getLayer().getName(), column);
	}

	// @RequestBody final AsistidoEntity direccion,

	// @RequestMapping(value = "/{queryName}/save", method = RequestMethod.POST,
	// params = { "save" })
	// public String saveGenral(@PathVariable("queryName") String queryName,
	// final Query query,
	// HttpServletRequest request) {
	// geodirQueryManagerImpl.generateModels().get(queryName).saveLayerQuery(query);
	// // simpleGeodirQuery.saveLayerQuery(query);
	// String referer = request.getHeader("Referer");
	// return "redirect:" + referer;
	// }

	@RequestMapping(value = "/saveconf", method = RequestMethod.POST, params = { "saveconf" })
	public String saveConf(final QueryLayerConf queryconf, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}
}
