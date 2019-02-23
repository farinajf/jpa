/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.complexschemas;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.complexschemas.compositekey.manytoone.Item;
import es.my.model.entities.complexschemas.compositekey.manytoone.Usuario;
import es.my.model.entities.complexschemas.compositekey.manytoone.UsuarioId;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class CompositeKeyManyToOne extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myCompositeKeyManyToOnePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final UsuarioId id = new UsuarioId("x-1", "y-1");
                final Usuario   u  = new Usuario(id);

                em.persist(u);

                final Item i = new Item("i-1");
                i.setVendedor(u);

                em.persist(i);

                tx.commit();
                em.close();

                Constants.print(i);
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final UsuarioId id = new UsuarioId("x-1", "y-1");
                final Usuario   u  = em.find(Usuario.class, id);

                final Item i = (Item) em.createQuery("SELECT i FROM Item i WHERE i.vendedor = :u").setParameter("u", u).getSingleResult();

                tx.commit();
                em.close();

                Constants.print(i);
            }
        }
        finally {_TM.rollback();}
    }
}
