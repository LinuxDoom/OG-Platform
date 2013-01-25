/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import static org.testng.AssertJUnit.assertEquals;

import javax.time.calendar.LocalDate;

import org.testng.annotations.Test;

import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;

public class GoldFutureExpiryCalculatorTest {
  private static final GoldFutureExpiryCalculator EXPIRY_CALC = GoldFutureExpiryCalculator.getInstance();

  static final Calendar WEEKEND_CALENDAR = new MondayToFridayCalendar("weekday");

  private static final LocalDate DATE = LocalDate.of(2013, 1, 1);

  @Test
  public void getExpiryDate() {
    assertEquals(LocalDate.of(2013, 2, 26), EXPIRY_CALC.getExpiryDate(1, DATE, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 27), EXPIRY_CALC.getExpiryDate(2, DATE, WEEKEND_CALENDAR)); //Good friday not in weekday calendar
    assertEquals(LocalDate.of(2013, 4, 26), EXPIRY_CALC.getExpiryDate(3, DATE, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 26), EXPIRY_CALC.getExpiryDate(4, DATE, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 8, 28), EXPIRY_CALC.getExpiryDate(5, DATE, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 10, 29), EXPIRY_CALC.getExpiryDate(6, DATE, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 27), EXPIRY_CALC.getExpiryDate(7, DATE, WEEKEND_CALENDAR));
  }

}
