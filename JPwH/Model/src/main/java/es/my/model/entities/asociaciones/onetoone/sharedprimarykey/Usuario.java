/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetoone.sharedprimarykey;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author fran
 */
@Entity
public class Usuario {

    @Id // El id lo asigna la propia aplicacion (no se define un generador)
    private Long id;

    private String nombre;

    @OneToOne(fetch = FetchType.LAZY,
              optional = false) // El esquema generado por Hibernate contiene un FK.
    @PrimaryKeyJoinColumn       // Establece la estrategia Shared Primary Key.
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
    protected Usuario() {}

    public Usuario(final Long id, final String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long      getId()        {return id;}
    public Direccion getDireccion() {return direccion;}
    public String    getNombre()    {return nombre;}

    public void setNombre   (final String    x) {this.nombre    = x;}
    public void setDireccion(final Direccion x) {this.direccion = x;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
}
