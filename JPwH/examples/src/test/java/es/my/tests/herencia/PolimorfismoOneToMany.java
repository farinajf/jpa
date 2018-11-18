/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.herencia.asociaciones.onetomany.BillingDetails;
import es.my.model.entities.herencia.asociaciones.onetomany.CuentaBancaria;
import es.my.model.entities.herencia.asociaciones.onetomany.TarjetaCredito;
import es.my.model.entities.herencia.asociaciones.onetomany.User;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class PolimorfismoOneToMany extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void configurePU() throws Exception {super.configurePU("myPolimorfismoOneToManyPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final CuentaBancaria cb = new CuentaBancaria("cristina", "1234567890",  "SAN");
                final TarjetaCredito tc = new TarjetaCredito("sonia",    "888844442222", "11", "2020");

                final User sonia = new User("sonia");
                sonia.getBillingDetails().add(tc);
                sonia.getBillingDetails().add(cb);

                cb.setUsuario(sonia);
                tc.setUsuario(sonia);

                em.persist(cb);
                em.persist(tc);
                em.persist(sonia);

                tx.commit();
                em.close();

                id = sonia.getId();
            }
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final User x = em.find(User.class, id);

                System.out.println("..................................................................................................");

                for (BillingDetails aux : x.getBillingDetails())
                {
                    aux.pagar(0);
                    System.out.println("..................................................................................................");
                    Constants.print(aux.toString());
                }

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
