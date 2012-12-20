/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.calculator;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opengamma.analytics.financial.curve.sensitivity.AbstractParameterSensitivityCalculator;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.YieldCurveBundle;
import com.opengamma.analytics.math.matrix.ColtMatrixAlgebra;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.analytics.math.matrix.DoubleMatrix2D;
import com.opengamma.analytics.math.matrix.MatrixAlgebra;
import com.opengamma.lambdava.tuple.DoublesPair;

/**
 * Calculator of the sensitivity to the market quotes of instruments used to build the curves.
 */
public final class MarketQuoteSensitivityCalculator {

  /**
   * The matrix algebra used for matrix inversion.
   */
  private static final MatrixAlgebra MATRIX_ALGEBRA = new ColtMatrixAlgebra(); //TODO make this a parameter  //ColtMatrixAlgebra()
  /**
   * The parameter sensitivity calculator. The parameters are the parameters used to described the curve.
   */
  private final AbstractParameterSensitivityCalculator _parameterSensitivityCalculator;

  /**
   * The constructor.
   * @param parameterSensitivityCalculator The parameter sensitivity calculator.
   */
  public MarketQuoteSensitivityCalculator(final AbstractParameterSensitivityCalculator parameterSensitivityCalculator) {
    _parameterSensitivityCalculator = parameterSensitivityCalculator;
  }

  /**
   * Compute the market quote sensitivity from the parameter sensitivity and the inverse Jacobian matrix.
   * @param parameterSensitivity The parameter sensitivity.
   * @param inverseJacobian The inverse Jacobian matrix (derivative of the curve parameters with respect to the market quotes).
   * @return The market quote sensitivity.
   */
  public DoubleMatrix1D fromParameterSensitivityInverseJacobian(final DoubleMatrix1D parameterSensitivity, final DoubleMatrix2D inverseJacobian) {
    return (DoubleMatrix1D) MATRIX_ALGEBRA.multiply(parameterSensitivity, inverseJacobian);
  }

  /**
   * Compute the market quote sensitivity from an instrument and the Jacobian matrix.
   * @param instrument The instrument. Not null.
   * @param fixedCurves The fixed curves names (for which the parameter sensitivity are not computed even if they are necessary for the instrument pricing).
   * The curve in the list may or may not be in the bundle. Not null.
   * @param bundle The curve bundle with all the curves with respect to which the sensitivity should be computed. Not null.
   * @param inverseJacobian The inverse Jacobian matrix (derivative of the curve parameters with respect to the market quotes).
   * @return The market quote sensitivity.
   */
  public DoubleMatrix1D fromInstrumentInverseJacobian(final InstrumentDerivative instrument, final Set<String> fixedCurves, final YieldCurveBundle bundle, final DoubleMatrix2D inverseJacobian) {
    final DoubleMatrix1D parameterSensitivity = _parameterSensitivityCalculator.calculateSensitivity(instrument, fixedCurves, bundle);
    return fromParameterSensitivityInverseJacobian(parameterSensitivity, inverseJacobian);
  }

  /**
   * Calculate the instrument sensitivity from the yield sensitivity, the jacobian matrix and the coupon sensitivity.
   * @param curveSensitivities The sensitivity to points of the yield curve.
   * @param curves The curve bundle.
   * @param couponSensitivity The sensitivity
   * @param jacobian The present value coupon sensitivity.
   * @return The instrument quote/rate sensitivity.
   */
  public DoubleMatrix1D calculateFromPresentValue(final Map<String, List<DoublesPair>> curveSensitivities, final YieldCurveBundle curves, final DoubleMatrix1D couponSensitivity,
      final DoubleMatrix2D jacobian) {
    final DoubleArrayList resultList = new DoubleArrayList();
    for (final String curveName : curves.getAllNames()) {
      final DoubleMatrix1D nodeSensitivity = new DoubleMatrix1D(
          (_parameterSensitivityCalculator.pointToParameterSensitivity(curveSensitivities.get(curveName), curves.getCurve(curveName))).toArray(new Double[0]));
      final int n = nodeSensitivity.getNumberOfElements();
      final DoubleMatrix2D inverseJacobian = MATRIX_ALGEBRA.getInverse(jacobian);
      for (int i = 0; i < n; i++) {
        double sum = 0;
        for (int j = 0; j < n; j++) {
          sum += -couponSensitivity.getEntry(i) * inverseJacobian.getEntry(j, i) * nodeSensitivity.getEntry(j);
        }
        resultList.add(sum);
      }
    }
    return new DoubleMatrix1D(resultList.toDoubleArray());
  }

  public DoubleMatrix1D calculateFromParRate(final Map<String, List<DoublesPair>> curveSensitivities, final YieldCurveBundle interpolatedCurves, final DoubleMatrix2D jacobian) {
    final DoubleArrayList resultList = new DoubleArrayList();
    for (final String curveName : interpolatedCurves.getAllNames()) {
      final DoubleMatrix1D nodeSensitivity = new DoubleMatrix1D(
          (_parameterSensitivityCalculator.pointToParameterSensitivity(curveSensitivities.get(curveName), interpolatedCurves.getCurve(curveName))).toArray(new Double[0]));
      final int n = nodeSensitivity.getNumberOfElements();
      final DoubleMatrix2D inverseJacobian = MATRIX_ALGEBRA.getInverse(jacobian);
      for (int i = 0; i < n; i++) {
        double sum = 0;
        for (int j = 0; j < n; j++) {
          sum += inverseJacobian.getEntry(j, i) * nodeSensitivity.getEntry(j);
        }
        resultList.add(sum);
      }
    }
    return new DoubleMatrix1D(resultList.toDoubleArray());
  }

  public DoubleMatrix1D calculateFromSimpleInterpolatedCurve(final Map<String, List<DoublesPair>> curveSensitivities, final YieldCurveBundle interpolatedCurves) {
    final DoubleArrayList resultList = new DoubleArrayList();
    for (final String curveName : interpolatedCurves.getAllNames()) {
      final DoubleMatrix1D nodeSensitivity = new DoubleMatrix1D(
          (_parameterSensitivityCalculator.pointToParameterSensitivity(curveSensitivities.get(curveName), interpolatedCurves.getCurve(curveName))).toArray(new Double[0]));
      final int n = nodeSensitivity.getNumberOfElements();
      for (int i = 0; i < n; i++) {
        double sum = 0;
        for (int j = 0; j < n; j++) {
          sum += nodeSensitivity.getEntry(j);
        }
        resultList.add(sum);
      }
    }
    return new DoubleMatrix1D(resultList.toDoubleArray());
  }

}
