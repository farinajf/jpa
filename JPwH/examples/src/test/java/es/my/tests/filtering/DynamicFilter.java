/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.filtering.dynamic.Categoria;
import es.my.model.entities.filtering.dynamic.Item;
import es.my.model.entities.filtering.dynamic.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class DynamicFilter extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    private DynamicFilterTestData _store() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        tx.begin();

        final EntityManager em = _JPA.createEntityManager();

        final DynamicFilterTestData result = new DynamicFilterTestData();
        result.usuarios   = new TestData(new Long[2]);
        result.categorias = new TestData(new Long[2]);
        result.items      = new TestData(new Long[3]);

        final Usuario u1 = new Usuario("U-01");
        em.persist(u1);
        result.usuarios.getIds()[0] = u1.getId();

        final Usuario u2 = new Usuario("U-02", 100);
        em.persist(u2);
        result.usuarios.getIds()[1] = u2.getId();

        final Categoria c1 = new Categoria("C-01");
        em.persist(c1);
        result.categorias.getIds()[0] = c1.getId();

        final Categoria c2 = new Categoria("C-02");
        em.persist(c2);
        result.categorias.getIds()[1] = c2.getId();

        final Item i1 = new Item("I-01", c1, u1);
        em.persist(i1);
        result.items.getIds()[0] = i1.getId();

        final Item i2 = new Item("I-02", c1, u2);
        em.persist(i2);
        result.items.getIds()[1] = i2.getId();

        final Item i3 = new Item("I-03", c2, u2);
        em.persist(i3);
        result.items.getIds()[2] = i3.getId();

        tx.commit();
        em.close();

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
    public void configurePU() throws Exception {super.configurePU("myFilteringDynamicPUnit");}

    //@Test
    public void filterItems() throws Throwable {
        final DynamicFilterTestData td = _store();
        final UserTransaction       tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            {
                final org.hibernate.Filter f = em.unwrap(Session.class).enableFilter("limitByUserRank");

                f.setParameter("rangoUsuarioActual", 0);

                Constants.print("Filtrando Items por Rango <= 0");
                {
                    // SELECT * FROM Item WHERE 0>= (SELECT u.RANGO FROM Usuarios U WHERE u.ID = VENDEDOR_ID)
                    final List<Item> items = em.createQuery("SELECT i FROM Item i").getResultList();
                    for (Item i : items) System.out.println(">> Item: " + i);
                }
                em.clear();

                Constants.print("Criteria - filtrando Items por Rango <= 0");
                {
                    final CriteriaBuilder cb = em.getCriteriaBuilder();
                    final CriteriaQuery   q  = cb.createQuery();

                    q.select(q.from(Item.class));

                    final List<Item> items = em.createQuery(q).getResultList();

                    for (Item i : items) System.out.println(">> Item: " + i);
                }
                em.clear();

                f.setParameter("rangoUsuarioActual", 100);

                Constants.print("Filtrando Items por Rango <= 100");
                final List<Item> items = em.createQuery("SELECT i FROM Item i").getResultList();
                for (Item i : items) System.out.println(">> Item: " + i);
            }
            em.clear();

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test
    public void filterCollection() throws Throwable {

    }

    /**
     *
     */
    class DynamicFilterTestData {
        TestData categorias;
        TestData items;
        TestData usuarios;
    }
}
