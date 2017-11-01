/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.env;

import java.util.Locale;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Starts and stops the transaction manager/database pool before/after a test suite.
 * <p>
 * All tests in a suite execute with a single {@link TransactionManagerSetup}, call
 * the static {@link TransactionManagerTest#TM} in your test to access the JTA
 * transaction manager and database connections.
 * </p>
 * <p>
 * The test parameters <code>database</code> (specifying a supported
 * {@link DatabaseProduct}) and a <code>connectionURL</code> are optional.
 * The default is an in-memory H2 database instance, created and destroyed
 * automatically for each test suite.
 * </p>
 */
public class TransactionManagerTest {
    // Static single database connection manager per test suite
    static public TransactionManagerSetup _TM;

    @Parameters({"database", "connectionURL"})
    @BeforeSuite()
    public void beforeSuite(@Optional String database, @Optional String connectionURL) throws Exception {
        final DatabaseProduct dp = database != null ? DatabaseProduct.valueOf(database.toUpperCase(Locale.US)) : DatabaseProduct.H2;

        _TM = new TransactionManagerSetup(dp, connectionURL);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() throws Exception {
        if (_TM != null) _TM.stop();
    }
}
