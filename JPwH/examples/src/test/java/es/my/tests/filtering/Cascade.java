/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.filtering.cascade.Bid;
import es.my.model.entities.filtering.cascade.Item;
import es.my.model.entities.filtering.cascade.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Cascade extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myFilteringCascadePUnit");}

    @Test
    public void detatchMerge() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID;

            {
                final Usuario u1 = new Usuario("U-1");
                em.persist(u1);

                final Item i1 = new Item("I-1", u1);
                em.persist(i1);

                ITEM_ID = i1.getId();

                final Bid b1 = new Bid(i1, new BigDecimal("60.0"));
                i1.getBids().add(b1);
                em.persist(b1);

                final Bid b2 = new Bid(i1, new BigDecimal("70.0"));
                i1.getBids().add(b2);
                em.persist(b2);

                em.flush();
            }

            em.clear();

            final Item i = em.find(Item.class, ITEM_ID);

            Constants.print("Item: " + i.toString());

            em.detach(i);
            em.clear();

            i.setNombre("X-I-1");

            final Bid b3 = new Bid(i, new BigDecimal("90.0"));
            i.getBids().add(b3);

            // Hibernate busca el item primero en el contexto de persistencia.
            // Si no existe en el contexto de persistencia, lo carga de la base de datos.
            // Hibernate carga los bids asociados al Item en la misma query.
            // A continuacion copia el nuevo Bid en el Item y lo anhade al contexto
            // de persistencia.
            //
            // SELECT i.*, b.* FROM Item i
            // LEFT OUTER JOIN Bid b ON i.id = b.item_id
            // WHERE i.id=?
            //
            // INSERT INTO Bid (cantidad, item_id, id) VALUES (?,?,?)
            final Item imerge = em.merge(i);

            for (Bid o : imerge.getBids()) Constants.print("Bid.id: " + o.getId());

            em.flush();
            em.clear();

            final Item i2 = em.find(Item.class, ITEM_ID);

            Constants.print("Nuevo Item: " + i2.toString());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
