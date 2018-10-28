/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced.converter;

import es.my.converters.ValorMonetarioConverter;
import es.my.model.Constants;
import es.my.model.entities.advanced.ValorMonetario;
import java.util.Date;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class ItemConverter {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @Convert( //OPcional si se habilita 'autoApply'
            converter = ValorMonetarioConverter.class,
            disableConversion = false
    )
    private ValorMonetario precioCompra;

    @NotNull
    private java.util.Date fechaCreacion = new Date();

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
    public Long           getId()            {return id;}
    public String         getNombre()        {return nombre;}
    public ValorMonetario getPrecioCompra()  {return precioCompra;}
    public java.util.Date getFechaCreacion() {return fechaCreacion;}

    public void setNombre      (final String         x) {this.nombre       = x;}
    public void setPrecioCompra(final ValorMonetario x) {this.precioCompra = x;}

    @Override
    public String toString() {
        return "ItemConverter{" + "id=" + id + ", nombre=" + nombre + ", precioCompra=" + precioCompra + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
