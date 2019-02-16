/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.naturalprimarykey;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Claves naturales:
 * Si no se declara un generador de identificador sobre ls propiedad @Id, Hibernate
 * espera que la aplicacion sea la que asigne el valor de la clave primaria.
 *
 * @author fran
 */
@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    private String nombre;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Usuario() {}

    public Usuario(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getNombre() {return nombre;}

    public void setNombre(final String x) {this.nombre = x;}

    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre + '}';
    }
}
