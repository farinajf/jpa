/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

/**
 *
 * @author fran
 */
public class Direccion {

    private String calle;
    private String zip;

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

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCalle() {return calle;}
    public String getZip()   {return zip;}

    public void setCalle(String x) {this.calle = x;}
    public void setZip  (String x) {this.zip   = x;}

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + ", zip=" + zip + '}';
    }
}
