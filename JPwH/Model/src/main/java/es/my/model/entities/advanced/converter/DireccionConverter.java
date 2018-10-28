/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced.converter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class DireccionConverter {

    @NotNull
    @Column(nullable = false)
    private String calle;

    @NotNull
    @Column(nullable = false, length = 5)
    private ZipCode zipCode;

    @NotNull
    @Column(nullable = false)
    private String poblacion;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected DireccionConverter() {}

    public DireccionConverter(final String x, final ZipCode y, final String z) {
        this.calle     = x;
        this.zipCode   = y;
        this.poblacion = z;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String  getCalle()     {return calle;}
    public ZipCode getZipCode()   {return zipCode;}
    public String  getPoblacion() {return poblacion;}

    public void setCalle    (final String  x) {this.calle     = x;}
    public void setZipCode  (final ZipCode x) {this.zipCode   = x;}
    public void setPoblacion(final String  x) {this.poblacion = x;}

    @Override
    public String toString() {
        return "DireccionConverter{" + "calle=" + calle + ", zipCode=" + zipCode + ", poblacion=" + poblacion + '}';
    }
}
