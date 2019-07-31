/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.dynamic;

import es.my.model.Constants;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "categoria")
    @org.hibernate.annotations.Filter(
            name = "limitByUserRank",
            condition = ":currentUserRank >= (SELECT u.RANGO FROM USUARIOS u WHERE u.ID = VENDEDOR_ID)"
    )
    private Set<Item> items = new HashSet<Item>();

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

    public Categoria(final String n) {
        this.nombre = n;
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
        return "Categoria{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}
