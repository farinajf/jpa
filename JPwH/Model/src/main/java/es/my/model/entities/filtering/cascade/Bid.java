/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.cascade;

import es.my.model.Constants;
import java.math.BigDecimal;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Item item;

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

    public Bid(final Item i, final BigDecimal c) {
        this.item     = i;
        this.cantidad = c;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()       {return id;}
    public Item       getItem()     {return item;}
    public BigDecimal getCantidad() {return cantidad;}

    public void setItem    (final Item       x) {this.item     = x;}
    public void setCantidad(final BigDecimal x) {this.cantidad = x;}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.item.getId());
        hash = 13 * hash + Objects.hashCode(this.cantidad);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final Bid other = (Bid) obj;

        if (!Objects.equals(this.cantidad, other.cantidad))         return false;
        if (!Objects.equals(this.item.getId(), other.item.getId())) return false;

        return true;
    }


    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", item=" + item + ", cantidad=" + cantidad + '}';
    }
}
