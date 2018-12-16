/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.onetoone.jointable.Item;
import es.my.model.entities.asociaciones.onetoone.jointable.Transporte;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToOneJoinTable extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToOneJoinTablePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            itemId;
        final Long            transportId;
        final Long            transportItemId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Transporte t = new Transporte();
                em.persist(t);

                final Item x = new Item("X-1");
                em.persist(x);

                final Transporte t_x = new Transporte(x);
                em.persist(t_x);

                tx.commit();
                em.close();

                itemId          = x.getId();
                transportId     = t.getId();
                transportItemId = t_x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

//                final Item x = em.find(Item.class, itemId);
//                Constants.print(x);

                final Transporte t = em.find(Transporte.class, transportId);
                Constants.print(t);

                final Transporte t_x = em.find(Transporte.class, transportItemId);
                Constants.print(t_x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
