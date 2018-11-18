/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.jph.env.JPATest;
import es.my.model.entities.herencia.asociaciones.manytoone.BillingDetails;
import es.my.model.entities.herencia.asociaciones.manytoone.TarjetaCredito;
import es.my.model.entities.herencia.asociaciones.manytoone.User;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class PolimorfismoManyToOne extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myPolimorfismoManyToOnePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final TarjetaCredito tc   = new TarjetaCredito("Pepe", "1234567812345678", "11", "2020");
                final User           pepe = new User("Pepe");

                pepe.setBilling(tc);

                em.persist(tc);
                em.persist(pepe);

                id = pepe.getId();
            }
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final User x = em.find(User.class, id);

                x.getBilling().pagar(100);

                tx.commit();
                em.close();
            }
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final User           x  = em.find(User.class, id);
                final BillingDetails bd = x.getBilling();

                System.out.println("BillingDetails class (1): " + bd.getClass().getName());

                final TarjetaCredito y = em.getReference(TarjetaCredito.class, bd.getId());

                tx.commit();
                em.close();
            }
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final User x = (User) em.createQuery("select u from User u left join fetch u.billing where u.id = :id").setParameter("id", id).getSingleResult();

                System.out.println("BillingDetails class (2): " + x.getBilling().getClass().getName());

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
