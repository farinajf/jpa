/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.mapas.ternary.Categoria;
import es.my.model.entities.asociaciones.mapas.ternary.Item;
import es.my.model.entities.asociaciones.mapas.ternary.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MapsTernary extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myMapsTernaryPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            c1_id;
        final Long            c2_id;
        final Long            i_id;
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

                c1.getAnhadidoPor().put(i1, u);
                c1.getAnhadidoPor().put(i2, u);
                c2.getAnhadidoPor().put(i1, u);

                tx.commit();
                em.close();

                c1_id = c1.getId();
                c2_id = c2.getId();
                i_id  = i1.getId();
                u_id  = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Categoria c1 = em.find(Categoria.class, c1_id);
                final Categoria c2 = em.find(Categoria.class, c2_id);
                final Item      i  = em.find(Item.class,      i_id);
                final Usuario   u  = em.find(Usuario.class,   u_id);

                Constants.print(c1);
                Constants.print(c2);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
