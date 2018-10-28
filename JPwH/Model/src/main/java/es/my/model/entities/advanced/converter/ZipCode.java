/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced.converter;

/**
 *
 * @author fran
 */
abstract public class ZipCode {

    protected String codigo;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public ZipCode(final String x) {
        this.codigo = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCodigo() {return codigo;}

    @Override
    public int hashCode() {return codigo.hashCode();}

    @Override
    public boolean equals(Object x) {
        if (this == x) return true;

        if (x == null || this.getClass() != x.getClass()) return false;

        final ZipCode zip = (ZipCode) x;
        return codigo.equals(zip.getCodigo());
    }

    @Override
    public String toString() {
        return "ZipCode{" + "codigo=" + codigo + '}';
    }
}
