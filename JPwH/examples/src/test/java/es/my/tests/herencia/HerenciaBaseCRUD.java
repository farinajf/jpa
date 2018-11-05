/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public abstract class HerenciaBaseCRUD extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    protected void _doJdbcSqlQuery(final String sqlResource, final boolean matchOrder, final String[] ... expectedRows) throws Exception {
        try (final Session s = _JPA.createEntityManager().unwrap(Session.class))
        {
            s.doWork(new JdbcQueryWork(sqlResource, matchOrder, expectedRows));
        }
    }

    /**
     * Solo es valida si BillingDetails se mapea como @Entity
     * No es valida si BillingDetails se mapea como @MappedSuperclass
     *
     * @return
     */
    protected String _getBillingDetailsQuery() {return "select bd from BillingDetails bd";}
    protected String _getCuentaBancariaQuery() {return "select cb from CuentaBancaria cb";}
    protected String _getTarjetaCreditoQuery() {return "select tc from TarjetaCredito tc";}

    protected void _loadBillingDetails(final String sql) throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                em.persist(_crearCuentaBancaria());
                em.persist(_crearTarjetaCredito());

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                System.out.println("sql: " + sql);

                final List x = em.createQuery(sql).getResultList();

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }

    /**************************************************************************/
    /*                       Metodos Abstractos                               */
    /**************************************************************************/
    abstract protected Object _crearCuentaBancaria();
    abstract protected Object _crearTarjetaCredito();

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public void loadBillingsDetails()  throws Exception {_loadBillingDetails(_getBillingDetailsQuery());}
    @Test
    public void loadBillingDetailsTC() throws Exception {_loadBillingDetails(_getTarjetaCreditoQuery());}
    @Test
    public void loadBillingDetailsCB() throws Exception {_loadBillingDetails(_getCuentaBancariaQuery());}
}
