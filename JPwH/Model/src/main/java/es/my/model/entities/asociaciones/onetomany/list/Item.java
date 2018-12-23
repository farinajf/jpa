/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.list;

import es.my.model.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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

    @OneToMany
    @JoinColumn(
            name = "ID_ITEM",
            nullable = false
    )
    @OrderColumn(
            name = "BID_INDICE", // Valor por defecto: BIDS_ORDER
            nullable = false
    )
    private List<Bid> bids = new ArrayList<>();

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
    public Long      getId()     {return id;}
    public String    getNombre() {return nombre;}
    public List<Bid> getBids()   {return bids;}

    public void setNombre(final String    x) {this.nombre = x;}
    public void setBids  (final List<Bid> x) {this.bids   = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", bids=" + bids + '}';
    }
}
