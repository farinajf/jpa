/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.secondarytable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class Direccion {

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false, length = 5)
    private String codigoPostal;

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
    public Direccion() {}

    public Direccion(final String c, final String cp, final String p) {
        this.provincia    = p;
        this.codigoPostal = cp;
        this.calle        = c;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCalle()        {return calle;}
    public String getCodigoPostal() {return codigoPostal;}
    public String getProvincia()    {return provincia;}

    public void setCalle       (final String x) {this.calle        = x;}
    public void setCodigoPostal(final String x) {this.codigoPostal = x;}
    public void setProvincia   (final String x) {this.provincia    = x;}

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + ", codigoPostal=" + codigoPostal + ", provincia=" + provincia + '}';
    }
}
