package org.hibernate.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.AbstractQueryImpl;
import org.hibernate.internal.CriteriaImpl.CriterionEntry;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;

import com.soluciones.extension.core.model.LayerTable;
import com.soluciones.extension.core.type.ColumnType;

public class CustomCriteriaImpl implements Criteria, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String entityOrClassName;
	private transient SessionImplementor session;
	private final String rootAlias;
	
	private Map<String, ColumnType> mapColumns;
	private LayerTable layerTable;
	
	
	private List<CriterionEntry> criterionEntries = new ArrayList<CriterionEntry>();
	private List<OrderEntry> orderEntries = new ArrayList<OrderEntry>();
	private Projection projection;
	private Criteria projectionCriteria;

	private List<Subcriteria> subcriteriaList = new ArrayList<Subcriteria>();

	private Map<String, FetchMode> fetchModes = new HashMap<String, FetchMode>();
	private Map<String, LockMode> lockModes = new HashMap<String, LockMode>();

	private Integer maxResults;
	private Integer firstResult;
	private Integer timeout;
	private Integer fetchSize;

	private boolean cacheable;
	private String cacheRegion;
	private String comment;
	private final List<String> queryHints = new ArrayList<String>();

	private FlushMode flushMode;
	private CacheMode cacheMode;
	private FlushMode sessionFlushMode;
	private CacheMode sessionCacheMode;

	private Boolean readOnly;

	private ResultTransformer resultTransformer = Criteria.ROOT_ENTITY;

	// Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CustomCriteriaImpl(String entityOrClassName, SessionImplementor session) {
		this(entityOrClassName, ROOT_ALIAS, session);
	}

	public CustomCriteriaImpl(String entityOrClassName, String alias, SessionImplementor session) {
		this.session = session;
		this.entityOrClassName = entityOrClassName;
		this.cacheable = false;
		this.rootAlias = alias;
	}
	
	public Map<String, ColumnType> getMapColumns() {
		return mapColumns;
	}

	public void setMapColumns(Map<String, ColumnType> mapColumns) {
		this.mapColumns = mapColumns;
	}
	
	

	public LayerTable getLayerTable() {
		return layerTable;
	}

	public void setLayerTable(LayerTable layerTable) {
		this.layerTable = layerTable;
	}

	@Override
	public String toString() {
		return "CriteriaImpl(" + entityOrClassName + ":" + (rootAlias == null ? "" : rootAlias)
				+ subcriteriaList.toString() + criterionEntries.toString()
				+ (projection == null ? "" : projection.toString()) + ')';
	}

	// State ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public SessionImplementor getSession() {
		return session;
	}

	public void setSession(SessionImplementor session) {
		this.session = session;
	}

	public String getEntityOrClassName() {
		return entityOrClassName;
	}

	public Map<String, LockMode> getLockModes() {
		return lockModes;
	}

	public Criteria getProjectionCriteria() {
		return projectionCriteria;
	}

	public Iterator<Subcriteria> iterateSubcriteria() {
		return subcriteriaList.iterator();
	}

	public Iterator<CriterionEntry> iterateExpressionEntries() {
		return criterionEntries.iterator();
	}

	public Iterator<OrderEntry> iterateOrderings() {
		return orderEntries.iterator();
	}

	public Criteria add(Criteria criteriaInst, Criterion expression) {
		criterionEntries.add(new CriterionEntry(expression, criteriaInst));
		return this;
	}

	// Criteria impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public String getAlias() {
		return rootAlias;
	}

	public Projection getProjection() {
		return projection;
	}

	@Override
	public Criteria setProjection(Projection projection) {
		this.projection = projection;
		this.projectionCriteria = this;
		setResultTransformer(PROJECTION);
		return this;
	}

	@Override
	public Criteria add(Criterion expression) {
		add(this, expression);
		return this;
	}

	@Override
	public Criteria addOrder(Order ordering) {
		orderEntries.add(new OrderEntry(ordering, this));
		return this;
	}

	public FetchMode getFetchMode(String path) {
		return fetchModes.get(path);
	}

	@Override
	public Criteria setFetchMode(String associationPath, FetchMode mode) {
		fetchModes.put(associationPath, mode);
		return this;
	}

	@Override
	public Criteria setLockMode(LockMode lockMode) {
		return setLockMode(getAlias(), lockMode);
	}

	@Override
	public Criteria setLockMode(String alias, LockMode lockMode) {
		lockModes.put(alias, lockMode);
		return this;
	}

	@Override
	public Criteria createAlias(String associationPath, String alias) {
		return createAlias(associationPath, alias, JoinType.INNER_JOIN);
	}

	@Override
	public Criteria createAlias(String associationPath, String alias, JoinType joinType) {
		new Subcriteria(this, associationPath, alias, joinType);
		return this;
	}

	@Override
	public Criteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
		return createAlias(associationPath, alias, JoinType.parse(joinType));
	}

	@Override
	public Criteria createAlias(String associationPath, String alias, JoinType joinType, Criterion withClause) {
		new Subcriteria(this, associationPath, alias, joinType, withClause);
		return this;
	}

	@Override
	public Criteria createAlias(String associationPath, String alias, int joinType, Criterion withClause)
			throws HibernateException {
		return createAlias(associationPath, alias, JoinType.parse(joinType), withClause);
	}

	@Override
	public Criteria createCriteria(String associationPath) {
		return createCriteria(associationPath, JoinType.INNER_JOIN);
	}

	@Override
	public Criteria createCriteria(String associationPath, JoinType joinType) {
		return new Subcriteria(this, associationPath, joinType);
	}

	@Override
	public Criteria createCriteria(String associationPath, int joinType) throws HibernateException {
		return createCriteria(associationPath, JoinType.parse(joinType));
	}

	@Override
	public Criteria createCriteria(String associationPath, String alias) {
		return createCriteria(associationPath, alias, JoinType.INNER_JOIN);
	}

	@Override
	public Criteria createCriteria(String associationPath, String alias, JoinType joinType) {
		return new Subcriteria(this, associationPath, alias, joinType);
	}

	@Override
	public Criteria createCriteria(String associationPath, String alias, int joinType) throws HibernateException {
		return createCriteria(associationPath, alias, JoinType.parse(joinType));
	}

	@Override
	public Criteria createCriteria(String associationPath, String alias, JoinType joinType, Criterion withClause) {
		return new Subcriteria(this, associationPath, alias, joinType, withClause);
	}

	@Override
	public Criteria createCriteria(String associationPath, String alias, int joinType, Criterion withClause)
			throws HibernateException {
		return createCriteria(associationPath, alias, JoinType.parse(joinType), withClause);
	}

	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	@Override
	public Criteria setResultTransformer(ResultTransformer tupleMapper) {
		this.resultTransformer = tupleMapper;
		return this;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public Integer getFetchSize() {
		return fetchSize;
	}

	@Override
	public Criteria setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return this;
	}

	public Integer getTimeout() {
		return timeout;
	}

	@Override
	public Criteria setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	public boolean isReadOnlyInitialized() {
		return readOnly != null;
	}

	@Override
	public boolean isReadOnly() {
		if (!isReadOnlyInitialized() && getSession() == null) {
			throw new IllegalStateException(
					"cannot determine readOnly/modifiable setting when it is not initialized and is not initialized and getSession() == null");
		}
		return (isReadOnlyInitialized() ? readOnly : getSession().getPersistenceContext().isDefaultReadOnly());
	}

	@Override
	public Criteria setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public boolean getCacheable() {
		return this.cacheable;
	}

	@Override
	public Criteria setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
		return this;
	}

	public String getCacheRegion() {
		return this.cacheRegion;
	}

	@Override
	public Criteria setCacheRegion(String cacheRegion) {
		this.cacheRegion = cacheRegion.trim();
		return this;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public Criteria setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public List<String> getQueryHints() {
		return queryHints;
	}

	@Override
	public Criteria addQueryHint(String queryHint) {
		queryHints.add(queryHint);
		return this;
	}

	@Override
	public Criteria setFlushMode(FlushMode flushMode) {
		this.flushMode = flushMode;
		return this;
	}

	@Override
	public Criteria setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
		return this;
	}

	@Override
	public List list() throws HibernateException {
		before();
		try {			
			return session.list(this);
		} finally {
			after();
		}
	}
	
	/*
	public List list(Criteria criteria) throws HibernateException {
		// TODO: Is this guaranteed to always be CriteriaImpl?
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;

		final NaturalIdLoadAccess naturalIdLoadAccess = session.tryNaturalIdLoadAccess( criteriaImpl );
		if ( naturalIdLoadAccess != null ) {
			// EARLY EXIT!
			return Arrays.asList( naturalIdLoadAccess.load() );
		}

		errorIfClosed();
		checkTransactionSynchStatus();
		String[] implementors = factory.getImplementors( criteriaImpl.getEntityOrClassName() );
		int size = implementors.length;

		CriteriaLoader[] loaders = new CriteriaLoader[size];
		Set spaces = new HashSet();
		for ( int i = 0; i < size; i++ ) {

			loaders[i] = new CriteriaLoader(
					getOuterJoinLoadable( implementors[i] ),
					factory,
					criteriaImpl,
					implementors[i],
					getLoadQueryInfluencers()
			);

			spaces.addAll( loaders[i].getQuerySpaces() );

		}

		autoFlushIfRequired( spaces );

		List results = Collections.EMPTY_LIST;
		dontFlushFromFind++;
		boolean success = false;
		try {
			for ( int i = 0; i < size; i++ ) {
				final List currentResults = loaders[i].list( this );
				currentResults.addAll( results );
				results = currentResults;
			}
			success = true;
		}
		finally {
			dontFlushFromFind--;
			afterOperation( success );
			delayedAfterCompletion();
		}

		return results;
	}*/

	@Override
	public ScrollableResults scroll() {
		return scroll(session.getFactory().getDialect().defaultScrollMode());
	}

	@Override
	public ScrollableResults scroll(ScrollMode scrollMode) {
		before();
		try {
			return session.scroll(this, scrollMode);
		} finally {
			after();
		}
	}

	@Override
	public Object uniqueResult() throws HibernateException {
		return AbstractQueryImpl.uniqueElement(list());
	}

	protected void before() {
		if (flushMode != null) {
			sessionFlushMode = getSession().getFlushMode();
			getSession().setFlushMode(flushMode);
		}
		if (cacheMode != null) {
			sessionCacheMode = getSession().getCacheMode();
			getSession().setCacheMode(cacheMode);
		}
	}

	protected void after() {
		if (sessionFlushMode != null) {
			getSession().setFlushMode(sessionFlushMode);
			sessionFlushMode = null;
		}
		if (sessionCacheMode != null) {
			getSession().setCacheMode(sessionCacheMode);
			sessionCacheMode = null;
		}
	}

	public boolean isLookupByNaturalKey() {
		if (projection != null) {
			return false;
		}
		if (subcriteriaList.size() > 0) {
			return false;
		}
		if (criterionEntries.size() != 1) {
			return false;
		}
		CriterionEntry ce = criterionEntries.get(0);
		return ce.getCriterion() instanceof NaturalIdentifier;
	}

	// Inner classes ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final class Subcriteria implements Criteria, Serializable {

		private String alias;
		private String path;
		private Criteria parent;
		private LockMode lockMode;
		private JoinType joinType = JoinType.INNER_JOIN;
		private Criterion withClause;
		private boolean hasRestriction;

		// Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		private Subcriteria(Criteria parent, String path, String alias, JoinType joinType, Criterion withClause) {
			this.alias = alias;
			this.path = path;
			this.parent = parent;
			this.joinType = joinType;
			this.withClause = withClause;
			this.hasRestriction = withClause != null;
			CustomCriteriaImpl.this.subcriteriaList.add(this);
		}

		private Subcriteria(Criteria parent, String path, String alias, JoinType joinType) {
			this(parent, path, alias, joinType, null);
		}

		private Subcriteria(Criteria parent, String path, JoinType joinType) {
			this(parent, path, null, joinType);
		}

		@Override
		public String toString() {
			return "Subcriteria(" + path + ":" + (alias == null ? "" : alias) + ')';
		}

		// State ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		@Override
		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getPath() {
			return path;
		}

		public Criteria getParent() {
			return parent;
		}

		public LockMode getLockMode() {
			return lockMode;
		}

		@Override
		public Criteria setLockMode(LockMode lockMode) {
			this.lockMode = lockMode;
			return this;
		}

		public JoinType getJoinType() {
			return joinType;
		}

		public Criterion getWithClause() {
			return this.withClause;
		}

		public boolean hasRestriction() {
			return hasRestriction;
		}

		// Criteria impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		@Override
		public Criteria add(Criterion expression) {
			hasRestriction = true;
			CustomCriteriaImpl.this.add(this, expression);
			return this;
		}

		@Override
		public Criteria addOrder(Order order) {
			CustomCriteriaImpl.this.orderEntries.add(new OrderEntry(order, this));
			return this;
		}

		@Override
		public Criteria createAlias(String associationPath, String alias) {
			return createAlias(associationPath, alias, JoinType.INNER_JOIN);
		}

		@Override
		public Criteria createAlias(String associationPath, String alias, JoinType joinType) throws HibernateException {
			new Subcriteria(this, associationPath, alias, joinType);
			return this;
		}

		@Override
		public Criteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
			return createAlias(associationPath, alias, JoinType.parse(joinType));
		}

		@Override
		public Criteria createAlias(String associationPath, String alias, JoinType joinType, Criterion withClause)
				throws HibernateException {
			new Subcriteria(this, associationPath, alias, joinType, withClause);
			return this;
		}

		@Override
		public Criteria createAlias(String associationPath, String alias, int joinType, Criterion withClause)
				throws HibernateException {
			return createAlias(associationPath, alias, JoinType.parse(joinType), withClause);
		}

		@Override
		public Criteria createCriteria(String associationPath) {
			return createCriteria(associationPath, JoinType.INNER_JOIN);
		}

		@Override
		public Criteria createCriteria(String associationPath, JoinType joinType) throws HibernateException {
			return new Subcriteria(Subcriteria.this, associationPath, joinType);
		}

		@Override
		public Criteria createCriteria(String associationPath, int joinType) throws HibernateException {
			return createCriteria(associationPath, JoinType.parse(joinType));
		}

		@Override
		public Criteria createCriteria(String associationPath, String alias) {
			return createCriteria(associationPath, alias, JoinType.INNER_JOIN);
		}

		@Override
		public Criteria createCriteria(String associationPath, String alias, JoinType joinType)
				throws HibernateException {
			return new Subcriteria(Subcriteria.this, associationPath, alias, joinType);
		}

		@Override
		public Criteria createCriteria(String associationPath, String alias, int joinType) throws HibernateException {
			return createCriteria(associationPath, alias, JoinType.parse(joinType));
		}

		@Override
		public Criteria createCriteria(String associationPath, String alias, JoinType joinType, Criterion withClause)
				throws HibernateException {
			return new Subcriteria(this, associationPath, alias, joinType, withClause);
		}

		@Override
		public Criteria createCriteria(String associationPath, String alias, int joinType, Criterion withClause)
				throws HibernateException {
			return createCriteria(associationPath, alias, JoinType.parse(joinType), withClause);
		}

		@Override
		public boolean isReadOnly() {
			return CustomCriteriaImpl.this.isReadOnly();
		}

		@Override
		public boolean isReadOnlyInitialized() {
			return CustomCriteriaImpl.this.isReadOnlyInitialized();
		}

		@Override
		public Criteria setReadOnly(boolean readOnly) {
			CustomCriteriaImpl.this.setReadOnly(readOnly);
			return this;
		}

		@Override
		public Criteria setCacheable(boolean cacheable) {
			CustomCriteriaImpl.this.setCacheable(cacheable);
			return this;
		}

		@Override
		public Criteria setCacheRegion(String cacheRegion) {
			CustomCriteriaImpl.this.setCacheRegion(cacheRegion);
			return this;
		}

		@Override
		public List list() throws HibernateException {
			return CustomCriteriaImpl.this.list();
		}

		@Override
		public ScrollableResults scroll() throws HibernateException {
			return CustomCriteriaImpl.this.scroll();
		}

		@Override
		public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
			return CustomCriteriaImpl.this.scroll(scrollMode);
		}

		@Override
		public Object uniqueResult() throws HibernateException {
			return CustomCriteriaImpl.this.uniqueResult();
		}

		@Override
		public Criteria setFetchMode(String associationPath, FetchMode mode) {
			CustomCriteriaImpl.this.setFetchMode(StringHelper.qualify(path, associationPath), mode);
			return this;
		}

		@Override
		public Criteria setFlushMode(FlushMode flushMode) {
			CustomCriteriaImpl.this.setFlushMode(flushMode);
			return this;
		}

		@Override
		public Criteria setCacheMode(CacheMode cacheMode) {
			CustomCriteriaImpl.this.setCacheMode(cacheMode);
			return this;
		}

		@Override
		public Criteria setFirstResult(int firstResult) {
			CustomCriteriaImpl.this.setFirstResult(firstResult);
			return this;
		}

		@Override
		public Criteria setMaxResults(int maxResults) {
			CustomCriteriaImpl.this.setMaxResults(maxResults);
			return this;
		}

		@Override
		public Criteria setTimeout(int timeout) {
			CustomCriteriaImpl.this.setTimeout(timeout);
			return this;
		}

		@Override
		public Criteria setFetchSize(int fetchSize) {
			CustomCriteriaImpl.this.setFetchSize(fetchSize);
			return this;
		}

		@Override
		public Criteria setLockMode(String alias, LockMode lockMode) {
			CustomCriteriaImpl.this.setLockMode(alias, lockMode);
			return this;
		}

		@Override
		public Criteria setResultTransformer(ResultTransformer resultProcessor) {
			CustomCriteriaImpl.this.setResultTransformer(resultProcessor);
			return this;
		}

		@Override
		public Criteria setComment(String comment) {
			CustomCriteriaImpl.this.setComment(comment);
			return this;
		}

		@Override
		public Criteria addQueryHint(String queryHint) {
			CustomCriteriaImpl.this.addQueryHint(queryHint);
			return this;
		}

		@Override
		public Criteria setProjection(Projection projection) {
			CustomCriteriaImpl.this.projection = projection;
			CustomCriteriaImpl.this.projectionCriteria = this;
			setResultTransformer(PROJECTION);
			return this;
		}
	}

	public static final class CriterionEntry implements Serializable {
		private final Criterion criterion;
		private final Criteria criteria;

		private CriterionEntry(Criterion criterion, Criteria criteria) {
			this.criteria = criteria;
			this.criterion = criterion;
		}

		public Criterion getCriterion() {
			return criterion;
		}

		public Criteria getCriteria() {
			return criteria;
		}

		@Override
		public String toString() {
			return criterion.toString();
		}
	}

	public static final class OrderEntry implements Serializable {
		private final Order order;
		private final Criteria criteria;

		private OrderEntry(Order order, Criteria criteria) {
			this.criteria = criteria;
			this.order = order;
		}

		public Order getOrder() {
			return order;
		}

		public Criteria getCriteria() {
			return criteria;
		}

		@Override
		public String toString() {
			return order.toString();
		}
	}

}
