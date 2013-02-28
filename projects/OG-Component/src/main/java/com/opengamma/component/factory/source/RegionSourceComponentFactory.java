/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.source;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.core.region.RegionSource;
import com.opengamma.core.region.impl.DataRegionSourceResource;
import com.opengamma.core.region.impl.RemoteRegionSource;
import com.opengamma.master.region.RegionMaster;
import com.opengamma.master.region.impl.EHCachingRegionSource;
import com.opengamma.master.region.impl.MasterRegionSource;

/**
 * Component factory providing the {@code RegionSource}.
 */
@BeanDefinition
public class RegionSourceComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;
  /**
   * The cache manager.
   */
  @PropertyDefinition
  private CacheManager _cacheManager;
  /**
   * The underlying region master.
   */
  @PropertyDefinition(validate = "notNull")
  private RegionMaster _regionMaster;

  //-------------------------------------------------------------------------
  /**
   * Initializes the region source, setting up component information and REST.
   * Override using {@link #createRegionSource(ComponentRepository)}.
   * 
   * @param repo  the component repository, not null
   * @param configuration  the remaining configuration, not null
   */
  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) {
    RegionSource source = createRegionSource(repo);
    
    ComponentInfo info = new ComponentInfo(RegionSource.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    info.addAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA, RemoteRegionSource.class);
    repo.registerComponent(info, source);
    if (isPublishRest()) {
      repo.getRestComponents().publish(info, new DataRegionSourceResource(source));
    }
  }

  /**
   * Creates the region source without registering it.
   * 
   * @param repo  the component repository, only used to register secondary items like lifecycle, not null
   * @return the region source, not null
   */
  protected RegionSource createRegionSource(ComponentRepository repo) {
    RegionSource source = new MasterRegionSource(getRegionMaster());
    if (getCacheManager() != null) {
      source = new EHCachingRegionSource(source, getCacheManager());
    }
    return source;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code RegionSourceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static RegionSourceComponentFactory.Meta meta() {
    return RegionSourceComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(RegionSourceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public RegionSourceComponentFactory.Meta metaBean() {
    return RegionSourceComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        return getClassifier();
      case -614707837:  // publishRest
        return isPublishRest();
      case -1452875317:  // cacheManager
        return getCacheManager();
      case -1820969354:  // regionMaster
        return getRegionMaster();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case -614707837:  // publishRest
        setPublishRest((Boolean) newValue);
        return;
      case -1452875317:  // cacheManager
        setCacheManager((CacheManager) newValue);
        return;
      case -1820969354:  // regionMaster
        setRegionMaster((RegionMaster) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_regionMaster, "regionMaster");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      RegionSourceComponentFactory other = (RegionSourceComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(isPublishRest(), other.isPublishRest()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          JodaBeanUtils.equal(getRegionMaster(), other.getRegionMaster()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRegionMaster());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the flag determining whether the component should be published by REST (default true).
   * @return the value of the property
   */
  public boolean isPublishRest() {
    return _publishRest;
  }

  /**
   * Sets the flag determining whether the component should be published by REST (default true).
   * @param publishRest  the new value of the property
   */
  public void setPublishRest(boolean publishRest) {
    this._publishRest = publishRest;
  }

  /**
   * Gets the the {@code publishRest} property.
   * @return the property, not null
   */
  public final Property<Boolean> publishRest() {
    return metaBean().publishRest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cache manager.
   * @return the value of the property
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cache manager.
   * @param cacheManager  the new value of the property
   */
  public void setCacheManager(CacheManager cacheManager) {
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying region master.
   * @return the value of the property, not null
   */
  public RegionMaster getRegionMaster() {
    return _regionMaster;
  }

  /**
   * Sets the underlying region master.
   * @param regionMaster  the new value of the property, not null
   */
  public void setRegionMaster(RegionMaster regionMaster) {
    JodaBeanUtils.notNull(regionMaster, "regionMaster");
    this._regionMaster = regionMaster;
  }

  /**
   * Gets the the {@code regionMaster} property.
   * @return the property, not null
   */
  public final Property<RegionMaster> regionMaster() {
    return metaBean().regionMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code RegionSourceComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", RegionSourceComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", RegionSourceComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", RegionSourceComponentFactory.class, CacheManager.class);
    /**
     * The meta-property for the {@code regionMaster} property.
     */
    private final MetaProperty<RegionMaster> _regionMaster = DirectMetaProperty.ofReadWrite(
        this, "regionMaster", RegionSourceComponentFactory.class, RegionMaster.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "publishRest",
        "cacheManager",
        "regionMaster");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case -614707837:  // publishRest
          return _publishRest;
        case -1452875317:  // cacheManager
          return _cacheManager;
        case -1820969354:  // regionMaster
          return _regionMaster;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends RegionSourceComponentFactory> builder() {
      return new DirectBeanBuilder<RegionSourceComponentFactory>(new RegionSourceComponentFactory());
    }

    @Override
    public Class<? extends RegionSourceComponentFactory> beanType() {
      return RegionSourceComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code publishRest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> publishRest() {
      return _publishRest;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

    /**
     * The meta-property for the {@code regionMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<RegionMaster> regionMaster() {
      return _regionMaster;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
