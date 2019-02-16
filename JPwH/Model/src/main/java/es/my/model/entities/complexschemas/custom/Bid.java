/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.custom;

import es.my.model.Constants;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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
public class Bid {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ITEM_ID")
    )
    private Item item;

    @NotNull
    private BigDecimal cantidad;

    @NotNull
    private java.util.Date fecha = new Date();

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

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()       {return id;}
    public Item       getItem()     {return item;}
    public BigDecimal getCantidad() {return cantidad;}
    public Date       getFecha()    {return fecha;}
}
