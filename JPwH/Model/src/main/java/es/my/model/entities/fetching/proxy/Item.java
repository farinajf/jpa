/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.proxy;

import es.my.model.Constants;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    private Long           id;
    private String         nombre;
    private Date           fechaFin;
    private Usuario        vendedor;
    private Set<Categoria> categorias = new HashSet<Categoria>();
    private Set<Bid>       bids       = new HashSet<Bid>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Item() {}

    public Item(final String n, final Date f, final Usuario v) {
        this.nombre   = n;
        this.fechaFin = f;
        this.vendedor = v;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    public Long getId() {return id;}

    @NotNull
    public String getNombre() {return nombre;}

    @NotNull
    public Date getFechaFin() {return fechaFin;}

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Usuario getVendedor() {return vendedor;}

    @ManyToMany(mappedBy = "items")
    public Set<Categoria> getCategorias() {return categorias;}

    @OneToMany(mappedBy = "item")
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.EXTRA)
    public Set<Bid> getBids() {return bids;}

    public void setId        (final Long           x) {this.id         = x;}
    public void setNombre    (final String         x) {this.nombre     = x;}
    public void setFechaFin  (final Date           x) {this.fechaFin   = x;}
    public void setVendedor  (final Usuario        x) {this.vendedor   = x;}
    public void setCategorias(final Set<Categoria> x) {this.categorias = x;}
    public void setBids      (final Set<Bid>       x) {this.bids       = x;}

}
