/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.proxy;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Categoria {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "CATEGORIA_ITEM",
            joinColumns = @JoinColumn(name = "CATEGORIA_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    protected Set<Item> items = new HashSet<Item>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Categoria() {}

    public Categoria(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long      getId()     {return id;}
    public String    getNombre() {return nombre;}
    public Set<Item> getItems()  {return items;}

    public void setNombre(final String    x) {this.nombre = x;}
    public void setItems (final Set<Item> x) {this.items  = x;}

    @Override
    public String toString() {
        return "Categoria{" + "id=" + id + ", nombre=" + nombre + ", items=" + items + '}';
    }
}
