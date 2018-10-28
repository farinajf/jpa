/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced.converter;

import es.my.converters.ZipCodeConverter;
import es.my.model.Constants;
import java.io.Serializable;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Table(name = "USUARIOS_CONVERTER")
public class UsuarioConverter implements Serializable {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @Convert(
            converter = ZipCodeConverter.class,
            attributeName = "zipCode"
    )
    private DireccionConverter direccion;

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
    public Long               getId()        {return id;}
    public String             getNombre()    {return nombre;}
    public DireccionConverter getDireccion() {return direccion;}

    public void setNombre   (final String             x) {this.nombre    = x;}
    public void setDireccion(final DireccionConverter x) {this.direccion = x;}

    @Override
    public String toString() {
        return "UsuarioConverter{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
}
