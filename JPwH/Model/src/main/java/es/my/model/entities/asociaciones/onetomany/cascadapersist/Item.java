/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.cascadapersist;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
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

    public Item(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long     getId()     {return id;}
    public String   getNombre() {return nombre;}
    public Set<Bid> getBids()   {return bids;}

    public void setNombre(final String   x) {this.nombre = x;}
    public void setBids  (final Set<Bid> x) {this.bids   = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", bids=" + bids + '}';
    }
}
