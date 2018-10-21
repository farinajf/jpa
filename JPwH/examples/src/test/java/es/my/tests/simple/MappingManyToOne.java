/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.Bid;
import es.my.model.entities.Item;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MappingManyToOne extends JPATest {
    private static final Logger LOG = Logger.getLogger(MappingManyToOne.class.getName());

    @Override
    public void configurePU() throws Exception {this.configurePU("mySimplePUnit");}

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
    /**
     *
     * @throws Exception
     */
    @Test
    public void storeAndLoad() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item();

                x.setNombre("x-1");

                final Bid b1 = new Bid(new BigDecimal("111"), x);
                final Bid b2 = new Bid(new BigDecimal("999"), x);

                em.persist(x);
                em.persist(b2);
                em.persist(b1);

                tx.commit();

                em.close();

                id = b1.getId();

                System.out.println("x.getBids().size: " + x.getBids().size());
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Bid w = em.find(Bid.class, id);

                Constants.print(w);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
