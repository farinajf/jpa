/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import es.my.model.Constants;
import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long      id;
    private String    name;
    private Direccion direccion; // Direccion es @Embeddable, no es necesario una anotacion aqui...

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "calle",     column = @Column(name = "DF_CALLE")),
        @AttributeOverride(name = "zipCode",   column = @Column(name = "DF_CODPOSTAL", length = 5)),
        @AttributeOverride(name = "provincia", column = @Column(name = "DF_PROVINCIA"))
    })
    private Direccion direccionFacturas;

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
    public Long      getId()                {return id;}
    public String    getName()              {return name;}
    public Direccion getDireccion()         {return direccion;}
    public Direccion getDireccionFacturas() {return direccionFacturas;}

    public void setName             (final String    x) {this.name              = x;}
    public void setDireccion        (final Direccion x) {this.direccion         = x;}
    public void setDireccionFacturas(final Direccion x) {this.direccionFacturas = x;}

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", name=" + name + ", direccion=" + direccion + ", direccionFacturas=" + direccionFacturas + '}';
    }
}
