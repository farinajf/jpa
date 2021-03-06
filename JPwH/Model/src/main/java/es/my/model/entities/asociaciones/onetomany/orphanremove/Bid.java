/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.orphanremove;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author fran
 */
@Entity
public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false) //Columna en la tabla BID que es FK
    private Item item;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario bidder;

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

    public Bid(final BigDecimal x, final Item y) {
        this.cantidad = x;
        this.item     = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Item       getItem()     {return item;}
    public Usuario    getBidder()   {return bidder;}
    public BigDecimal getCantidad() {return cantidad;}

    public void setItem    (final Item       x) {this.item     = x;}
    public void setBidder  (final Usuario    x) {this.bidder   = x;}
    public void setCantidad(final BigDecimal x) {this.cantidad = x;}

    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", item=" + item + ", cantidad=" + cantidad + '}';
    }
}
