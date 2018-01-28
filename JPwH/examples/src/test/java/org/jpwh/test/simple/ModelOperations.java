/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import java.util.Date;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.jpwh.model.simple.Bid;
import org.jpwh.model.simple.Item;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class ModelOperations {

    @Test
    public void linkBidAndItem() {
        Item item = new Item();
        Bid  bid  = new Bid();

        item.addBid(bid);
        bid.setItem(item);

        Assert.assertEquals(item.getBids().size(), 1);
        Assert.assertTrue  (item.getBids().contains(bid));
        Assert.assertEquals(bid.getItem(), item);

        // Again with convenience method
        Bid secondBid = new Bid();
        item.addBid(secondBid);

        Assert.assertEquals(item.getBids().size(), 2);
        Assert.assertTrue  (item.getBids().contains(secondBid));
        Assert.assertEquals(secondBid.getItem(), item);
    }

    @Test
    public void validateItem() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setName("Nuevo item");
        item.setAuctionEnd(new Date());

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        // We have one validation error, auction end date was not in the future!
        Assert.assertEquals(violations.size(), 1);

        ConstraintViolation<Item> violation = violations.iterator().next();

        String failedPropertyName = violation.getPropertyPath().iterator().next().getName();

        Assert.assertEquals(failedPropertyName, "auctionEnd");

        if (Locale.getDefault().getLanguage().equals("en")) Assert.assertEquals(violation.getMessage(), "must be in the future");
    }
}
