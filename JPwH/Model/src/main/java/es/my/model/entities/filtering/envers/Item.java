/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.envers;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author fran
 */
@Entity
@Audited
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "item")
    @NotAudited
    private Set<Bid> bids = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDEDOR_ID", nullable = false)
    private Usuario vendedor;

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

    public Item(final String n, final Usuario v) {
        this.nombre   = n;
        this.vendedor = v;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long     getId()       {return id;}
    public String   getNombre()   {return nombre;}
    public Usuario  getVendedor() {return vendedor;}
    public Set<Bid> getBids()     {return bids;}

    public void setId      (final Long     x) {this.id       = x;}
    public void setNombre  (final String   x) {this.nombre   = x;}
    public void setVendedor(final Usuario  x) {this.vendedor = x;}
    public void setBids    (final Set<Bid> x) {this.bids     = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", vendedor=" + vendedor + ", bids=" + bids + '}';
    }
}
