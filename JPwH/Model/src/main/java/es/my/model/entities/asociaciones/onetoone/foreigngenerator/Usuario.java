/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetoone.foreigngenerator;

import es.my.model.Constants;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author fran
 */
@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String nombre;

    @OneToOne(
            mappedBy = "usuario",
            cascade = CascadeType.PERSIST
    )
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

    public Usuario(final String nombre) {
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
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}
