/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.component.factory.engine;

import java.net.URI;
import java.util.LinkedHashMap;

import org.joda.beans.BeanDefinition;
import org.joda.beans.PropertyDefinition;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.ComponentServer;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.rest.RemoteComponentServer;
import java.util.Map;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * 
 */
@BeanDefinition
public abstract class AbstractRemoteComponentServerComponentFactory extends AbstractComponentFactory {

  /**
   * The remote URI.
   */
  @PropertyDefinition(validate = "notNull")
  private URI _baseUri;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;

  //-------------------------------------------------------------------------
  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) {
    RemoteComponentServer remote = new RemoteComponentServer(_baseUri);
    ComponentServer server = remote.getComponentServer();
    for (ComponentInfo info : server.getComponentInfos()) {
      initComponent(repo, info);
    }
  }
 
  /**
   * Initialize the remote component.
   * 
   * @param repo  the local repository, not null
   * @param info  the remote information, not null
   */
  protected abstract void initComponent(ComponentRepository repo, ComponentInfo info);
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code AbstractRemoteComponentServerComponentFactory}.
   * @return the meta-bean, not null
   */
  public static AbstractRemoteComponentServerComponentFactory.Meta meta() {
    return AbstractRemoteComponentServerComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(AbstractRemoteComponentServerComponentFactory.Meta.INSTANCE);
  }

  @Override
  public AbstractRemoteComponentServerComponentFactory.Meta metaBean() {
    return AbstractRemoteComponentServerComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -332625701:  // baseUri
        return getBaseUri();
      case -614707837:  // publishRest
        return isPublishRest();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -332625701:  // baseUri
        setBaseUri((URI) newValue);
        return;
      case -614707837:  // publishRest
        setPublishRest((Boolean) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_baseUri, "baseUri");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      AbstractRemoteComponentServerComponentFactory other = (AbstractRemoteComponentServerComponentFactory) obj;
      return JodaBeanUtils.equal(getBaseUri(), other.getBaseUri()) &&
          JodaBeanUtils.equal(isPublishRest(), other.isPublishRest()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getBaseUri());
    hash += hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the remote URI.
   * @return the value of the property, not null
   */
  public URI getBaseUri() {
    return _baseUri;
  }

  /**
   * Sets the remote URI.
   * @param baseUri  the new value of the property, not null
   */
  public void setBaseUri(URI baseUri) {
    JodaBeanUtils.notNull(baseUri, "baseUri");
    this._baseUri = baseUri;
  }

  /**
   * Gets the the {@code baseUri} property.
   * @return the property, not null
   */
  public final Property<URI> baseUri() {
    return metaBean().baseUri().createProperty(this);
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
   * The meta-bean for {@code AbstractRemoteComponentServerComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code baseUri} property.
     */
    private final MetaProperty<URI> _baseUri = DirectMetaProperty.ofReadWrite(
        this, "baseUri", AbstractRemoteComponentServerComponentFactory.class, URI.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", AbstractRemoteComponentServerComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "baseUri",
        "publishRest");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -332625701:  // baseUri
          return _baseUri;
        case -614707837:  // publishRest
          return _publishRest;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends AbstractRemoteComponentServerComponentFactory> builder() {
      throw new UnsupportedOperationException("AbstractRemoteComponentServerComponentFactory is an abstract class");
    }

    @Override
    public Class<? extends AbstractRemoteComponentServerComponentFactory> beanType() {
      return AbstractRemoteComponentServerComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code baseUri} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<URI> baseUri() {
      return _baseUri;
    }

    /**
     * The meta-property for the {@code publishRest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> publishRest() {
      return _publishRest;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
