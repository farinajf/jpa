/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.model.Constants;
import es.my.model.entities.Bid;
import es.my.model.entities.Item;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class ModelOperations {

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
    /**
     *
     */
    @Test
    public void linkBidAndItem() {
        final Item x = new Item();
        final Bid  b = new Bid();

        x.addBid (b);
        b.setItem(x);

        Assert.assertEquals(x.getBids().size(), 1);
        Assert.assertTrue  (x.getBids().contains(b));
        Assert.assertEquals(b.getItem(), x);

        final Bid b2 = new Bid();
        x.addBid(b2);

        Assert.assertEquals(x.getBids().size(), 2);
        Assert.assertTrue  (x.getBids().contains(b2));
        Assert.assertEquals(b2.getItem(), x);
    }

    /**
     *
     */
    @Test
    public void validateItem() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator        v       = factory.getValidator();

        final Item x = new Item();

        x.setNombre    ("x-1");
        x.setAuctionEnd(new Date());

        final Set<ConstraintViolation<Item>> vv = v.validate(x);

        Assert.assertEquals(vv.size(), 1);

        final ConstraintViolation<Item> aux = vv.iterator().next();

        final String causa = aux.getPropertyPath().iterator().next().getName();

        Constants.print(causa);
    }
}
