/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.mapas.mapkey;

import es.my.model.Constants;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
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

    @MapKey(name = "id")
    @OneToMany(mappedBy = "item")
    private Map<Long, Bid> bids = new HashMap<>();

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
    public Long           getId()     {return id;}
    public String         getNombre() {return nombre;}
    public Map<Long, Bid> getBids()   {return bids;}

    public void setNombre(final String x) {this.nombre = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", bids=" + bids + '}';
    }
}
