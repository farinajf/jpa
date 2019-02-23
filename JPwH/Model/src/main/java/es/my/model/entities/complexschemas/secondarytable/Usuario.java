/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.secondarytable;

import es.my.model.Constants;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIOS")
@SecondaryTable(
        name = "DIRECCION_2",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "UID")
)
public class Usuario {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private long id;

    private String nombre;

    private Direccion direccion;

    @AttributeOverrides({
        @AttributeOverride(name = "calle",        column = @Column(table = "DIRECCION_2", nullable = false)),
        @AttributeOverride(name = "codigoPostal", column = @Column(table = "DIRECCION_2", nullable = false, length = 5)),
        @AttributeOverride(name = "provincia",    column = @Column(table = "DIRECCION_2", unique = false))
    })
    private Direccion direccion2;

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

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long      getId()         {return id;}
    public String    getNombre()     {return nombre;}
    public Direccion getDireccion()  {return direccion;}
    public Direccion getDireccion2() {return direccion2;}

    public void setNombre    (final String x)    {this.nombre     = x;}
    public void setDireccion (final Direccion x) {this.direccion  = x;}
    public void setDireccion2(final Direccion x) {this.direccion2 = x;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", direccion2=" + direccion2 + '}';
    }
}
