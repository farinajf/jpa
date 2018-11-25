/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.colecciones.mapofembeddables;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class Fichero {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String extension;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Fichero() {}

    public Fichero(final String n, final String e) {
        this.nombre    = n;
        this.extension = e;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getExtension() {return extension;}
    public String getNombre()    {return nombre;}

    public void setNombre   (final String x) {this.nombre    = x;}
    public void setExtension(final String x) {this.extension = x;}

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.extension);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final Fichero other = (Fichero) obj;

        if (!Objects.equals(this.nombre,    other.nombre))    return false;
        if (!Objects.equals(this.extension, other.extension)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Fichero{" + "nombre=" + nombre + ", extension=" + extension + '}';
    }
}
