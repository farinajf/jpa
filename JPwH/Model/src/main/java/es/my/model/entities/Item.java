/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import es.my.model.Constants;
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
import javax.persistence.Version;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @Version
    private long version;

    @NotNull
    @Size(min = 1, max = 10, message = "El nombre es obligatorio!!")
    private String nombre;

    @Future
    private Date auctionEnd;

    private BigDecimal price;

    @Transient
    private Set<Bid> bids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

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
    public Long       getId()         {return id;}
    public Date       getAuctionEnd() {return auctionEnd;}
    public Set<Bid>   getBids()       {return bids;}
    public Categoria  getCategoria()  {return categoria;}
    public String     getNombre()     {return nombre;}
    public BigDecimal getPrice()      {return price;}

    public void setNombre    (final String     x) {this.nombre     = x;}
    public void setAuctionEnd(final Date       x) {this.auctionEnd = x;}
    public void setBids      (final Set<Bid>   x) {this.bids       = x;}
    public void setCategoria (final Categoria  x) {this.categoria  = x;}
    public void setPrice     (final BigDecimal x) {this.price      = x;}

    /**
     *
     * @param x
     */
    public void addBid(final Bid x) {
        if (x.getItem() != null) throw new IllegalStateException("La puja ya pertenece a un item!!");

        this.getBids().add(x);
        x.setItem(this);
    }

    /**
     *
     * @param highestBid
     * @param x
     * @return
     */
    public Bid placeBid(final Bid highestBid, BigDecimal x) {
        if ((highestBid == null) || x.compareTo(highestBid.getValor()) > 0) return new Bid(x, this);

        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Articulo{" + "id=" + id +
               ", version=" + version +
                ", nombre=" + nombre +
                ", price=" + price +
                ", auctionEnd=" + auctionEnd +
                ", categoria=" + categoria +
                ", bids=" + bids +
               '}';
    }
}
