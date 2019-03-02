/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.jph.shared.util;

/**
 *
 * @author fran
 */
public class TestData {

    private final Long[] ids;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public TestData(final Long[] x) {
        this.ids = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long getPrimerId() {return ids.length > 0 ? ids[0]             : null;}
    public Long getUltimoId() {return ids.length > 0 ? ids[ids.length -1] : null;}
}
