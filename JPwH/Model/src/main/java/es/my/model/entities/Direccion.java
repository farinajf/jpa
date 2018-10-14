/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class Direccion {

    @NotNull                  // Ignorado en la generacion del DDL
    @Column(nullable = false) // Usado en la generacion del DDL
    private String calle;

    @NotNull
    @Column(nullable = false, length = 5)
    private String zipCode;

    @NotNull
    @Column(nullable = false)
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
        this.zipCode       = zipCode;
        this.provincia = provincia;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCalle()     {return calle;}
    public String getProvincia() {return provincia;}
    public String getZipCode()   {return zipCode;}

    public void setCalle    (final String x) {this.calle     = x;}
    public void setProvincia(final String x) {this.provincia = x;}
    public void setZipCode  (final String x) {this.zipCode   = x;}

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + ", zipCode=" + zipCode + ", provincia=" + provincia + '}';
    }
}
