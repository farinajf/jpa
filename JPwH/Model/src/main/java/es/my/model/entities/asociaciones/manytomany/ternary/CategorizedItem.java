/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.manytomany.ternary;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class CategorizedItem {

    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false,
            updatable = false
    )
    private Item item;

    @ManyToOne
    @JoinColumn(
            name = "USUARIO_ID",
            updatable = false
    )
    @NotNull
    private Usuario anhadidoPor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @NotNull
    private Date fecha = new Date();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public CategorizedItem() {}

    public CategorizedItem(final Usuario anhadidoPor, final Item i) {
        this.anhadidoPor = anhadidoPor;
        this .item       = i;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Usuario getAnhadidoPor() {return anhadidoPor;}
    public Date    getFecha()       {return fecha;}
    public Item    getItem()        {return item;}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.item);
        hash = 37 * hash + Objects.hashCode(this.anhadidoPor);
        hash = 37 * hash + Objects.hashCode(this.fecha);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final CategorizedItem other = (CategorizedItem) obj;

        if (!Objects.equals(this.item, other.item))               return false;
        if (!Objects.equals(this.anhadidoPor, other.anhadidoPor)) return false;
        if (!Objects.equals(this.fecha, other.fecha))             return false;

        return true;
    }

    @Override
    public String toString() {
        return "CategorizedItem{" + "item=" + item + ", anhadidoPor=" + anhadidoPor + ", fecha=" + fecha + '}';
    }
}
