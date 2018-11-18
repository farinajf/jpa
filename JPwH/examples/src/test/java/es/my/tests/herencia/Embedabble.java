/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.jph.env.JPATest;
import es.my.model.entities.herencia.embeddable.Dimensiones;
import es.my.model.entities.herencia.embeddable.Item;
import es.my.model.entities.herencia.embeddable.Peso;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Embedabble extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myHerenciaEmbedablePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id1;
        final Long            id2;

        try
        {
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final Item x1 = new Item("ITEM-1",
                        Dimensiones.centimetros(BigDecimal.valueOf(10.5d), BigDecimal.valueOf(20.2d), BigDecimal.valueOf(1.8d)),
                        Peso.kilogramos(BigDecimal.valueOf(45.2d)));

                em.persist(x1);

                final Item x2 = new Item("ITEM-2",
                        Dimensiones.metros(BigDecimal.valueOf(102.4d), BigDecimal.valueOf(49.5d), BigDecimal.valueOf(89.4d)),
                        Peso.toneladas(BigDecimal.valueOf(5.0d)));

                em.persist(x2);

                tx.commit();
                em.close();

                id1 = x1.getId();
                id2 = x2.getId();
            }
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final Item x1 = em.find(Item.class, id1);
                final Item x2 = em.find(Item.class, id2);

                tx.commit();
                em.close();

                System.out.println("X1: " + x1.toString());
                System.out.println("X2: " + x2.toString());
            }
        }
        finally {_TM.rollback();}
    }
}
