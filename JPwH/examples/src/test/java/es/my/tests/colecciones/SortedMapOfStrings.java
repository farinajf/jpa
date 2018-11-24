/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.colecciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.colecciones.sortedmapofstrings.Item;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class SortedMapOfStrings extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("mySortedMapOfStringsPUnit");}

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

                x.getImagenes().put("k-1", "1.png");
                x.getImagenes().put("k-2", "2.png");
                x.getImagenes().put("k-3", "3.png");
                x.getImagenes().put("k-1", "1.png");

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
