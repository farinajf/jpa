/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
public class Provincia2 {

    @NotNull
    @Column(nullable = false, length = 5)
    private String zipCode;
    @NotNull
    @Column(nullable = false)
    private String nombre;
    @NotNull
    @Column(nullable = false)
    private String pais;

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
    public String getNombre()  {return nombre;}
    public String getZipCode() {return zipCode;}
    public String getPais()    {return pais;}

    public void setZipCode(final String x) {this.zipCode = x;}
    public void setNombre (final String x) {this.nombre  = x;}
    public void setPais   (final String x) {this.pais    = x;}

    @Override
    public String toString() {
        return "Provincia2{" + "zipCode=" + zipCode + ", nombre=" + nombre + ", pais=" + pais + '}';
    }
}
