/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.my.jph.env;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * Provides a database connection pool with the Bitronix JTA transaction
 * manager (http://docs.codehaus.org/display/BTM/Home).
 * https://github.com/bitronix/btm
 * <p>
 * Hibernate will look up the datasource and <code>UserTransaction</code> through
 * JNDI, that's why you also need a <code>jndi.properties</code> file. A minimal
 * JNDI context is bundled with and started by Bitronix.
 * </p>
 */
public class TransactionManagerSetup {
    private static final Logger _logger = Logger.getLogger(TransactionManagerSetup.class.getName());

    public static final String DATASOURCE_NAME = "myDS";

    protected final Context           _context;
    protected final PoolingDataSource _datasource;
    protected final DatabaseProduct   _databaseProduct;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    /**
     *
     * @param databaseProduct
     * @throws Exception
     */
    public TransactionManagerSetup(DatabaseProduct databaseProduct) throws Exception {
        this(databaseProduct, null);
    }

    public TransactionManagerSetup(DatabaseProduct databaseProduct, String connectionURL) throws Exception {
        _logger.info("Starting database connection pool");

        _context = new InitialContext();

        _logger.fine("Setting stable unique identifier for transaction recovery");
        TransactionManagerServices.getConfiguration().setServerId("myServer1234");

        _logger.fine("Disabling JMX binding of manager in unit tests");
        TransactionManagerServices.getConfiguration().setDisableJmx(true);

        _logger.fine("Disabling transaction logging for unit tests");
        TransactionManagerServices.getConfiguration().setJournal("null");

        _logger.fine("Disabling warnings when the database isn't accessed in a transaction");
        TransactionManagerServices.getConfiguration().setWarnAboutZeroResourceTransaction(false);

        _logger.info("Creating connection pool");
        _datasource = new PoolingDataSource();
        _datasource.setUniqueName                (DATASOURCE_NAME);
        _datasource.setMinPoolSize               ( 1);
        _datasource.setMaxPoolSize               ( 5);
        _datasource.setPreparedStatementCacheSize(10);

        // Our locking/versioning tests assume READ COMMITTED transaction isolation.
        // This is not the default on MySQL InnoDB, so we set it here explicitly.
        _datasource.setIsolationLevel("READ_COMMITTED");

        // Hibernate's SQL schema generator calls connection.setAutoCommit(true)
        // and we use auto-commit mode when the EntityManager is in suspended
        // mode and not joined with a transaction.
        _datasource.setAllowLocalTransactions(true);

        _logger.info("Setting up database connection: " + databaseProduct);
        _databaseProduct = databaseProduct;

        _databaseProduct.configuration.configure(_datasource, connectionURL);

        _logger.info("Initializing transaction and resource management");
        _datasource.init();
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public DatabaseProduct getDatabaseProduct() {return _databaseProduct;}

    public Context getNamingContext() {return _context;}

    /**
     *
     * @return
     */
    public UserTransaction getUserTransaction() {
        try
        {
            return (UserTransaction) getNamingContext().lookup("java:comp/UserTransaction");
        }
        catch (Exception e) {throw new RuntimeException(e);}
    }

    /**
     *
     * @return
     */
    public DataSource getDataSource() {
        try
        {
            return (DataSource) getNamingContext().lookup(DATASOURCE_NAME);
        }
        catch (Exception ex) {throw new RuntimeException(ex);}
    }

    /**
     *
     */
    public void rollback() {
        UserTransaction tx = getUserTransaction();

        try
        {
            if (tx.getStatus() == Status.STATUS_ACTIVE || tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) tx.rollback();
        }
        catch (Exception ex)
        {
            System.err.println("Rollback of transaction failed, trace follows!");
            ex.printStackTrace(System.err);
        }
    }

    public void stop() throws Exception {
        _logger.info("Stopping database connection pool");

        _datasource.close();

        TransactionManagerServices.getTransactionManager().shutdown();
    }
}
