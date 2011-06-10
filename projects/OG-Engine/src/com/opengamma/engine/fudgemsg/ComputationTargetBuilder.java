/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.fudgemsg;

import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeBuilder;
import org.fudgemsg.mapping.FudgeBuilderFor;
import org.fudgemsg.mapping.FudgeDeserializationContext;
import org.fudgemsg.mapping.FudgeSerializationContext;

import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.core.security.Security;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;

/**
 * Fudge message builder for {@link ComputationTarget}.
 */
@FudgeBuilderFor(ComputationTarget.class)
public class ComputationTargetBuilder implements FudgeBuilder<ComputationTarget> {

  private static final String TYPE_FIELD = "type";
  private static final String VALUE_FIELD = "value";
  
  @Override
  public MutableFudgeMsg buildMessage(FudgeSerializationContext context, ComputationTarget object) {
    MutableFudgeMsg msg = context.newMessage();
    context.addToMessage(msg, TYPE_FIELD, null, object.getType());
    context.addToMessageWithClassHeaders(msg, VALUE_FIELD, null, object.getValue());
    return msg;
  }

  @Override
  public ComputationTarget buildObject(FudgeDeserializationContext context, FudgeMsg message) {
    ComputationTargetType type = context.fieldValueToObject(ComputationTargetType.class, message.getByName(TYPE_FIELD));
    
    // Same type assumptions as made in ComputationTarget itself
    Class<?> expectedTargetType;
    switch (type) {
      case PORTFOLIO_NODE:
        expectedTargetType = PortfolioNode.class;
        break;
      case POSITION:
        expectedTargetType = Position.class;
        break;
      case TRADE:
        expectedTargetType = Trade.class;
        break;
      case SECURITY:
        expectedTargetType = Security.class;
        break;
      default:
        expectedTargetType = Object.class;
        break;
    }
    
    Object value = context.fieldValueToObject(expectedTargetType, message.getByName(VALUE_FIELD));
    return new ComputationTarget(type, value);
  }

}
