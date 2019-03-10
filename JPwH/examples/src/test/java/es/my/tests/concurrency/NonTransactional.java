/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Item;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class NonTransactional extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myConcurrencyVersioningPUnit");}

    /**
     * Autocommit.
     *
     * @throws Throwable
     */
    @Test(groups = {"H2", "ORACLE", "POSTGRESQL"})
    public void demo1() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            ITEM_ID;

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();
            final Item          i  = new Item("I-1");

            em.persist(i);

            tx.commit();
            em.close();

            ITEM_ID = i.getId();
        }
        finally {_TM.rollback();}

        Constants.print("ITEM: " + ITEM_ID);

        {
            //1.- No hay transaccion activa: el contexto de persistencia esta en modo NO-SINCRONIZADO
            final EntityManager em = _JPA.createEntityManager();

            //2.- Lanza un SELECT en modo AUTO-COMMIT
            final Item item = em.find(Item.class, ITEM_ID);
            item.setNombre("XXXXXXXXXX");

            //3.- En modo SINCRONIZADO Hibernate hace un flush del contexto de persistencia antes de ejecutar una QUERY.
            //    En modo NO-SINCRONIZADO Hibernate no hace flush.
            Constants.print("Item name: " + em.createQuery("SELECT i.nombre FROM Item i WHERE i.id= :id").setParameter("id", ITEM_ID).getSingleResult());

            //4.- Recupera el estado de la base de datos, sobreescribiendo los cambios hechos en la entidad JPA
            em.refresh(item);

            Constants.print("ITEM: " + item);

            em.close();
        }

        Constants.print("22222222222222222222222222222");
        {
            final EntityManager em = _JPA.createEntityManager();

            final Item item = new Item("I-2");

            //5.- Hibernate obtiene un nuevo Id para la identidad, pero no ejecuta el INSERT.
            em.persist(item);

            Constants.print("ITEM: " + item);

            //6.- Se inicia una transaccion. Hibernate sincroniza los cambios en el contexto de persistencia cuando se ejecute el COMMIT.
            tx.begin();
            Constants.print("Joined to transaction: " + em.isJoinedToTransaction());
            if (em.isJoinedToTransaction() == false) em.joinTransaction();
            tx.commit();
            em.close();
        }

        Constants.print("3333333333333333333333333333");
        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Item item = em.find(Item.class, ITEM_ID);
            Constants.print("ITEM: " + item);

            Constants.print("Count: " + em.createQuery("SELECT count(i) FROM Item i").getSingleResult());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}

        Constants.print("4444444444444444444444444444");
        {
            final EntityManager tmp = _JPA.createEntityManager();

            final Item di = tmp.find(Item.class, ITEM_ID);

            tmp.close();

            di.setNombre("OOO");

            final EntityManager em = _JPA.createEntityManager();

            final Item mi = em.merge(di);

            tx.begin();
            em.joinTransaction();
            Constants.print("FLUSH update: " + mi);
            tx.commit();
            em.close();
        }

        Constants.print("555555555555555555555555555");
        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            Constants.print("ITEM.nombre: " + em.find(Item.class, ITEM_ID).getNombre());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}

        Constants.print("6666666666666666666666666");
        {
            final EntityManager em = _JPA.createEntityManager();

            final Item item = em.find(Item.class, ITEM_ID);

            em.remove(item);

            tx.begin();
            em.joinTransaction();
            Constants.print("FLUSH delete: " + item);
            tx.commit();
            em.close();
        }

        Constants.print("77777777777777777777777");
        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            Constants.print("COUNT: " + em.createQuery("SELECT count(i) FROM Item i").getSingleResult());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
