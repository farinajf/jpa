/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.colecciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.colecciones.bagofstrings.Item;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class BagOfStrings extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myBagOfStringsPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item();

                x.getImagenes().add("1.png");
                x.getImagenes().add("2.png");
                x.getImagenes().add("3.png");
                x.getImagenes().add("1.png");

                em.persist(x);
                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                System.out.println("----------------------------------------------------------------");
                Constants.print(x);

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                System.out.println("----------------------------------------------------------------");

                final Item x = em.find(Item.class, id);
                em.remove(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
