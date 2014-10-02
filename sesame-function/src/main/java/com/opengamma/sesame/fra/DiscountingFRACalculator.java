/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.fra;

import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.calculator.discounting.PV01CurveParametersCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.ParRateDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueCurveSensitivityDiscountingCalculator;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueDiscountingCalculator;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.util.amount.ReferenceAmount;
import com.opengamma.financial.analytics.conversion.FRASecurityConverter;
import com.opengamma.financial.analytics.conversion.FixedIncomeConverterDataProvider;
import com.opengamma.financial.analytics.timeseries.HistoricalTimeSeriesBundle;
import com.opengamma.financial.security.fra.FRASecurity;
import com.opengamma.financial.security.fra.ForwardRateAgreementSecurity;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.MultipleCurrencyAmount;
import com.opengamma.util.result.Result;
import com.opengamma.util.tuple.Pair;

/**
 * Calculator for Discounting FRAs.
 */
public class DiscountingFRACalculator implements FRACalculator {

  /**
   * Calculator for present value.
   */
  private static final PresentValueDiscountingCalculator PVDC = PresentValueDiscountingCalculator.getInstance();

  /**
   * Calculator for par rate.
   */
  private static final ParRateDiscountingCalculator PRDC = ParRateDiscountingCalculator.getInstance();

  /**
   * Calculator for PV01
   */
  private static final PV01CurveParametersCalculator<MulticurveProviderInterface> PV01C =
      new PV01CurveParametersCalculator<>(PresentValueCurveSensitivityDiscountingCalculator.getInstance());

  /**
   * Derivative form of the security.
   */
  private final InstrumentDerivative _derivative;

  /**
   * The multicurve bundle.
   */
  private final MulticurveProviderDiscount _bundle;

  /**
   * Creates a calculator for a FRA.
   *
   * @param security the fra to calculate values for, not null
   * @param bundle the multicurve bundle, including the curves, not null
   * @param fraConverter converter for transforming a fra into its InstrumentDefinition form, not null
   * @param valuationTime the ZonedDateTime, not null
   */
  public DiscountingFRACalculator(FRASecurity security,
                                  MulticurveProviderDiscount bundle,
                                  FRASecurityConverter fraConverter,
                                  ZonedDateTime valuationTime) {
    ArgumentChecker.notNull(security, "security");
    ArgumentChecker.notNull(fraConverter, "fraConverter");
    ArgumentChecker.notNull(valuationTime, "valuationTime");
    _derivative = createInstrumentDerivative(security, fraConverter, valuationTime);
    _bundle = ArgumentChecker.notNull(bundle, "bundle");
  }

  /**
   * Creates a calculator for a FRA.
   *
   * @param security the fra to calculate values for, not null
   * @param bundle the multicurve bundle, including the curves, not null
   * @param fraConverter converter for transforming a fra into its InstrumentDefinition form, not null
   * @param valuationTime the ZonedDateTime, not null
   * @param definitionConverter converter for transforming the instrumentDefinition into the Derivative, not null
   * @param fixings the HistoricalTimeSeriesBundle, a collection of historical time-series objects
   */
  public DiscountingFRACalculator(ForwardRateAgreementSecurity security,
                                  MulticurveProviderDiscount bundle,
                                  FRASecurityConverter fraConverter,
                                  ZonedDateTime valuationTime,
                                  FixedIncomeConverterDataProvider definitionConverter,
                                  HistoricalTimeSeriesBundle fixings) {
    ArgumentChecker.notNull(security, "security");
    ArgumentChecker.notNull(fraConverter, "fraConverter");
    ArgumentChecker.notNull(valuationTime, "valuationTime");
    ArgumentChecker.notNull(definitionConverter, "definitionConverter");
    ArgumentChecker.notNull(fixings, "fixings");
    _derivative = createInstrumentDerivative(security, fraConverter, valuationTime, definitionConverter, fixings);
    _bundle = ArgumentChecker.notNull(bundle, "bundle");
  }

  @Override
  public Result<MultipleCurrencyAmount> calculatePV() {
    return Result.success(calculateResult(PVDC));
  }
  
  @Override
  public Result<MultipleCurrencyAmount> calculatePv(MulticurveProviderInterface bundle) {
    ArgumentChecker.notNull(bundle, "curve bundle");
    return Result.success(_derivative.accept(PVDC, bundle));
  }

  @Override
  public Result<Double> calculateRate() {
    return Result.success(calculateResult(PRDC));
  }

  private <T> T calculateResult(InstrumentDerivativeVisitor<MulticurveProviderInterface, T> calculator) {
    return _derivative.accept(calculator, _bundle);
  }

  private InstrumentDerivative createInstrumentDerivative(FRASecurity security,
                                                          FRASecurityConverter fraConverter,
                                                          ZonedDateTime valuationTime) {
    InstrumentDefinition<?> definition = security.accept(fraConverter);
    return definition.toDerivative(valuationTime);
  }
  
  private InstrumentDerivative createInstrumentDerivative(ForwardRateAgreementSecurity security,
      FRASecurityConverter fraConverter,
      ZonedDateTime valuationTime,
      FixedIncomeConverterDataProvider definitionConverter,
      HistoricalTimeSeriesBundle fixings) {
    InstrumentDefinition<?> definition = security.accept(fraConverter);
    return definitionConverter.convert(security, definition, valuationTime, fixings);
  }

  @Override
  public Result<ReferenceAmount<Pair<String, Currency>>> calculatePV01() {
    return Result.success(calculateResult(PV01C));
  }

}
