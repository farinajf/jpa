/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.my.tests.converters;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.ValorMonetario;
import es.my.model.entities.advanced.usertype.ItemUserType;
import java.math.BigDecimal;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class UserTypesTest extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myTypesPUnit");}

    @Test
    public void test1() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                final EntityManager em = _JPA.createEntityManager();

                tx.begin();

                final ItemUserType x = new ItemUserType();

                x.setNombre("x1");
                x.setPrecioCompra (new ValorMonetario(new BigDecimal("121"), java.util.Currency.getInstance(Locale.ITALY)));
                x.setPrecioInicial(new ValorMonetario(new BigDecimal("100"), java.util.Currency.getInstance(Locale.GERMANY)));

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final ItemUserType x = em.find(ItemUserType.class, id);

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
