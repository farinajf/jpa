/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.readonly;

import es.my.model.Constants;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private Date fechaFin;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario vendedor;

    @OneToMany(mappedBy = "item")
    private Set<Bid> bids = new HashSet<Bid>();

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

    public Item(final String n, final Date f, final Usuario u) {
        this.nombre   = n;
        this.fechaFin = f;
        this.vendedor = u;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long     getId()       {return id;}
    public String   getNombre()   {return nombre;}
    public Date     getFechaFin() {return fechaFin;}
    public Usuario  getVendedor() {return vendedor;}
    public Set<Bid> getBids()     {return bids;}

    public void setNombre  (final String   x) {this.nombre   = x;}
    public void setFechaFin(final Date     x) {this.fechaFin = x;}
    public void setVendedor(final Usuario  x) {this.vendedor = x;}
    public void setBids    (final Set<Bid> x) {this.bids     = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", fechaFin=" + fechaFin + ", vendedor=" + vendedor + ", bids=" + bids + '}';
    }
}
