/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.option.pricing.tree;

import org.apache.commons.lang.NotImplementedException;

/**
 * 
 */
public class AmericanSingleBarrierOptionFunctionProvider extends OptionFunctionProvider1D {
  /**
   * Use these strings to specify barrier option type. Down-And-In and Up-And-In MUST be computed via in-out parity
   */
  public static enum BarrierTypes {
    /**
     * Down-and-out option
     */
    DownAndOut,
    /**
     * Up-and-out option
     */
    UpAndOut,
    /**
     * Down-and-in option, not implemented
     */
    DownAndIn,
    /**
     * Up-and-in option, not implemented
     */
    UpAndIn
  }

  private double _barrier;
  private CrossBarrierChecker _checker;

  /**
   * @param strike Strike price
   * @param steps Number of steps
   * @param isCall True if call, false if put
   * @param barrier Barrier price
   * @param typeName {@link BarrierTypes}, DownAndOut or UpAndOut
   */
  public AmericanSingleBarrierOptionFunctionProvider(final double strike, final int steps, final boolean isCall, final double barrier, final BarrierTypes typeName) {
    super(strike, steps, isCall);
    _barrier = barrier;

    switch (typeName) {
      case DownAndOut:
        _checker = new CrossLowerBarrier();
        break;
      case UpAndOut:
        _checker = new CrossUpperBarrier();
        break;
      case DownAndIn:
        throw new NotImplementedException("Down-and-in should be computed by using down-and-out and in-out parity");
      case UpAndIn:
        throw new NotImplementedException("Up-and-in should be computed by using up-and-out and in-out parity");
    }

  }

  @Override
  public double[] getPayoffAtExpiry(final double assetPrice, final double upOverDown) {
    final double strike = getStrike();
    final int nStepsP = getNumberOfSteps() + 1;
    final double sign = getSign();

    final double[] values = new double[nStepsP];
    double priceTmp = assetPrice;
    for (int i = 0; i < nStepsP; ++i) {
      values[i] = _checker.checkOut(priceTmp) ? 0. : Math.max(sign * (priceTmp - strike), 0);
      priceTmp *= upOverDown;
    }
    return values;
  }

  @Override
  public double[] getNextOptionValues(final double discount, final double upProbability, final double downProbability, final double[] values, final double baseAssetPrice, final double downFactor,
      final double upOverDown, final int steps) {
    final double strike = getStrike();
    final double sign = getSign();
    final int nStepsP = steps + 1;

    final double[] res = new double[nStepsP];
    double assetPrice = baseAssetPrice * Math.pow(downFactor, steps);
    for (int j = 0; j < nStepsP; ++j) {
      res[j] = _checker.checkOut(assetPrice) ? 0. : Math.max(discount * (upProbability * values[j + 1] + downProbability * values[j]), sign * (assetPrice - strike));
      assetPrice *= upOverDown;
    }
    return res;
  }

  private abstract class CrossBarrierChecker {
    public abstract boolean checkOut(final double priceTmp);
  }

  private class CrossLowerBarrier extends CrossBarrierChecker {
    @Override
    public boolean checkOut(final double priceTmp) {
      return priceTmp <= _barrier;
    }
  }

  private class CrossUpperBarrier extends CrossBarrierChecker {
    @Override
    public boolean checkOut(final double priceTmp) {
      return priceTmp >= _barrier;
    }
  }

}