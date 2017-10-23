package com.soluciones.extension.query;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.bytecode.spi.EntityInstrumentationMetadata;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.entry.CacheEntry;
import org.hibernate.cache.spi.entry.CacheEntryStructure;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.EntityEntryFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.ValueInclusion;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.FilterAliasGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.walking.spi.AttributeDefinition;
import org.hibernate.persister.walking.spi.EntityIdentifierDefinition;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.EntityTuplizer;
import org.hibernate.type.Type;
import org.hibernate.type.VersionType;

public class DinamicEntityPersister implements EntityPersister{

	@Override
	public Comparator getVersionComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityPersister getEntityPersister() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityIdentifierDefinition getEntityKeyDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AttributeDefinition> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateEntityDefinition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInstantiate() throws MappingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SessionFactoryImplementor getFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityEntryFactory getEntityEntryFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRootEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMetamodel getEntityMetamodel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSubclassEntityName(String entityName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Serializable[] getPropertySpaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable[] getQuerySpaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCollections() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasMutableProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasSubselectLoadableCollections() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCascades() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInherited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIdentifierAssignedByInsert() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type getPropertyType(String propertyName) throws MappingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] findDirty(Object[] currentState, Object[] previousState, Object owner, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] findModified(Object[] old, Object[] current, Object object, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasIdentifierProperty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractIdOutOfEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVersioned() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public VersionType getVersionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVersionProperty() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasNaturalIdentifier() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getNaturalIdentifierProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getNaturalIdentifierSnapshot(Serializable id, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdentifierGenerator getIdentifierGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasLazyProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Serializable loadEntityIdByNaturalId(Object[] naturalIdValues, LockOptions lockOptions,
			SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object load(Serializable id, Object optionalObject, LockMode lockMode, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object load(Serializable id, Object optionalObject, LockOptions lockOptions, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lock(Serializable id, Object version, Object object, LockMode lockMode, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lock(Serializable id, Object version, Object object, LockOptions lockOptions,
			SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(Serializable id, Object[] fields, Object object, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Serializable insert(Object[] fields, Object object, SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Serializable id, Object version, Object object, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Serializable id, Object[] fields, int[] dirtyFields, boolean hasDirtyCollection,
			Object[] oldFields, Object oldVersion, Object object, Object rowId, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type[] getPropertyTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyInsertability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueInclusion[] getPropertyInsertGenerationInclusions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueInclusion[] getPropertyUpdateGenerationInclusions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyUpdateability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyCheckability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyNullability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyVersionability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getPropertyLaziness() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CascadeStyle[] getPropertyCascadeStyles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getIdentifierType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifierPropertyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCacheInvalidationRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLazyPropertiesCacheable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCache() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityRegionAccessStrategy getCacheAccessStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntryStructure getCacheEntryStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntry buildCacheEntry(Object entity, Object[] state, Object version, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNaturalIdCache() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NaturalIdRegionAccessStrategy getNaturalIdCacheAccessStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBatchLoadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelectBeforeUpdateRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] getDatabaseSnapshot(Serializable id, SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getIdByUniqueKey(Serializable key, String uniquePropertyName, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCurrentVersion(Serializable id, SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object forceVersionIncrement(Serializable id, Object currentVersion, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInstrumented() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasInsertGeneratedProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasUpdateGeneratedProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVersionPropertyGenerated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterInitialize(Object entity, boolean lazyPropertiesAreUnfetched, SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterReassociate(Object entity, SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object createProxy(Serializable id, SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransient(Object object, SessionImplementor session) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getPropertyValuesToInsert(Object object, Map mergeMap, SessionImplementor session)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processInsertGeneratedProperties(Serializable id, Object entity, Object[] state,
			SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processUpdateGeneratedProperties(Serializable id, Object entity, Object[] state,
			SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class getMappedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean implementsLifecycle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class getConcreteProxyClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPropertyValues(Object object, Object[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object object, int i, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getPropertyValues(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object object, int i) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object object, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getIdentifier(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getIdentifier(Object entity, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentifier(Object entity, Serializable id, SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getVersion(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(Serializable id, SessionImplementor session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInstance(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasUninitializedLazyProperties(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetIdentifier(Object entity, Serializable currentId, Object currentVersion,
			SessionImplementor session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EntityPersister getSubclassEntityPersister(Object instance, SessionFactoryImplementor factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMode getEntityMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityTuplizer getEntityTuplizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityInstrumentationMetadata getInstrumentationMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterAliasGenerator getFilterAliasGenerator(String rootAlias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] resolveAttributeIndexes(String[] attributeNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canUseReferenceCacheEntries() {
		// TODO Auto-generated method stub
		return false;
	}

}
