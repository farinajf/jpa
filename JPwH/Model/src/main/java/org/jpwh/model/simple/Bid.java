/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.model.simple;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.jpwh.model.Constants;

/**
 *
 */
@Entity
@org.hibernate.annotations.Immutable

public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected BigDecimal amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    protected Item item;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Bid() {}

    public Bid(Item item) {
        this.item = item;
        this.item.getBids().add(this);
    }

    public Bid(BigDecimal amount, Item item) {
        this.amount = amount;
        this.item   = item;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public BigDecimal getAmount() {return amount;}
    public Long       getId()     {return id;}
    public Item       getItem()   {return item;}

    public void setAmount(BigDecimal x) {this.amount = x;}
    public void setItem  (Item       x) {this.item   = x;}
}
