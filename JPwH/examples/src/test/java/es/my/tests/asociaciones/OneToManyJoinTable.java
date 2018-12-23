/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.jointable.Item;
import es.my.model.entities.asociaciones.onetomany.jointable.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToManyJoinTable extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyJoinTablePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id_1;
        final Long            id_2;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x1 = new Item("X-1");
                em.persist(x1);

                final Item x2 = new Item("X-2");
                em.persist(x2);

                final Usuario u = new Usuario("Juan");

                u.getItems().add(x1);
                x1.setComprador(u);

                u.getItems().add(x2);
                x2.setComprador(u);

                em.persist(u);

                System.out.println("------------------------ COMMIT -------------------------");

                //1.- INSERT INTO Item    (nombre, id) VALUES ...
                //2.- INSERT INTO Item    (nombre, id) VALUES ...
                //3.- INSERT INTO Usuario (nombre, id) VALUES ...
                //4.- INSERT INTO ITEM_COMPRADOR (COMPRADOR, ITEM_ID) VALUES ...
                //5.- INSERT INTO ITEM_COMPRADOR (COMPRADOR, ITEM_ID) VALUES ...

                tx.commit();
                em.close();

                id_1 = x1.getId();
                id_2 = x2.getId();
            }
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                System.out.println("--------------------------------- SELECT 1 ------------------------");
                final Item x = em.find(Item.class, id_1);

                //6.- SELECT id, nombre, ITEM_COMPRADOR.COMPRADOR FROM Item LEFT OUTER JOIN ITEM_COMPRADOR ON Item.id = ITEM_COMPRADOR.ITEM_ID WHERE id=?
                //7.- SELECT id, nombre FROM Usuario WHERE id=?
                System.out.println(x);

                System.out.println("--------------------------------- SELECT 2 ------------------------");
                final Item y = em.find(Item.class, id_2);

                //8.- SELECT id, nombre, ITEM_COMPRADOR.COMPRADOR FROM Item LEFT OUTER JOIN ITEM_COMPRADOR ON Item.id = ITEM_COMPRADOR.ITEM_ID WHERE id=?
                System.out.println(y);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
