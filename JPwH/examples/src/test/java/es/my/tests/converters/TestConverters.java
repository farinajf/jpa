/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.converters;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.ValorMonetario;
import es.my.model.entities.advanced.converter.DireccionConverter;
import es.my.model.entities.advanced.converter.ItemConverter;
import es.my.model.entities.advanced.converter.SpainZipCode;
import es.my.model.entities.advanced.converter.UsuarioConverter;
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

    /**
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager      em = _JPA.createEntityManager();
                final UsuarioConverter   x  = new UsuarioConverter();
                final DireccionConverter d  = new DireccionConverter("QUEVEDO 35", new SpainZipCode("22211"), "Vigo");

                x.setNombre("JUAN");
                x.setDireccion(d);

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager    em = _JPA.createEntityManager();
                final UsuarioConverter x  = em.find(UsuarioConverter.class, id);

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
