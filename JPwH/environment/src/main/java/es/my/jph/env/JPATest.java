/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * Starts and stops the JPA environment before/after a test class.
 * Create a subclass to write unit tests. Access the EntityManagerFactory with JPATest
 * and create EntityManager instances.
 *
 * Drops and creates the SQL database schema of the persistence unit before and after
 * every test method. This means your database will be cleaned for every test method.
 *
 */
public class JPATest extends TransactionManagerTest {
    private   String   _persistenceUnitName;
    private   String[] _hbmResources;
    protected JPASetup _JPA;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    protected long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        long   count  = 0;

        int n;

        while (-1 != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    protected String getTextResourceAsString(String resource) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);

        if (is == null) throw new IllegalArgumentException("Resource not found: " + resource);

        StringWriter sw = new StringWriter();
        copy(new InputStreamReader(is), sw);

        return sw.toString();
    }

    protected Throwable unwrapRootCause(Throwable throwable) {return unwrapCauseOfType(throwable, null);}

    protected Throwable unwrapCauseOfType(Throwable throwable, Class<? extends Throwable> type) {
        for (Throwable current = throwable; current != null; current = current.getCause())
        {
            if (type != null && type.isAssignableFrom(current.getClass())) return current;
            throwable = current;
        }

        return throwable;
    }

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @BeforeClass
    public void beforeClass() throws Exception {JPATest.this.configurePU();}

    /**
     * Configura la Unidad de Persistencia de JPA a null.
     *
     * @throws Exception
     */
    public void configurePU() throws Exception {configurePU(null);}

    /**
     * Configura la Unidad de Persistencia de JPA.
     *
     * @param persistenceUnitName
     * @param hbmResources
     * @throws Exception
     */
    public void configurePU(String persistenceUnitName, String... hbmResources) throws Exception {
        this._persistenceUnitName = persistenceUnitName;
        this._hbmResources        = hbmResources;
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        _JPA = new JPASetup(_TM._databaseProduct, _persistenceUnitName, _hbmResources);

        _JPA.dropSchema();
        _JPA.createSchema();

        afterJPABootstrap();
    }

    public void afterJPABootstrap() throws Exception {}

    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws Exception {
        if (_JPA != null)
        {
            beforeJPAClose();
            if ("true".equals(System.getProperty("keepSchema")) == false) _JPA.dropSchema();

            _JPA.getEntityManagerFactory().close();
        }
    }

    public void beforeJPAClose() throws Exception {}
}
