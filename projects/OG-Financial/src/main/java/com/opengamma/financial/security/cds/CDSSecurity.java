/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cds;

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
import org.threeten.bp.ZonedDateTime;

import com.opengamma.financial.convention.StubType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.util.money.Currency;

/**
 * CDS Security object
 * 
 * @author Martin Traverse, Niels Stchedroff (Riskcare)
 */
@BeanDefinition
public class CDSSecurity extends FinancialSecurity {
  
  /** Serialization version */
  private static final long serialVersionUID = 1L;
  
  /** Security type for CDS securities */
  public static final String SECURITY_TYPE = "CDS";
  
  /** Notional */
  @PropertyDefinition
  private double _notional;
  
  /** CDS spread (premium rate) */
  @PropertyDefinition
  private double _spread;
  
  /** Recovery rate of underlying asset */
  @PropertyDefinition
  private double _recoveryRate;
  
  /** Currency of the CDS */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  
  /** protection start date */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _startDate;
  
  /** Maturity date */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _maturity;
  
  /** Premium payment frequency */
  @PropertyDefinition(validate = "notNull")
  private Frequency _premiumFrequency;
  
  /**
   * The day count convention. ISDA uses ACT/360.
   */
  @PropertyDefinition(validate = "notNull")
  private DayCount _dayCount;
  
  /**
   * The business day convention.
   */
  @PropertyDefinition(validate = "notNull")
  private BusinessDayConvention _businessDayConvention;
  
  /**
   * Stub type for the premium payments
   */
  @PropertyDefinition(validate = "notNull")
  private StubType _stubType;
  
  /**
   * Number of business days for settlement
   */
  @PropertyDefinition
  private int _settlementDays;
  
  /**
   * The name of the underlying issuer
   */
  @PropertyDefinition(validate = "notNull")
  private String _underlyingIssuer;
  
  /**
   * The currency of the underlying issue
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _underlyingCurrency;
  
  /**
   * The seniority of the underlying issue
   */
  @PropertyDefinition(validate = "notNull")
  private String _underlyingSeniority;
  
  /**
   * The restructuring clause
   */
  @PropertyDefinition(validate = "notNull")
  private String _restructuringClause;
  
  
  CDSSecurity() {
    super(SECURITY_TYPE);
  }

  public CDSSecurity(double notional, double recoveryRate, double spread, Currency currency,
      ZonedDateTime maturity, ZonedDateTime startDate, Frequency premiumFrequency,
      DayCount dayCount, BusinessDayConvention businessDayConvention, StubType stubType, int settlementDays,
      String underlyingIssuer, Currency underlyingCurrency, String underlyingSeniority, String restructuringClause) {
    
    super(SECURITY_TYPE);
    setNotional(notional);
    setRecoveryRate(recoveryRate);
    setSpread(spread);
    setCurrency(currency);
    setMaturity(maturity);
    setStartDate(startDate);
    setPremiumFrequency(premiumFrequency);
    setDayCount(dayCount);
    setBusinessDayConvention(businessDayConvention);
    setStubType(stubType);
    setSettlementDays(settlementDays);
    setUnderlyingIssuer(underlyingIssuer);
    setUnderlyingCurrency(underlyingCurrency);
    setUnderlyingSeniority(underlyingSeniority);
    setRestructuringClause(restructuringClause);
  }

  @Override
  public <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CDSSecurity}.
   * @return the meta-bean, not null
   */
  public static CDSSecurity.Meta meta() {
    return CDSSecurity.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(CDSSecurity.Meta.INSTANCE);
  }

  @Override
  public CDSSecurity.Meta metaBean() {
    return CDSSecurity.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 1585636160:  // notional
        return getNotional();
      case -895684237:  // spread
        return getSpread();
      case 2002873877:  // recoveryRate
        return getRecoveryRate();
      case 575402001:  // currency
        return getCurrency();
      case -2129778896:  // startDate
        return getStartDate();
      case 313843601:  // maturity
        return getMaturity();
      case 146671813:  // premiumFrequency
        return getPremiumFrequency();
      case 1905311443:  // dayCount
        return getDayCount();
      case -1002835891:  // businessDayConvention
        return getBusinessDayConvention();
      case 1873675528:  // stubType
        return getStubType();
      case -295948000:  // settlementDays
        return getSettlementDays();
      case -81466250:  // underlyingIssuer
        return getUnderlyingIssuer();
      case -1102975346:  // underlyingCurrency
        return getUnderlyingCurrency();
      case -305508959:  // underlyingSeniority
        return getUnderlyingSeniority();
      case -1774904020:  // restructuringClause
        return getRestructuringClause();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 1585636160:  // notional
        setNotional((Double) newValue);
        return;
      case -895684237:  // spread
        setSpread((Double) newValue);
        return;
      case 2002873877:  // recoveryRate
        setRecoveryRate((Double) newValue);
        return;
      case 575402001:  // currency
        setCurrency((Currency) newValue);
        return;
      case -2129778896:  // startDate
        setStartDate((ZonedDateTime) newValue);
        return;
      case 313843601:  // maturity
        setMaturity((ZonedDateTime) newValue);
        return;
      case 146671813:  // premiumFrequency
        setPremiumFrequency((Frequency) newValue);
        return;
      case 1905311443:  // dayCount
        setDayCount((DayCount) newValue);
        return;
      case -1002835891:  // businessDayConvention
        setBusinessDayConvention((BusinessDayConvention) newValue);
        return;
      case 1873675528:  // stubType
        setStubType((StubType) newValue);
        return;
      case -295948000:  // settlementDays
        setSettlementDays((Integer) newValue);
        return;
      case -81466250:  // underlyingIssuer
        setUnderlyingIssuer((String) newValue);
        return;
      case -1102975346:  // underlyingCurrency
        setUnderlyingCurrency((Currency) newValue);
        return;
      case -305508959:  // underlyingSeniority
        setUnderlyingSeniority((String) newValue);
        return;
      case -1774904020:  // restructuringClause
        setRestructuringClause((String) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_currency, "currency");
    JodaBeanUtils.notNull(_startDate, "startDate");
    JodaBeanUtils.notNull(_maturity, "maturity");
    JodaBeanUtils.notNull(_premiumFrequency, "premiumFrequency");
    JodaBeanUtils.notNull(_dayCount, "dayCount");
    JodaBeanUtils.notNull(_businessDayConvention, "businessDayConvention");
    JodaBeanUtils.notNull(_stubType, "stubType");
    JodaBeanUtils.notNull(_underlyingIssuer, "underlyingIssuer");
    JodaBeanUtils.notNull(_underlyingCurrency, "underlyingCurrency");
    JodaBeanUtils.notNull(_underlyingSeniority, "underlyingSeniority");
    JodaBeanUtils.notNull(_restructuringClause, "restructuringClause");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CDSSecurity other = (CDSSecurity) obj;
      return JodaBeanUtils.equal(getNotional(), other.getNotional()) &&
          JodaBeanUtils.equal(getSpread(), other.getSpread()) &&
          JodaBeanUtils.equal(getRecoveryRate(), other.getRecoveryRate()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getStartDate(), other.getStartDate()) &&
          JodaBeanUtils.equal(getMaturity(), other.getMaturity()) &&
          JodaBeanUtils.equal(getPremiumFrequency(), other.getPremiumFrequency()) &&
          JodaBeanUtils.equal(getDayCount(), other.getDayCount()) &&
          JodaBeanUtils.equal(getBusinessDayConvention(), other.getBusinessDayConvention()) &&
          JodaBeanUtils.equal(getStubType(), other.getStubType()) &&
          JodaBeanUtils.equal(getSettlementDays(), other.getSettlementDays()) &&
          JodaBeanUtils.equal(getUnderlyingIssuer(), other.getUnderlyingIssuer()) &&
          JodaBeanUtils.equal(getUnderlyingCurrency(), other.getUnderlyingCurrency()) &&
          JodaBeanUtils.equal(getUnderlyingSeniority(), other.getUnderlyingSeniority()) &&
          JodaBeanUtils.equal(getRestructuringClause(), other.getRestructuringClause()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getNotional());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSpread());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRecoveryRate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStartDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getMaturity());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPremiumFrequency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getDayCount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getBusinessDayConvention());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStubType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSettlementDays());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingIssuer());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingSeniority());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRestructuringClause());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets notional
   * @return the value of the property
   */
  public double getNotional() {
    return _notional;
  }

  /**
   * Sets notional
   * @param notional  the new value of the property
   */
  public void setNotional(double notional) {
    this._notional = notional;
  }

  /**
   * Gets the the {@code notional} property.
   * @return the property, not null
   */
  public final Property<Double> notional() {
    return metaBean().notional().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets cDS spread (premium rate)
   * @return the value of the property
   */
  public double getSpread() {
    return _spread;
  }

  /**
   * Sets cDS spread (premium rate)
   * @param spread  the new value of the property
   */
  public void setSpread(double spread) {
    this._spread = spread;
  }

  /**
   * Gets the the {@code spread} property.
   * @return the property, not null
   */
  public final Property<Double> spread() {
    return metaBean().spread().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets recovery rate of underlying asset
   * @return the value of the property
   */
  public double getRecoveryRate() {
    return _recoveryRate;
  }

  /**
   * Sets recovery rate of underlying asset
   * @param recoveryRate  the new value of the property
   */
  public void setRecoveryRate(double recoveryRate) {
    this._recoveryRate = recoveryRate;
  }

  /**
   * Gets the the {@code recoveryRate} property.
   * @return the property, not null
   */
  public final Property<Double> recoveryRate() {
    return metaBean().recoveryRate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets currency of the CDS
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets currency of the CDS
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets protection start date
   * @return the value of the property, not null
   */
  public ZonedDateTime getStartDate() {
    return _startDate;
  }

  /**
   * Sets protection start date
   * @param startDate  the new value of the property, not null
   */
  public void setStartDate(ZonedDateTime startDate) {
    JodaBeanUtils.notNull(startDate, "startDate");
    this._startDate = startDate;
  }

  /**
   * Gets the the {@code startDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> startDate() {
    return metaBean().startDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets maturity date
   * @return the value of the property, not null
   */
  public ZonedDateTime getMaturity() {
    return _maturity;
  }

  /**
   * Sets maturity date
   * @param maturity  the new value of the property, not null
   */
  public void setMaturity(ZonedDateTime maturity) {
    JodaBeanUtils.notNull(maturity, "maturity");
    this._maturity = maturity;
  }

  /**
   * Gets the the {@code maturity} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> maturity() {
    return metaBean().maturity().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets premium payment frequency
   * @return the value of the property, not null
   */
  public Frequency getPremiumFrequency() {
    return _premiumFrequency;
  }

  /**
   * Sets premium payment frequency
   * @param premiumFrequency  the new value of the property, not null
   */
  public void setPremiumFrequency(Frequency premiumFrequency) {
    JodaBeanUtils.notNull(premiumFrequency, "premiumFrequency");
    this._premiumFrequency = premiumFrequency;
  }

  /**
   * Gets the the {@code premiumFrequency} property.
   * @return the property, not null
   */
  public final Property<Frequency> premiumFrequency() {
    return metaBean().premiumFrequency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count convention. ISDA uses ACT/360.
   * @return the value of the property, not null
   */
  public DayCount getDayCount() {
    return _dayCount;
  }

  /**
   * Sets the day count convention. ISDA uses ACT/360.
   * @param dayCount  the new value of the property, not null
   */
  public void setDayCount(DayCount dayCount) {
    JodaBeanUtils.notNull(dayCount, "dayCount");
    this._dayCount = dayCount;
  }

  /**
   * Gets the the {@code dayCount} property.
   * @return the property, not null
   */
  public final Property<DayCount> dayCount() {
    return metaBean().dayCount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the business day convention.
   * @return the value of the property, not null
   */
  public BusinessDayConvention getBusinessDayConvention() {
    return _businessDayConvention;
  }

  /**
   * Sets the business day convention.
   * @param businessDayConvention  the new value of the property, not null
   */
  public void setBusinessDayConvention(BusinessDayConvention businessDayConvention) {
    JodaBeanUtils.notNull(businessDayConvention, "businessDayConvention");
    this._businessDayConvention = businessDayConvention;
  }

  /**
   * Gets the the {@code businessDayConvention} property.
   * @return the property, not null
   */
  public final Property<BusinessDayConvention> businessDayConvention() {
    return metaBean().businessDayConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets stub type for the premium payments
   * @return the value of the property, not null
   */
  public StubType getStubType() {
    return _stubType;
  }

  /**
   * Sets stub type for the premium payments
   * @param stubType  the new value of the property, not null
   */
  public void setStubType(StubType stubType) {
    JodaBeanUtils.notNull(stubType, "stubType");
    this._stubType = stubType;
  }

  /**
   * Gets the the {@code stubType} property.
   * @return the property, not null
   */
  public final Property<StubType> stubType() {
    return metaBean().stubType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets number of business days for settlement
   * @return the value of the property
   */
  public int getSettlementDays() {
    return _settlementDays;
  }

  /**
   * Sets number of business days for settlement
   * @param settlementDays  the new value of the property
   */
  public void setSettlementDays(int settlementDays) {
    this._settlementDays = settlementDays;
  }

  /**
   * Gets the the {@code settlementDays} property.
   * @return the property, not null
   */
  public final Property<Integer> settlementDays() {
    return metaBean().settlementDays().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the underlying issuer
   * @return the value of the property, not null
   */
  public String getUnderlyingIssuer() {
    return _underlyingIssuer;
  }

  /**
   * Sets the name of the underlying issuer
   * @param underlyingIssuer  the new value of the property, not null
   */
  public void setUnderlyingIssuer(String underlyingIssuer) {
    JodaBeanUtils.notNull(underlyingIssuer, "underlyingIssuer");
    this._underlyingIssuer = underlyingIssuer;
  }

  /**
   * Gets the the {@code underlyingIssuer} property.
   * @return the property, not null
   */
  public final Property<String> underlyingIssuer() {
    return metaBean().underlyingIssuer().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency of the underlying issue
   * @return the value of the property, not null
   */
  public Currency getUnderlyingCurrency() {
    return _underlyingCurrency;
  }

  /**
   * Sets the currency of the underlying issue
   * @param underlyingCurrency  the new value of the property, not null
   */
  public void setUnderlyingCurrency(Currency underlyingCurrency) {
    JodaBeanUtils.notNull(underlyingCurrency, "underlyingCurrency");
    this._underlyingCurrency = underlyingCurrency;
  }

  /**
   * Gets the the {@code underlyingCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> underlyingCurrency() {
    return metaBean().underlyingCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the seniority of the underlying issue
   * @return the value of the property, not null
   */
  public String getUnderlyingSeniority() {
    return _underlyingSeniority;
  }

  /**
   * Sets the seniority of the underlying issue
   * @param underlyingSeniority  the new value of the property, not null
   */
  public void setUnderlyingSeniority(String underlyingSeniority) {
    JodaBeanUtils.notNull(underlyingSeniority, "underlyingSeniority");
    this._underlyingSeniority = underlyingSeniority;
  }

  /**
   * Gets the the {@code underlyingSeniority} property.
   * @return the property, not null
   */
  public final Property<String> underlyingSeniority() {
    return metaBean().underlyingSeniority().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the restructuring clause
   * @return the value of the property, not null
   */
  public String getRestructuringClause() {
    return _restructuringClause;
  }

  /**
   * Sets the restructuring clause
   * @param restructuringClause  the new value of the property, not null
   */
  public void setRestructuringClause(String restructuringClause) {
    JodaBeanUtils.notNull(restructuringClause, "restructuringClause");
    this._restructuringClause = restructuringClause;
  }

  /**
   * Gets the the {@code restructuringClause} property.
   * @return the property, not null
   */
  public final Property<String> restructuringClause() {
    return metaBean().restructuringClause().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CDSSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<Double> _notional = DirectMetaProperty.ofReadWrite(
        this, "notional", CDSSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code spread} property.
     */
    private final MetaProperty<Double> _spread = DirectMetaProperty.ofReadWrite(
        this, "spread", CDSSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code recoveryRate} property.
     */
    private final MetaProperty<Double> _recoveryRate = DirectMetaProperty.ofReadWrite(
        this, "recoveryRate", CDSSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", CDSSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code startDate} property.
     */
    private final MetaProperty<ZonedDateTime> _startDate = DirectMetaProperty.ofReadWrite(
        this, "startDate", CDSSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code maturity} property.
     */
    private final MetaProperty<ZonedDateTime> _maturity = DirectMetaProperty.ofReadWrite(
        this, "maturity", CDSSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code premiumFrequency} property.
     */
    private final MetaProperty<Frequency> _premiumFrequency = DirectMetaProperty.ofReadWrite(
        this, "premiumFrequency", CDSSecurity.class, Frequency.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> _dayCount = DirectMetaProperty.ofReadWrite(
        this, "dayCount", CDSSecurity.class, DayCount.class);
    /**
     * The meta-property for the {@code businessDayConvention} property.
     */
    private final MetaProperty<BusinessDayConvention> _businessDayConvention = DirectMetaProperty.ofReadWrite(
        this, "businessDayConvention", CDSSecurity.class, BusinessDayConvention.class);
    /**
     * The meta-property for the {@code stubType} property.
     */
    private final MetaProperty<StubType> _stubType = DirectMetaProperty.ofReadWrite(
        this, "stubType", CDSSecurity.class, StubType.class);
    /**
     * The meta-property for the {@code settlementDays} property.
     */
    private final MetaProperty<Integer> _settlementDays = DirectMetaProperty.ofReadWrite(
        this, "settlementDays", CDSSecurity.class, Integer.TYPE);
    /**
     * The meta-property for the {@code underlyingIssuer} property.
     */
    private final MetaProperty<String> _underlyingIssuer = DirectMetaProperty.ofReadWrite(
        this, "underlyingIssuer", CDSSecurity.class, String.class);
    /**
     * The meta-property for the {@code underlyingCurrency} property.
     */
    private final MetaProperty<Currency> _underlyingCurrency = DirectMetaProperty.ofReadWrite(
        this, "underlyingCurrency", CDSSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code underlyingSeniority} property.
     */
    private final MetaProperty<String> _underlyingSeniority = DirectMetaProperty.ofReadWrite(
        this, "underlyingSeniority", CDSSecurity.class, String.class);
    /**
     * The meta-property for the {@code restructuringClause} property.
     */
    private final MetaProperty<String> _restructuringClause = DirectMetaProperty.ofReadWrite(
        this, "restructuringClause", CDSSecurity.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "notional",
        "spread",
        "recoveryRate",
        "currency",
        "startDate",
        "maturity",
        "premiumFrequency",
        "dayCount",
        "businessDayConvention",
        "stubType",
        "settlementDays",
        "underlyingIssuer",
        "underlyingCurrency",
        "underlyingSeniority",
        "restructuringClause");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1585636160:  // notional
          return _notional;
        case -895684237:  // spread
          return _spread;
        case 2002873877:  // recoveryRate
          return _recoveryRate;
        case 575402001:  // currency
          return _currency;
        case -2129778896:  // startDate
          return _startDate;
        case 313843601:  // maturity
          return _maturity;
        case 146671813:  // premiumFrequency
          return _premiumFrequency;
        case 1905311443:  // dayCount
          return _dayCount;
        case -1002835891:  // businessDayConvention
          return _businessDayConvention;
        case 1873675528:  // stubType
          return _stubType;
        case -295948000:  // settlementDays
          return _settlementDays;
        case -81466250:  // underlyingIssuer
          return _underlyingIssuer;
        case -1102975346:  // underlyingCurrency
          return _underlyingCurrency;
        case -305508959:  // underlyingSeniority
          return _underlyingSeniority;
        case -1774904020:  // restructuringClause
          return _restructuringClause;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CDSSecurity> builder() {
      return new DirectBeanBuilder<CDSSecurity>(new CDSSecurity());
    }

    @Override
    public Class<? extends CDSSecurity> beanType() {
      return CDSSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> notional() {
      return _notional;
    }

    /**
     * The meta-property for the {@code spread} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> spread() {
      return _spread;
    }

    /**
     * The meta-property for the {@code recoveryRate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> recoveryRate() {
      return _recoveryRate;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code startDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> startDate() {
      return _startDate;
    }

    /**
     * The meta-property for the {@code maturity} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> maturity() {
      return _maturity;
    }

    /**
     * The meta-property for the {@code premiumFrequency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Frequency> premiumFrequency() {
      return _premiumFrequency;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DayCount> dayCount() {
      return _dayCount;
    }

    /**
     * The meta-property for the {@code businessDayConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BusinessDayConvention> businessDayConvention() {
      return _businessDayConvention;
    }

    /**
     * The meta-property for the {@code stubType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<StubType> stubType() {
      return _stubType;
    }

    /**
     * The meta-property for the {@code settlementDays} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> settlementDays() {
      return _settlementDays;
    }

    /**
     * The meta-property for the {@code underlyingIssuer} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> underlyingIssuer() {
      return _underlyingIssuer;
    }

    /**
     * The meta-property for the {@code underlyingCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> underlyingCurrency() {
      return _underlyingCurrency;
    }

    /**
     * The meta-property for the {@code underlyingSeniority} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> underlyingSeniority() {
      return _underlyingSeniority;
    }

    /**
     * The meta-property for the {@code restructuringClause} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> restructuringClause() {
      return _restructuringClause;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
