/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.manytomany.linkentity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "CATEGORIA_ITEM")
@org.hibernate.annotations.Immutable
public class CategorizedItem {

    @EmbeddedId
    private Id id = new Id();

    @Column(updatable = false)
    @NotNull
    private String anhadidoPor;

    @Column(updatable = false)
    @NotNull
    private Date fecha = new Date();

    @ManyToOne
    @JoinColumn(
            name = "CATEGORIA_ID",
            insertable = false,
            updatable = false
    )
    private Categoria categoria;

    @ManyToOne
    @JoinColumn (
            name = "ITEM_ID",
            insertable = false,
            updatable = false
    )
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
    public CategorizedItem() {}

    public CategorizedItem(final String anhadidoPor, final Categoria c, final Item i) {
        this.anhadidoPor = anhadidoPor;
        this.categoria   = c;
        this .item       = i;

        this.id.categoriaId = c.getId();
        this.id.itemId      = i.getId();

        c.getCategorizedItems().add(this);
        i.getCategorizedItems().add(this);
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Id        getId()          {return id;}
    public String    getAnhadidoPor() {return anhadidoPor;}
    public Categoria getCategoria()   {return categoria;}
    public Date      getFecha()       {return fecha;}
    public Item      getItem()        {return item;}

    /**************************************************************************/
    /*                        Clases Internas                                 */
    /**************************************************************************/
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "ID_CATEGORIA")
        private Long categoriaId;

        @Column(name = "ITEM_ID")
        private Long itemId;

        public Id() {}

        public Id(final Long x, final Long y) {
            this.categoriaId = x;
            this.itemId      = y;
        }

        @Override
        public boolean equals(final Object o) {
            if (o != null && o instanceof Id) {
                final Id x = (Id) o;

                return this.categoriaId.equals(x.categoriaId) && this.itemId.equals(x.itemId);
            }

            return false;
        }

        @Override
        public int hashCode() {return this.categoriaId.hashCode() + this.itemId.hashCode();}
    }
}
