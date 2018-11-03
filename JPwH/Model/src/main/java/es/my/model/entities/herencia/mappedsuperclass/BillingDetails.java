/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.mappedsuperclass;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@MappedSuperclass
public abstract class BillingDetails {

    @NotNull
    private String owner;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected BillingDetails() {}

    protected BillingDetails(final String owner) {
        this.owner = owner;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getOwner() {return owner;}

    public void setOwner(final String x) {this.owner = x;}

    @Override
    public String toString() {
        return "BillingDetails{" + "owner=" + owner + '}';
    }
}
