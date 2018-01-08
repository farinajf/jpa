/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.model.simple;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jpwh.model.Constants;

/**
 * Java Persistence with Hibernate 2 Ed.
 * Ed. Manning
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @Size(min = 2, max = 255, message = "Name es onbligatorio, longitud maxima de 255.")
    protected String name;

    @Future
    protected Date auctionEnd;

    protected BigDecimal buyNowPrice;

    @Transient
    protected Set<Bid> bids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    protected Category category;

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
    public Date       getAuctionEnd()  {return auctionEnd;}
    public Set<Bid>   getBids()        {return bids;}
    public BigDecimal getBuyNowPrice() {return buyNowPrice;}
    public Category   getCategory()    {return category;}
    public Long       getId()          {return id;}
    public String     getName()        {return name;}

    public void setAuctionEnd (Date       x) {auctionEnd  = x;}
    public void setBids       (Set<Bid>   x) {bids        = x;}
    public void setBuyNowPrice(BigDecimal x) {buyNowPrice = x;}
    public void setCategory   (Category   x) {category    = x;}
    public void setName       (String     x) {name        = x;}

    public void addBid(Bid bid) {
        if (bid == null)           throw new NullPointerException("Bid is null!!");
        if (bid.getItem() != null) throw new IllegalStateException("Bid is already assigned to an Item!!");

        getBids().add(bid);
        bid.setItem(this);
    }

    public Bid placeBid(Bid currentHighestBid, BigDecimal bidAmount) {
        if (currentHighestBid == null || bidAmount.compareTo(currentHighestBid.getAmount()) >= 0) return new Bid(bidAmount, this);

        return null;
    }
}
