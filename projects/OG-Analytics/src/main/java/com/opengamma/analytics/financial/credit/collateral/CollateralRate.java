/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.collateral;

/**
 * Enumerate the rates at which a return is earnt on posted collateral
 */
public enum CollateralRate {

  /**
   * Euro Overnight Index Average rate
   */
  EONIA,
  /**
   * Federal Reserve funds rate
   */
  FED,
  /**
   * No return earnt
   */
  NONE,
  /**
   * Other (user will have to specify)
   */
  OTHER;
}
