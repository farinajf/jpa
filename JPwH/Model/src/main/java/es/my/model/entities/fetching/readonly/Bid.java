/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.readonly;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@org.hibernate.annotations.Immutable
public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario bidder;

    @NotNull
    private BigDecimal cantidad;

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

    public Bid(final Item i, final Usuario b, final BigDecimal c) {
        this.item     = i;
        this.bidder   = b;
        this.cantidad = c;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()       {return id;}
    public Item       getItem()     {return item;}
    public Usuario    getBidder()   {return bidder;}
    public BigDecimal getCantidad() {return cantidad;}

    public void setItem    (final Item       x) {this.item     = x;}
    public void setBidder  (final Usuario    x) {this.bidder   = x;}
    public void setCantidad(final BigDecimal x) {this.cantidad = x;}

    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", bidder=" + bidder + ", cantidad=" + cantidad + '}';
    }
}
