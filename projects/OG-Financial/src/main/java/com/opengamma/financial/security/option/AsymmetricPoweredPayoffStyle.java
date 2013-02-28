/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * The asymmetric powered payoff style.
 */
@BeanDefinition
public class AsymmetricPoweredPayoffStyle extends PayoffStyle {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The power.
   */
  @PropertyDefinition
  private double _power;

  /**
   * Creates an instance.
   */
  private AsymmetricPoweredPayoffStyle() {
  }

  /**
   * Creates an instance.
   * 
   * @param power  the power
   */
  public AsymmetricPoweredPayoffStyle(double power) {
    setPower(power);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(PayoffStyleVisitor<T> visitor) {
    return visitor.visitAsymmetricPoweredPayoffStyle(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code AsymmetricPoweredPayoffStyle}.
   * @return the meta-bean, not null
   */
  public static AsymmetricPoweredPayoffStyle.Meta meta() {
    return AsymmetricPoweredPayoffStyle.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(AsymmetricPoweredPayoffStyle.Meta.INSTANCE);
  }

  @Override
  public AsymmetricPoweredPayoffStyle.Meta metaBean() {
    return AsymmetricPoweredPayoffStyle.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 106858757:  // power
        return getPower();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 106858757:  // power
        setPower((Double) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      AsymmetricPoweredPayoffStyle other = (AsymmetricPoweredPayoffStyle) obj;
      return JodaBeanUtils.equal(getPower(), other.getPower()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getPower());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the power.
   * @return the value of the property
   */
  public double getPower() {
    return _power;
  }

  /**
   * Sets the power.
   * @param power  the new value of the property
   */
  public void setPower(double power) {
    this._power = power;
  }

  /**
   * Gets the the {@code power} property.
   * @return the property, not null
   */
  public final Property<Double> power() {
    return metaBean().power().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AsymmetricPoweredPayoffStyle}.
   */
  public static class Meta extends PayoffStyle.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code power} property.
     */
    private final MetaProperty<Double> _power = DirectMetaProperty.ofReadWrite(
        this, "power", AsymmetricPoweredPayoffStyle.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "power");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 106858757:  // power
          return _power;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends AsymmetricPoweredPayoffStyle> builder() {
      return new DirectBeanBuilder<AsymmetricPoweredPayoffStyle>(new AsymmetricPoweredPayoffStyle());
    }

    @Override
    public Class<? extends AsymmetricPoweredPayoffStyle> beanType() {
      return AsymmetricPoweredPayoffStyle.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code power} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> power() {
      return _power;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
