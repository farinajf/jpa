/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.colecciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.colecciones.mapofembeddables.Fichero;
import es.my.model.entities.colecciones.mapofembeddables.Imagen;
import es.my.model.entities.colecciones.mapofembeddables.Item;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MapOfEmbeddables extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myMapOfEmbeddablesPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item();

                x.getImagenes().put(new Fichero("f1", "png"), new Imagen("f-1", 100, 200));
                x.getImagenes().put(new Fichero("f2", "png"), new Imagen("f-2", 200, 300));
                x.getImagenes().put(new Fichero("f3", "png"), new Imagen("f-3", 400, 500));
                x.getImagenes().put(new Fichero("f1", "png"), new Imagen("f-1", 500, 200));

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
