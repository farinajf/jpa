/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.sortedsetofstrings;

import es.my.model.Constants;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
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
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @ElementCollection        // Es necesario para una coleccion de tipo valor
    @CollectionTable (
            name = "TABLA_IMAGENES",               // Por defecto es 'Item_imagenes'
            joinColumns = @JoinColumn(name = "id") // Por defecto es 'Item_id'
    )
    @Column(name = "ARCHIVO") // Por defecto el nombre de la columna es 'imagenes'
    @org.hibernate.annotations.SortNatural
    private SortedSet<String> imagenes = new TreeSet<>();

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long              getId()       {return id;}
    public SortedSet<String> getImagenes() {return imagenes;}

    public void setImagenes(final SortedSet<String> x) {imagenes = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", imagenes=" + imagenes + '}';
    }
}
