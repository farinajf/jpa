/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.setofstrings;

import es.my.model.Constants;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 * Ejecuta un insert por cada elemento de la coleccion.
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
    private java.util.Set<String> imagenes = new java.util.HashSet<>();

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
    public Long getId() {return id;}
    public java.util.Set<String> getImagenes() {return imagenes;}

    public void setImagenes(final java.util.Set<String> x) {imagenes = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", imagenes=" + imagenes + '}';
    }
}
