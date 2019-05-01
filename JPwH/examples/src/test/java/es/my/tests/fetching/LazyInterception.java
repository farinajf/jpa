/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.fetching;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.CalendarUtil;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.fetching.interception.Item;
import es.my.model.entities.fetching.interception.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.Hibernate;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class LazyInterception extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    /**
     *
     * @return
     * @throws Exception
     */
    private FetchTestData _storeTestData() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final EntityManager   em = _JPA.createEntityManager();

        tx.begin();

        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        final Usuario u1 = new Usuario("U-1");
        em.persist(u1);
        userIds[0] = u1.getId();

        final Usuario u2 = new Usuario("U-2");
        em.persist(u2);
        userIds[1] = u2.getId();

        final Usuario u3 = new Usuario("U-3");
        em.persist(u3);
        userIds[2] = u3.getId();

        Item item = new Item("ITEM-1", CalendarUtil.MANHANA.getTime(), u1, "Descripcion 1.");
        em.persist(item);
        itemIds[0] = item.getId();

        item = new Item("ITEM-2", CalendarUtil.MANHANA.getTime(), u1, "Descripcion 2.");
        em.persist(item);
        itemIds[1] = item.getId();

        item = new Item("ITEM-3", CalendarUtil.MANHANA.getTime(), u2, "Descripcion 3.");
        em.persist(item);
        itemIds[2] = item.getId();

        tx.commit();
        em.close();

        final FetchTestData result = new FetchTestData();

        result.items    = new TestData(itemIds);
        result.usuarios = new TestData(userIds);

        return result;
    }

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
    public void configurePU() throws Exception {super.configurePU("myFetchingInterceptionPUnit");}

    //@Test
    public void noUserProxy() throws Exception {
        final FetchTestData   td = _storeTestData();
        final UserTransaction tx = _TM.getUserTransaction();

        Constants.print("NO USER PROXY");

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID = td.items.getPrimerId();
            final Long USER_ID = td.usuarios.getPrimerId();

            {
                //Puesto que los PROXIES estan deshabilitados, se ejecuta la consulta
                //SELECT * FROM USUARIOS WHERE ID = ?
                final Usuario u = em.getReference(Usuario.class, USER_ID);

                Constants.print("User is initialized? " + Hibernate.isInitialized(u));
            }

            em.clear();

            {
                //SELECT * FROM ITEM WHERE ID = ?
                //SELECT * FROM USUARIOS WHERE ID = ? Ojo: Hibernate 5
                Item i = em.find(Item.class, ITEM_ID);

                Constants.print("");
                Constants.print("Item.vendedor.id: " + i.getVendedor().getId());
            }

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test
    public void lazyBasic() throws Exception {
        final FetchTestData   td = _storeTestData();
        final UserTransaction tx = _TM.getUserTransaction();

        Constants.print("LAZY BASIC");

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID = td.items.getPrimerId();

            //SELECT * FROM ITEM WHERE ID = ?
            //SELECT * FROM USUARIOS WHERE ID = ? Ojo: Hibernate 5
            final Item i = em.find(Item.class, ITEM_ID);

            Constants.print("");
            Constants.print("Item.descripcion.length: " + i.getDescripcion().length());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
