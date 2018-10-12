/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    private BigDecimal valor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
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

    public Bid(Item item) {
        this.item = item;
        this.item.getBids().add(this);
    }

    public Bid(BigDecimal valor, Item item) {
        this(item);
        this.valor = valor;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public BigDecimal getValor() {return valor;}
    public Long       getId()    {return id;}
    public Item       getItem()  {return item;}

    public void setItem (final Item       x) {this.item  = x;}
    public void setValor(final BigDecimal x) {this.valor = x;}

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Bid{" + "id=" + id + ", valor=" + valor + ", item=" + item + '}';
    }
}
