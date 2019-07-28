/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.callback;

import es.my.model.Constants;
import javax.persistence.PostPersist;

/**
 * Entity listener class: no implementa ningun interface especial. Es un objeto
 *                        sin estado.
 * @author fran
 */
public class PersistEntityListener {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public PersistEntityListener() {
        Constants.print("PersistEntityListener()");
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    /**
     * Callback method.
     *
     * @param x
     */
    @PostPersist
    public void notifyAdmin(final Object x) {
        Constants.print("PersistEntityListener.notifyAdmin(" + x + ")");
    }
}
