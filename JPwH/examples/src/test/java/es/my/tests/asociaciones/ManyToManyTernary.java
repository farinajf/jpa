/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.manytomany.ternary.Categoria;
import es.my.model.entities.asociaciones.manytomany.ternary.CategorizedItem;
import es.my.model.entities.asociaciones.manytomany.ternary.Item;
import es.my.model.entities.asociaciones.manytomany.ternary.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class ManyToManyTernary extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myManyToManyTernaryPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            c1_id;
        final Long            c2_id;
        final Long            i1_id;
        final Long            i2_id;
        final Long            u_id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Categoria c1 = new Categoria("C-1");
                final Categoria c2 = new Categoria("C-2");

                em.persist(c1);
                em.persist(c2);

                final Item i1 = new Item("IT-1");
                final Item i2 = new Item("IT-2");

                em.persist(i1);
                em.persist(i2);

                final Usuario u = new Usuario("juan");

                em.persist(u);

                final CategorizedItem l1 = new CategorizedItem(u, i1);
                c1.getCategorizedItems().add(l1);

                final CategorizedItem l2 = new CategorizedItem(u, i2);
                c1.getCategorizedItems().add(l2);

                final CategorizedItem l3 = new CategorizedItem(u, i1);
                c2.getCategorizedItems().add(l3);

                tx.commit();
                em.close();

                c1_id = c1.getId();
                c2_id = c2.getId();
                i1_id = i1.getId();
                i2_id = i2.getId();
                u_id  = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Categoria c1 = em.find(Categoria.class, c1_id);
                final Categoria c2 = em.find(Categoria.class, c2_id);

                final Item    i1 = em.find(Item.class, i1_id);
                final Usuario u  = em.find(Usuario.class, u_id);

                System.out.println("C1: " + c1);
                System.out.println("C2: " + c2);

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i1 = em.find(Item.class, i1_id);

                final List<Categoria> l = em.createQuery("select c from Categoria c join c.categorizedItems ci where ci.item = :x")
                        .setParameter("x", i1)
                        .getResultList();

                Constants.print(l);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
