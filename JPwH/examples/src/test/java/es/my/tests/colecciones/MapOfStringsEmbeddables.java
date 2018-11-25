/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.colecciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.colecciones.mapofstringsembeddables.Imagen;
import es.my.model.entities.colecciones.mapofstringsembeddables.Item;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MapOfStringsEmbeddables extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myMapOfStringsEmbeddablesPUnit");}

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

                x.getImagenes().put("f1.png", new Imagen("f-1", 100, 200));
                x.getImagenes().put("f2.png", new Imagen("f-2", 200, 300));
                x.getImagenes().put("f3.png", new Imagen("f-3", 400, 500));
                x.getImagenes().put("f1.png", new Imagen("f-1", 500, 200));

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Query q = em.createQuery("SELECT ENTRY(img) FROM Item i JOIN i.imagenes img WHERE KEY(img) like '%.png'");

                final List<Map.Entry<String, Imagen>> result = q.getResultList();

                Constants.print("ENTRY");
                for (Map.Entry<String, Imagen> i : result) System.out.println(i.getKey().toString() + " " + i.getValue().toString());

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Query q = em.createQuery("SELECT VALUE(img) FROM Item i JOIN i.imagenes img WHERE KEY(img) like '%.png'");

                final List<Imagen> result = q.getResultList();

                Constants.print("VALUE");
                for (Imagen i : result) System.out.println(i.toString());

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
