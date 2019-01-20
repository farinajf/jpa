/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.mapas.mapkey.Bid;
import es.my.model.entities.asociaciones.mapas.mapkey.Item;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MapsMapKey extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myMapsMapKeyPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i = new Item("item-1");

                em.persist(i);

                final Bid b1 = new Bid(new BigDecimal("100"), i);
                em.persist(b1);
                i.getBids().put(b1.getId(), b1);

                final Bid b2 = new Bid(new BigDecimal("19"), i);
                em.persist(b2);
                i.getBids().put(b2.getId(), b2);

                tx.commit();
                em.close();

                id = i.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i = em.find(Item.class, id);

                Constants.print(i);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
