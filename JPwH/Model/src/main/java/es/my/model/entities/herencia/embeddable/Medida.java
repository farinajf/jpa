/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.embeddable;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@MappedSuperclass
public abstract class Medida {

    @NotNull
    private String nombre;

    @NotNull
    private String simbolo;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Medida() {}

    protected Medida(final String nombre, final String simbolo) {
        this.nombre  = nombre;
        this.simbolo = simbolo;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getNombre()  {return nombre;}
    public String getSimbolo() {return simbolo;}

    public void setNombre (final String x) {this.nombre  = x;}
    public void setSimbolo(final String x) {this.simbolo = x;}

    @Override
    public String toString() {
        return "Medida{" + "nombre=" + nombre + ", simbolo=" + simbolo + '}';
    }
}
