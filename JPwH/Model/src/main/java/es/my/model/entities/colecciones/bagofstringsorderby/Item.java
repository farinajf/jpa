/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.bagofstringsorderby;

import es.my.model.Constants;
import java.util.ArrayList;
import java.util.Collection;
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
    @org.hibernate.annotations.CollectionId(
            columns = @Column(name = "IMAGEN_ID"),
            type = @org.hibernate.annotations.Type(type = "long"),
            generator = Constants.ID_GENERATOR
    )
    @org.hibernate.annotations.OrderBy(clause = "ARCHIVO DESC")
    private Collection<String> imagenes = new ArrayList<>();

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
    public Long               getId()       {return id;}
    public Collection<String> getImagenes() {return imagenes;}

    public void setImagenes(final Collection<String> x) {imagenes = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", imagenes=" + imagenes + '}';
    }
}
