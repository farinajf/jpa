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
import es.my.model.entities.fetching.proxy.Bid;
import es.my.model.entities.fetching.proxy.Categoria;
import es.my.model.entities.fetching.proxy.Item;
import es.my.model.entities.fetching.proxy.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.UserTransaction;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxyHelper;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class LazyProxyCollections extends JPATest {

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

        Long[] categoriaIds = new Long[3];
        Long[] itemIds      = new Long[3];
        Long[] userIds      = new Long[3];

        final Usuario u1 = new Usuario("u-1");
        em.persist(u1);
        userIds[0] = u1.getId();

        final Usuario u2 = new Usuario("u-2");
        em.persist(u2);
        userIds[1] = u2.getId();

        final Usuario u3 = new Usuario("u-3");
        em.persist(u3);
        userIds[2] = u3.getId();

        Categoria categoria = new Categoria("CAT-1");
        em.persist(categoria);
        categoriaIds[0] = categoria.getId();

        Item item = new Item("ITEM-1", CalendarUtil.MANHANA.getTime(), u1);
        em.persist(item);
        itemIds[0] = item.getId();
        categoria.getItems().add(item);
        item.getCategorias().add(categoria);
        for (int i = 1; i<=3; i++)
        {
            final Bid bid = new Bid(item, u3, new BigDecimal(27+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        categoria = new Categoria("CAT-2");
        em.persist(categoria);
        categoriaIds[1] = categoria.getId();

        item = new Item("ITEM-2", CalendarUtil.MANHANA.getTime(), u1);
        em.persist(item);
        itemIds[1] = item.getId();
        categoria.getItems().add(item);
        item.getCategorias().add(categoria);
        for (int i = 1; i<=1; i++)
        {
            final Bid bid = new Bid(item, u2, new BigDecimal(5+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("ITEM-3", CalendarUtil.PASADO_MANHANA.getTime(), u2);
        em.persist(item);
        itemIds[2] = item.getId();
        categoria.getItems().add(item);
        item.getCategorias().add(categoria);

        categoria = new Categoria("CAT-3");
        em.persist(categoria);
        categoriaIds[2] = categoria.getId();

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
    public void configurePU() throws Exception {super.configurePU("myFetchingProxyPUnit");}

    /**
     *
     * @throws Exception
     */
    @Test
    public void lazyEntityProxies() throws Exception {
        final FetchTestData   testData = _storeTestData();
        final UserTransaction tx       = _TM.getUserTransaction();

        try
        {
            tx.begin();
            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID = testData.items.getPrimerId();
            final Long USER_ID = testData.usuarios.getPrimerId();

            Constants.print("+++++++++++++++++++++++ 1 ++++++++++++++++++++++++++");

            {
                //No se lanza ninguna SELECT sobre la BBDD
                final Item item = em.getReference(Item.class, ITEM_ID);
                Constants.print("Item.id: " + item.getId());

                //Nombre de la clase del Hibernate-Proxy
                Constants.print("ItemProxy.class: " + item.getClass().getName());
                Constants.print("Item.class:      " + HibernateProxyHelper.getClassWithoutInitializingProxy(item).getName());

                PersistenceUtil pu = Persistence.getPersistenceUtil();
                Constants.print("item.isLoaded: " + pu.isLoaded(item));
                Constants.print("item.vendedor.isLoaded: " + pu.isLoaded(item, "vendedor"));

                Constants.print("Hibernate.isInitialized(item): " + Hibernate.isInitialized(item));

                //OJO: Esto lanza una SELECT * FROM ITEM WHERE ID=?
                //Constants.print("Hibernate.isInitialized(item.vendedor): " + Hibernate.isInitialized(item.getVendedor()));

                //Lanza una SELECT * FROM ITEM WHERE ID=?
                Hibernate.initialize(item);
                Constants.print("Hibernate.isInitialized(item.vendedor): " + Hibernate.isInitialized(item.getVendedor()));

                //Lanza una SELECT * FROM USUARIOS WHERE ID=?
                Hibernate.initialize(item.getVendedor());
            }
            em.clear();
            {
                Constants.print("+++++++++++++++++++++++ 2 ++++++++++++++++++++++++++");

                //Lanza una SELECT * FROM ITEM WHERE ID=?
                final Item item = em.find(Item.class, ITEM_ID);

                em.detach(item);
                em.detach(item.getVendedor());

                //Se puede usar PersistenceUtil sin contexto de persistencia para saber si un proxy fue inicializado
                PersistenceUtil pu = Persistence.getPersistenceUtil();
                Constants.print("item.isLoaded: " + pu.isLoaded(item));
                Constants.print("item.vendedor.isLoaded: " + pu.isLoaded(item, "vendedor"));

                //Se puede llamar a getId sobre un proxy, pero si se llama a otro metodo GET se lanza una LazyInitializationException
                Constants.print("item.vendedor.id:     " + item.getVendedor().getId());
                //Constants.print("item.vendedor.nombre: " + item.getVendedor().getNombre());
            }
            em.clear();
            {
                Constants.print("+++++++++++++++++++++++ 3 ++++++++++++++++++++++++++");

                final Item    item = em.getReference(Item.class,    ITEM_ID);
                final Usuario user = em.getReference(Usuario.class, USER_ID);

                final Bid b = new Bid(new BigDecimal("16"));
                b.setItem    (item);
                b.setVendedor(user);

                //Se lanza un INSERT INTO BID VALUES(...
                em.persist(b);

                em.flush();
                em.clear();

                //Se lanza una SELECT * FROM BID WHERE ID=?
                final Bid b2 = em.find(Bid.class, b.getId());
                Constants.print("b2.cantidad: " + b2.getCantidad());
            }
            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
