/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.joined;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BILLING_DETAILS3")
public class BillingDetails {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

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
    public Long   getId()    {return id;}
    public String getOwner() {return owner;}

    public void setOwner(final String x) {this.owner = x;}

    @Override
    public String toString() {
        return "BillingDetails3{" + "id=" + id + ", owner=" + owner + '}';
    }
}
