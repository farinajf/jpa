/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetomany.embeddable;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    private Direccion direccion;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Usuario() {}

    public Usuario(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long      getId()        {return id;}
    public String    getNombre()    {return nombre;}
    public Direccion getDireccion() {return direccion;}

    public void setNombre   (final String    x) {this.nombre    = x;}
    public void setDireccion(final Direccion x) {this.direccion = x;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
}
