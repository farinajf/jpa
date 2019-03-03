/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.concurrency.version;

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
public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private BigDecimal cantidad;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item item;

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

    public Bid(final BigDecimal c, final Item i) {
        this.cantidad = c;
        this.item     = i;
    }

    public Bid(final BigDecimal c, final Item i, final Bid last) throws InvalidBidException {
        if (last != null && c.compareTo(last.getCantidad()) < 1)
        {
            throw new InvalidBidException("Bid de " + c + " invalida!!");
        }

        this.cantidad = c;
        this.item     = i;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()       {return id;}
    public BigDecimal getCantidad() {return cantidad;}
    public Item       getItem()     {return item;}

    public void setCantidad(final BigDecimal x) {this.cantidad = x;}
    public void setItem    (final Item       x) {this.item     = x;}

    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", cantidad=" + cantidad + ", item=" + item + '}';
    }
}
