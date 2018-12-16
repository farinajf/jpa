/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.asociaciones.onetoone.foreignkey;

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
public class Direccion {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull                  // Ignorado en la generacion del DDL
    private String calle;

    @NotNull
    private String zipCode;

    @NotNull
    private String provincia;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Direccion() {}

    public Direccion(final String calle, final String zipCode, final String provincia) {
        this.calle     = calle;
        this.zipCode   = zipCode;
        this.provincia = provincia;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long   getId()        {return id;}
    public String getCalle()     {return calle;}
    public String getProvincia() {return provincia;}
    public String getZipCode()   {return zipCode;}

    public void setCalle    (final String x) {this.calle     = x;}
    public void setProvincia(final String x) {this.provincia = x;}
    public void setZipCode  (final String x) {this.zipCode   = x;}

    @Override
    public String toString() {
        return "Direccion{" + "id=" + id + ", calle=" + calle + ", zipCode=" + zipCode + ", provincia=" + provincia + '}';
    }
}
