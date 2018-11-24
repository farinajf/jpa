/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.listofstrings;

import es.my.model.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

/**
 * Mantiene la posicion de cada elemento en la lista.
 * Se crea una columna de nombre "ORDEN" para mantener el orden de los elementos en la lista.
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "TABLA_IMAGENES")
    @Column(name = "ARCHIVO")
    @OrderColumn(name = "ORDEN")
    private List<String> imagenes = new ArrayList<>();

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
    public Long         getId()       {return id;}
    public List<String> getImagenes() {return imagenes;}

    public void setImagenes(final List<String> x) {imagenes = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", imagenes=" + imagenes + '}';
    }
}
