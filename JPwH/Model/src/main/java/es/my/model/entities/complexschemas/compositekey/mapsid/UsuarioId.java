/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.compositekey.mapsid;

import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class UsuarioId implements java.io.Serializable {

    private String nombre;
    private Long   empresaId;

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

    public UsuarioId(final String x, final Long y) {
        this.nombre    = x;
        this.empresaId = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getNombre()    {return nombre;}
    public Long   getEmpresaId() {return empresaId;}

    @Override
    public String toString() {
        return "UsuarioId{" + "nombre=" + nombre + ", empresaId=" + empresaId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.empresaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final UsuarioId other = (UsuarioId) obj;
        if (!Objects.equals(this.nombre,    other.nombre))    return false;
        if (!Objects.equals(this.empresaId, other.empresaId)) return false;

        return true;
    }
}
