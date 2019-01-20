/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.mapas.ternary;

import es.my.model.Constants;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;

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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @MapKeyJoinColumn(name = "ITEM_ID")
    @JoinTable(
            name = "CATEGORIA_ITEM",
            joinColumns = @JoinColumn(name = "CATEGORIA_ID"),
            inverseJoinColumns = @JoinColumn(name = "USUARIO_ID")
    )
    private Map<Item, Usuario> anhadidoPor = new HashMap<>();

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
    public Long               getId()          {return id;}
    public String             getNombre()      {return nombre;}
    public Map<Item, Usuario> getAnhadidoPor() {return anhadidoPor;}

    public void setNombre(final String x) {this.nombre = x;}

    @Override
    public String toString() {
        return "Categoria{" + "id=" + id + ", name=" + nombre + ", anhadidoPor=" + anhadidoPor + '}';
    }
}
