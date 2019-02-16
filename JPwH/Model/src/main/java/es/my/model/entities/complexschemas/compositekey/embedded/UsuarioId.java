/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.compositekey.embedded;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Cualquier clase usada como Id en JPA debe ser serializable.
 *
 * @author fran
 */
@Embeddable
public class UsuarioId implements Serializable {

    private String nombre;
    private String apellido;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected UsuarioId() {}

    public UsuarioId(final String x, final String y) {
        this.nombre   = x;
        this.apellido = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getApellido() {return apellido;}
    public String getNombre()   {return nombre;}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.apellido);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final UsuarioId other = (UsuarioId) obj;
        if (!Objects.equals(this.nombre,   other.nombre))   return false;
        if (!Objects.equals(this.apellido, other.apellido)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "UsuarioId{" + "nombre=" + nombre + ", apellido=" + apellido + '}';
    }
}
