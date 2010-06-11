/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.position.db;

import java.util.Set;

import javax.sql.DataSource;
import javax.time.Instant;
import javax.time.InstantProvider;
import javax.time.TimeSource;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.Assert;

import com.opengamma.DataNotFoundException;
import com.opengamma.engine.position.Portfolio;
import com.opengamma.engine.position.PortfolioImpl;
import com.opengamma.engine.position.PortfolioNode;
import com.opengamma.engine.position.PortfolioNodeImpl;
import com.opengamma.engine.position.Position;
import com.opengamma.engine.position.PositionImpl;
import com.opengamma.financial.position.AddPortfolioNodeRequest;
import com.opengamma.financial.position.AddPortfolioRequest;
import com.opengamma.financial.position.AddPositionRequest;
import com.opengamma.financial.position.ManagablePositionMaster;
import com.opengamma.financial.position.PositionSummary;
import com.opengamma.financial.position.SearchPortfoliosRequest;
import com.opengamma.financial.position.SearchPortfoliosResult;
import com.opengamma.financial.position.SearchPositionsRequest;
import com.opengamma.financial.position.SearchPositionsResult;
import com.opengamma.financial.position.UpdatePortfolioNodeRequest;
import com.opengamma.financial.position.UpdatePortfolioRequest;
import com.opengamma.financial.position.UpdatePositionRequest;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.db.DbHelper;

/**
 * A database backed position master.
 * <p>
 * The position master provides a uniform structural view over a set of positions
 * holding them in a tree structure portfolio.
 * This class provides database storage for the entire tree.
 */
public class DbPositionMaster implements ManagablePositionMaster {

  /**
   * The scheme used for UniqueIdentifier objects.
   */
  public static final String IDENTIFIER_SCHEME_DEFAULT = "DbPos";
  /**
   * The type of a portfolio for unique identifiers.
   */
  /* package */ static final char TYPE_PORTFOLIO = 'F';
  /**
   * The type of a node for unique identifiers.
   */
  /* package */ static final char TYPE_NODE = 'N';
  /**
   * The type of a position for unique identifiers.
   */
  /* package */ static final char TYPE_POSITION = 'P';
  /** Logger. */
  private static final Logger s_logger = LoggerFactory.getLogger(DbPositionMaster.class);

  /**
   * The underlying worker.
   */
  private final DbPositionMasterWorker _worker;
  /**
   * The template for database operations.
   */
  private final SimpleJdbcTemplate _jdbcTemplate;
  /**
   * The database specific helper.
   */
  private final DbHelper _dbHelper;
  /**
   * The time-source to use.
   */
  private TimeSource _timeSource = TimeSource.system();
  /**
   * The scheme in use for UniqueIdentifier.
   */
  private String _identifierScheme = IDENTIFIER_SCHEME_DEFAULT;

  /**
   * Creates an instance.
   * @param transactionManager  the transaction manager, not null
   * @param dbHelper  the database specific helper, not null
   */
  public DbPositionMaster(DataSourceTransactionManager transactionManager, DbHelper dbHelper) {
    Validate.notNull(transactionManager, "DataSourceTransactionManager must not be null");
    Validate.notNull(dbHelper, "DbHelper must not be null");
    DataSource dataSource = transactionManager.getDataSource();
    _jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    _worker = new DbPositionMasterWorker(this);
    _dbHelper = dbHelper;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the database worker.
   * @return the worker, non-null
   */
  protected DbPositionMasterWorker getWorker() {
    return _worker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the database template.
   * @return the template, non-null
   */
  protected SimpleJdbcTemplate getTemplate() {
    return _jdbcTemplate;
  }

  /**
   * Gets the database helper.
   * @return the database helper, non-null
   */
  protected DbHelper getDbHelper() {
    return _dbHelper;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the time-source that determines the current time.
   * @return the time-source, not null
   */
  protected TimeSource getTimeSource() {
    return _timeSource;
  }

  /**
   * Sets the time-source.
   * @param timeSource  the time-source, not null
   */
  public void setTimeSource(final TimeSource timeSource) {
    Validate.notNull(timeSource, "TimeSource must not be null");
    _timeSource = timeSource;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the scheme in use for UniqueIdentifier.
   * @return the scheme, not null
   */
  protected String getIdentifierScheme() {
    return _identifierScheme;
  }

  /**
   * Sets the scheme in use for UniqueIdentifier.
   * @param scheme  the scheme, not null
   */
  public void setIdentifierScheme(final String scheme) {
    Validate.notNull(scheme, "Scheme must not be null");
    _identifierScheme = scheme;
  }

  //-------------------------------------------------------------------------
  /**
   * Checks whether the unique identifier has the right scheme.
   * @param uid  the unique identifier, not null
   * @param type  the object type
   */
  protected void checkIdentifier(final UniqueIdentifier uid, final char type) {
    Validate.notNull(uid, "UniqueIdentifier must not be null");
    if (isManagerFor(uid) == false) {
      s_logger.debug("Invalid UniqueIdentifier scheme: {}", uid.getScheme());
      throw new IllegalArgumentException("Invalid identifier for DbPositionMaster: " + uid);
    }
    if (uid.getValue().charAt(0) != type) {
      s_logger.debug("Invalid UniqueIdentifier object type: {} expected {}", type, uid);
      throw new IllegalArgumentException("Invalid object type, expected " + type + " in DbPositionMaster identifier: " + uid);
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Extracts a portfolio object identifier from a unique identifier.
   * @param uid  the unique identifier, not null
   * @return the portfolio object identifier
   */
  protected long extractPortfolioOid(final UniqueIdentifier uid) {
    int pos = uid.getValue().indexOf('-', 1);
    if (pos < 0) {
      return Long.parseLong(uid.getValue().substring(1));
    }
    return Long.parseLong(uid.getValue().substring(1, pos));
  }

  /**
   * Extracts the non-portfolio object identifier from a unique identifier.
   * @param uid  the unique identifier, not null
   * @return the non-portfolio object identifier
   */
  protected long extractOtherOid(final UniqueIdentifier uid) {
    int pos = uid.getValue().indexOf('-', 1);
    if (pos < 0) {
      throw new IllegalArgumentException("Unique identifier is invalid: " + uid);
    }
    return Long.parseLong(uid.getValue().substring(pos + 1));
  }

  /**
   * Extracts the version from a unique identifier.
   * @param uid  the unique identifier, not null
   * @return the version
   */
  protected long extractVersion(final UniqueIdentifier uid) {
    if (uid.getVersion() == null) {
      throw new IllegalArgumentException("Unique identifier does not contain a version: " + uid);
    }
    return Long.parseLong(uid.getVersion());
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean isManagerFor(UniqueIdentifier uid) {
    return uid.getScheme().equals(getIdentifierScheme());
  }

  @Override
  public boolean isModificationSupported() {
    return true;
  }

  //-------------------------------------------------------------------------
  @Override
  public Portfolio getPortfolio(final UniqueIdentifier uid) {
    if (uid.getValue().startsWith("" + TYPE_PORTFOLIO) == false) {
      return null;  // TODO: better solution/exception
    }
    checkIdentifier(uid, TYPE_PORTFOLIO);
    if (uid.isVersioned()) {
      return getWorker().selectPortfolioByOidVersion(extractPortfolioOid(uid), extractVersion(uid), true, true, true);
    }
    return getPortfolio(uid, Instant.now(getTimeSource()));
  }

  @Override
  public Portfolio getPortfolio(final UniqueIdentifier uid, final InstantProvider instantProvider) {
    checkIdentifier(uid, TYPE_PORTFOLIO);
    if (uid.getValue().contains("-")) {
      return null;  // TODO: better solution/exception
    }
    Instant instant = Instant.of(instantProvider);
    long portfolioOid = extractPortfolioOid(uid);
    return getWorker().selectPortfolioByOidInstant(portfolioOid, instant);
  }

  //-------------------------------------------------------------------------
  @Override
  public PortfolioNode getPortfolioNode(final UniqueIdentifier uid) {
    checkIdentifier(uid, TYPE_NODE);
    if (uid.isVersioned()) {
      return getWorker().selectPortfolioNodeTree(extractPortfolioOid(uid), extractOtherOid(uid), extractVersion(uid));
    }
    return getPortfolioNode(uid, Instant.now(getTimeSource()));
  }

  @Override
  public PortfolioNode getPortfolioNode(final UniqueIdentifier uid, final InstantProvider instantProvider) {
    checkIdentifier(uid, TYPE_NODE);
    Instant instant = Instant.of(instantProvider);
    long portfolioOid = extractPortfolioOid(uid);
    try {
      long version = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);
      return getWorker().selectPortfolioNodeTree(portfolioOid, extractOtherOid(uid), version);
    } catch (DataNotFoundException ex) {
      return null;
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public Position getPosition(final UniqueIdentifier uid) {
    checkIdentifier(uid, TYPE_POSITION);
    if (uid.isVersioned()) {
      return getWorker().selectPosition(extractPortfolioOid(uid), extractOtherOid(uid), extractVersion(uid));
    }
    return getPosition(uid, Instant.now(getTimeSource()));
  }

  @Override
  public Position getPosition(final UniqueIdentifier uid, final InstantProvider instantProvider) {
    checkIdentifier(uid, TYPE_POSITION);
    Instant instant = Instant.of(instantProvider);
    long portfolioOid = extractPortfolioOid(uid);
    try {
      long version = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);
      return getWorker().selectPosition(portfolioOid, extractOtherOid(uid), version);
    } catch (DataNotFoundException ex) {
      return null;
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the complete set of portfolio unique identifiers.
   * @return the set of unique identifiers, not null
   */
  @Override
  public Set<UniqueIdentifier> getPortfolioIds() {
    return getWorker().selectPortfolioIds(Instant.now(getTimeSource()));
  }

  /**
   * Gets the complete set of portfolio unique identifiers.
   * @param instantProvider  the instant to query at, not null
   * @return the set of unique identifiers, not null
   */
  public Set<UniqueIdentifier> getPortfolioIds(final InstantProvider instantProvider) {
    Instant instant = Instant.of(instantProvider);
    return getWorker().selectPortfolioIds(instant);
  }

  //-------------------------------------------------------------------------
  @Override
  public SearchPortfoliosResult searchPortfolios(final SearchPortfoliosRequest request) {
    Validate.notNull(request, "SearchPortfoliosRequest must not be null");
    request.checkValid();
    final Instant instant = Instant.now(getTimeSource());
    return getWorker().selectPortfolioSummaries(request, instant);
  }

  @Override
  public UniqueIdentifier addPortfolio(final AddPortfolioRequest request) {
    Validate.notNull(request, "AddPortfolioRequest must not be null");
    request.checkValid();
    final long portfolioOid = getWorker().selectNextPortfolioOid();
    final Portfolio portfolio = new PortfolioImpl(request.getName());
    final Instant instant = Instant.now(getTimeSource());
    final UniqueIdentifier uid = getWorker().insertPortfolio(portfolio, portfolioOid, 1, instant, true);
    getWorker().insertNodes(request.getRootNode(), portfolioOid, 1);
    getWorker().insertTreePositions(request.getRootNode(), portfolioOid, 1);
    return uid;
  }

  @Override
  public UniqueIdentifier updatePortfolio(final UpdatePortfolioRequest request) {
    Assert.notNull(request, "UpdatePortfolioRequest must not be null");
    request.checkValid();
    final UniqueIdentifier oldUid = request.getUniqueIdentifier();
    checkIdentifier(oldUid, TYPE_PORTFOLIO);
    final long portfolioOid = extractPortfolioOid(oldUid);
    final long oldVersion = extractVersion(oldUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to update PortfolioNode as version " + oldVersion + " is not the latest version " + latestVersion);
    }
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    final Portfolio portfolio = new PortfolioImpl(request.getName());
    final long newVersion = latestVersion + 1;
    return getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
  }

  @Override
  public UniqueIdentifier removePortfolio(final UniqueIdentifier portfolioUid) {
    Validate.notNull(portfolioUid, "UniqueIdentifier must not be null");
    checkIdentifier(portfolioUid, TYPE_PORTFOLIO);
    final long portfolioOid = extractPortfolioOid(portfolioUid);
    final long oldVersion = extractVersion(portfolioUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to update PortfolioNode as version " + oldVersion + " is not the latest version " + latestVersion);
    }
    final Portfolio portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, true, false, false);
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    return getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, false);  // insert new version
  }

  @Override
  public UniqueIdentifier reinstatePortfolio(final UniqueIdentifier portfolioUid) {
    Validate.notNull(portfolioUid, "UniqueIdentifier must not be null");
    checkIdentifier(portfolioUid, TYPE_PORTFOLIO);
    final long portfolioOid = extractPortfolioOid(portfolioUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, false);  // find latest version
    final Portfolio portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, false, false, false);
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    return getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
  }

  //-------------------------------------------------------------------------
  @Override
  public UniqueIdentifier addPortfolioNode(final AddPortfolioNodeRequest request) {
    Validate.notNull(request, "AddPortfolioNodeRequest must not be null");
    request.checkValid();
    final UniqueIdentifier parentUid = request.getParentNode();
    checkIdentifier(parentUid, TYPE_NODE);
    final long portfolioOid = extractPortfolioOid(parentUid);
    final long oldVersion = extractVersion(parentUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to add node as parent node version " + oldVersion + " is not the latest version " + latestVersion);
    }
    final PortfolioImpl portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, true, true, false);
    final PortfolioNodeImpl root = portfolio.getRootNode();
    final PortfolioNodeImpl parentNode = (PortfolioNodeImpl) root.getNode(parentUid);
    if (parentNode == null) {
      throw new DataNotFoundException("Unable to add node as specified parent " + parentUid + " could not be found");
    }
    final PortfolioNodeImpl newChild = new PortfolioNodeImpl(request.getName());
    parentNode.addChildNode(newChild);
    final long newChildOid = getWorker().selectNextNodeOid();
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().updateTreeSetEndVersion(portfolioOid, newVersion);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    getWorker().insertNode(newChild, portfolioOid, newChildOid, newVersion, instant);  // insert the child node
    getWorker().insertTree(root, portfolioOid, newVersion);  // insert tree with new child
    return getWorker().createNodeUniqueIdentifier(portfolioOid, newChildOid, newVersion);
  }

  @Override
  public UniqueIdentifier updatePortfolioNode(final UpdatePortfolioNodeRequest request) {
    Validate.notNull(request, "UpdatePortfolioNodeRequest must not be null");
    request.checkValid();
    final UniqueIdentifier nodeUid = request.getUniqueIdentifier();
    checkIdentifier(nodeUid, TYPE_NODE);
    final long portfolioOid = extractPortfolioOid(nodeUid);
    final long nodeOid = extractOtherOid(nodeUid);
    final long oldVersion = extractVersion(nodeUid);
    final Instant instant = Instant.now(getTimeSource());
    final Portfolio portfolio = getWorker().selectPortfolioByOidInstant(portfolioOid, instant);  // find latest version
    if (portfolio == null) {
      throw new DataNotFoundException("Portfolio not found: " + portfolioOid + " at " + instant);
    }
    final long latestVersion = extractVersion(portfolio.getUniqueIdentifier());
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to update node " + nodeUid + " as the version is not the latest version " + latestVersion);
    }
    final long newVersion = latestVersion + 1;
    final PortfolioNode node = new PortfolioNodeImpl(request.getName());
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().updateNodeSetEndVersion(portfolioOid, nodeOid, newVersion);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    return getWorker().insertNode(node, portfolioOid, nodeOid, newVersion, instant);  // insert new version
  }

  @Override
  public UniqueIdentifier removePortfolioNode(final UniqueIdentifier nodeUid) {
    Validate.notNull(nodeUid, "UniqueIdentifier must not be null");
    checkIdentifier(nodeUid, TYPE_NODE);
    final long portfolioOid = extractPortfolioOid(nodeUid);
    final long nodeOid = extractOtherOid(nodeUid);
    final long oldVersion = extractVersion(nodeUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to remove node " + nodeUid + "  as version is not the latest version " + latestVersion);
    }
    final PortfolioImpl portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, true, true, false);
    if (portfolio == null) {
      throw new DataNotFoundException("Portfolio not found: " + portfolioOid + " at " + instant);
    }
    final PortfolioNodeImpl root = portfolio.getRootNode();
    if (root.getUniqueIdentifier().equals(nodeUid)) {
      throw new DataNotFoundException("Unable to remove node " + nodeUid + " as it is the root node in a portfolio");
    }
    if (removeNode(root, nodeUid) == null) {
      throw new DataNotFoundException("Unable to remove node " + nodeUid + " as no parent node could not be found");
    }
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().updateTreeSetEndVersion(portfolioOid, newVersion);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    getWorker().insertTree(root, portfolioOid, newVersion);  // insert tree with removed child
    return getWorker().createNodeUniqueIdentifier(portfolioOid, nodeOid, newVersion);
  }

  /**
   * Recursively finds the node and removes it from its parent.
   * @param node  the node to start from, not null
   * @param uid  the unique identifier of a node/position, not null
   * @return the parent node, not null
   */
  private PortfolioNodeImpl removeNode(PortfolioNodeImpl node, UniqueIdentifier uid) {
    for (PortfolioNode childNode : node.getChildNodes()) {
      if (uid.equals(childNode.getUniqueIdentifier())) {
        node.removeChildNode(childNode);
        return node;
      }
      final PortfolioNodeImpl result = removeNode((PortfolioNodeImpl) childNode, uid);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public UniqueIdentifier reinstatePortfolioNode(final UniqueIdentifier nodeUid) {
    throw new UnsupportedOperationException();
  }

  //-------------------------------------------------------------------------
  @Override
  public PositionSummary getPositionSummary(final UniqueIdentifier positionUid) {
    checkIdentifier(positionUid, TYPE_POSITION);
    final long portfolioOid = extractPortfolioOid(positionUid);
    final long version;
    if (positionUid.isVersioned()) {
      version = extractVersion(positionUid);
    } else {
      version = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, Instant.now(getTimeSource()), true);  // find latest version
    }
    return getWorker().selectPositionSummary(portfolioOid, extractOtherOid(positionUid), version);
  }

  @Override
  public SearchPositionsResult searchPositions(final SearchPositionsRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UniqueIdentifier addPosition(final AddPositionRequest request) {
    Validate.notNull(request, "AddPositionRequest must not be null");
    request.checkValid();
    final UniqueIdentifier parentUid = request.getParentNode();
    checkIdentifier(parentUid, TYPE_NODE);
    final long portfolioOid = extractPortfolioOid(parentUid);
    final long oldVersion = extractVersion(parentUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to add position as parent node version " + oldVersion + " is not the latest version " + latestVersion);
    }
    final Portfolio portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, true, true, false);
    final PositionImpl newPosition = new PositionImpl(request.getQuantity(), request.getSecurityKey());
    final PortfolioNodeImpl tempRoot = new PortfolioNodeImpl(parentUid, "Temp");
    tempRoot.addPosition(newPosition);
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    getWorker().insertTreePositions(tempRoot, portfolioOid, newVersion);  // insert position
    return tempRoot.getPositions().get(0).getUniqueIdentifier();
  }

  @Override
  public UniqueIdentifier updatePosition(final UpdatePositionRequest request) {
    Validate.notNull(request, "UpdatePositionRequest must not be null");
    request.checkValid();
    final UniqueIdentifier positionUid = request.getUniqueIdentifier();
    checkIdentifier(positionUid, TYPE_POSITION);
    final long portfolioOid = extractPortfolioOid(positionUid);
    final long positionOid = extractOtherOid(positionUid);
    final long oldVersion = extractVersion(positionUid);
    final Instant instant = Instant.now(getTimeSource());
    final Portfolio portfolio = getWorker().selectPortfolioByOidInstant(portfolioOid, instant);  // find latest version
    if (portfolio == null) {
      throw new DataNotFoundException("Portfolio not found: " + portfolioOid + " at " + instant);
    }
    final long latestVersion = extractVersion(portfolio.getUniqueIdentifier());
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to update position " + positionUid + " as the version is not the latest version " + latestVersion);
    }
    final PositionSummary position = getWorker().selectPositionSummary(portfolioOid, positionOid, latestVersion);
    if (position == null) {
      throw new DataNotFoundException("Position not found: " + portfolioOid + " at " + instant);
    }
    long parentNodeOid = extractOtherOid(position.getParentNodeUid());
    final PositionImpl newPosition = new PositionImpl(request.getQuantity(), request.getSecurityKey());
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().updatePositionSetEndVersion(portfolioOid, positionOid, newVersion);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    return getWorker().insertPosition(newPosition, portfolioOid, parentNodeOid, newVersion);  // insert position
  }

  @Override
  public UniqueIdentifier removePosition(final UniqueIdentifier positionUid) {
    Validate.notNull(positionUid, "UniqueIdentifier must not be null");
    checkIdentifier(positionUid, TYPE_POSITION);
    final long portfolioOid = extractPortfolioOid(positionUid);
    final long positionOid = extractOtherOid(positionUid);
    final long oldVersion = extractVersion(positionUid);
    final Instant instant = Instant.now(getTimeSource());
    final long latestVersion = getWorker().selectVersionByPortfolioOidInstant(portfolioOid, instant, true);  // find latest version
    if (oldVersion != latestVersion) {
      throw new DataIntegrityViolationException("Unable to remove position " + positionUid + "  as version is not the latest version " + latestVersion);
    }
    final PortfolioImpl portfolio = getWorker().selectPortfolioByOidVersion(portfolioOid, latestVersion, true, true, false);
    if (portfolio == null) {
      throw new DataNotFoundException("Portfolio not found: " + portfolioOid + " at " + instant);
    }
    final long newVersion = latestVersion + 1;
    getWorker().updatePortfolioSetEndInstant(portfolioOid, instant);  // end old version
    getWorker().updatePositionSetEndVersion(portfolioOid, positionOid, newVersion);  // end old version
    getWorker().insertPortfolio(portfolio, portfolioOid, newVersion, instant, true);  // insert new version
    return getWorker().createNodeUniqueIdentifier(portfolioOid, positionOid, newVersion);
  }

  @Override
  public UniqueIdentifier reinstatePosition(final UniqueIdentifier positionUid) {
    throw new UnsupportedOperationException();
  }

  //-------------------------------------------------------------------------
  /**
   * Returns a string summary of this position master.
   * @return the string summary, not null
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + getIdentifierScheme() + "]";
  }

}
