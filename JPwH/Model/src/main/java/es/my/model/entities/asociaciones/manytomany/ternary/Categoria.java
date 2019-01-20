/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.manytomany.ternary;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 *
 * @author fran
 */
@Entity
public class Categoria {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String nombre;

    @ElementCollection
    @CollectionTable(
            name = "CATEGORIA_ITEM",
            joinColumns = @JoinColumn(name = "CATEGORIA_ID")
    )
    private Set<CategorizedItem> categorizedItems = new HashSet<>();

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
    public Long                 getId()               {return id;}
    public String               getNombre()           {return nombre;}
    public Set<CategorizedItem> getCategorizedItems() {return categorizedItems;}

    public void setNombre(final String x) {this.nombre = x;}

    @Override
    public String toString() {
        return "Categoria{" + "id=" + id + ", name=" + nombre + ", categorizedItems=" + categorizedItems + '}';
    }
}
