/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.eagerjoin;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario vendedor;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
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

    public Item(final String n, final Date f, final Usuario v) {
        this.nombre   = n;
        this.fechaFin = f;
        this.vendedor = v;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long     getId()       {return id;}
    public String   getNombre()   {return nombre;}
    public Date     getFechaFin() {return fechaFin;}
    public Usuario  getVendedor() {return vendedor;}
    public Set<Bid> getBids()     {return bids;}

    public void setId      (final Long     x) {this.id       = x;}
    public void setNombre  (final String   x) {this.nombre   = x;}
    public void setFechaFin(final Date     x) {this.fechaFin = x;}
    public void setVendedor(final Usuario  x) {this.vendedor = x;}
    public void setBids    (final Set<Bid> x) {this.bids     = x;}
}
