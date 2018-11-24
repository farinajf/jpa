/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.sortedmapofstrings;

import es.my.model.Constants;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

/**
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
    @MapKeyColumn(name = "CLAVE_IMAGEN")
    @org.hibernate.annotations.SortComparator(ReverseStringComparator.class)
    private SortedMap<String, String> imagenes = new TreeMap<>();

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
    public Long                      getId()       {return id;}
    public SortedMap<String, String> getImagenes() {return imagenes;}

    public void setImagenes(final SortedMap<String, String> x) {imagenes = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", imagenes=" + imagenes + '}';
    }

    /**
     * Orden inverso.
     */
    public static class ReverseStringComparator implements Comparator<String> {
        @Override
        public int compare(final String x, final String y) {return y.compareTo(x);}
    }
}
