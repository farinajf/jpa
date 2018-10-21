/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.advanced;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.Item2;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class AccessType extends JPATest {

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
    public void configurePersistenceUnit() throws Exception {this.configurePersistenceUnit("myAdvancedPUnit");}

    /**
     *
     * @throws Exception
     */
    @Test
    public void storeLoadAccessType() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item2 x = new Item2();

                x.setNombre("x1");
                x.setDesc  ("Descripcion de este objeto.");

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item2 y = em.find(Item2.class, id);

                Constants.print(y);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
