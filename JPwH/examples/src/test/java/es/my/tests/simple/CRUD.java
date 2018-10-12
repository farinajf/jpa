/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.model.entities.Item;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class CRUD extends JPATest {

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
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("mySimplePUnit");
    }

    @Test
    public void saveAndQuery() throws Exception {
        this.saveAndQuery("");
    }

    public void saveAndQuery(final String query) throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item      x1 = new Item();
            x1.setNombre    ("Articulo-1");
            x1.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));

            em.persist(x1);

            tx.commit();
            em.close();

            System.out.println("item: " + x1);
        }
        finally {_TM.rollback();}
    }
}
