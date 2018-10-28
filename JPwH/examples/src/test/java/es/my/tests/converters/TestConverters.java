/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.converters;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.ValorMonetario;
import es.my.model.entities.advanced.converter.ItemConverter;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class TestConverters extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myConverterPUnit");}

    /**
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager  em = _JPA.createEntityManager();
                final ItemConverter  x  = new ItemConverter();
                final ValorMonetario vm = new ValorMonetario(new BigDecimal("98.34"), Currency.getInstance(Locale.GERMANY));

                x.setNombre      ("x1");
                x.setPrecioCompra(vm);

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();

                Constants.print(id);
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();
                final ItemConverter x  = em.find(ItemConverter.class, id);

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
