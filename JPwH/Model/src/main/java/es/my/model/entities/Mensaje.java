/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author fran
 */
@Entity
public class Mensaje {

    @Id
    @GeneratedValue
    private Long   id;
    private String mensaje;

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
    public String getMensaje() {return mensaje;}

    public void setMensaje(final String x) {this.mensaje = x;}

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return "Mensaje{" + "id=" + id + ", mensaje=" + mensaje + '}';
    }
}
