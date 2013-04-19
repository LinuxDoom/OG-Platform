/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model;

/**
 * Contains property names and values that are used to label calculations.
 */
public final class CalculationPropertyNamesAndValues {

  /** Property name for the model type */
  public static final String PROPERTY_MODEL_TYPE = "ModelType";

  // Values for ValuePropertyNames.CALCULATION_METHOD
  /** Black calculations */
  public static final String BLACK_METHOD = "BlackMethod";
  /** Black model without an entire Volatility Surface, hence no smile */
  public static final String BLACK_BASIC_METHOD = "BlackBasicMethod";
  /** Black model without an entire Volatility Surface. Implies volatility from listed option security's market value */
  public static final String BLACK_LISTED_METHOD = "BlackListedMethod";
  /** The Barone-Adesi Whaley approximation for American options */
  public static final String BAW_METHOD = "BaroneAdesiWhaleyMethod";
  /** The Bjerksund-Stensland approximation for American options */
  public static final String BJERKSUND_STENSLAND_METHOD = "BjerksundStenslandMethod";
  /** The so-called PDE method computes prices for American and European options under the BlackScholesMerton model*/
  public static final String PDE_METHOD = "PDE";
  /** Pricing from marked / listed price */
  public static final String MARK_TO_MARKET_METHOD = "MarkToMarket";
  /** Pricing cash-flow instruments using discounting */
  public static final String DISCOUNTING = "Discounting";
  /** Pricing FX forwards using a forward points curve */
  public static final String FORWARD_POINTS = "ForwardPoints";

  //Values for PROPERTY_MODEL_TYPE
  /** Analytic */
  public static final String ANALYTIC = "Analytic";
  /** PDE */
  public static final String PDE = "PDE";

  private CalculationPropertyNamesAndValues() {
  }

}
