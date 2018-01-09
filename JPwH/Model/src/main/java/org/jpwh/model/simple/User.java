/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.model.simple;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jpwh.model.Constants;

/**
 *
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    protected String username;

    // Address is @Embeddable, no annotation needed here...
    protected Address homeAddress;

    @Embedded //Not necessary...
    @AttributeOverrides({
        @AttributeOverride(name = "street",  column = @Column(name = "BILLING_STREET")), //Nullable
        @AttributeOverride(name = "zipcode", column = @Column(name = "BILLING_ZIPCODE", length = 5)),
        @AttributeOverride(name = "city",    column = @Column(name = "BILLING_CITY"))
    })
    protected Address billingAddress;

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
    public Long    getId()             {return id;}
    public String  getUsername()       {return username;}
    public Address getHomeAddress()    {return homeAddress;}
    public Address getBillingAddress() {return billingAddress;}

    public void setUsername      (String  x) {this.username       = x;}
    public void setHomeAddress   (Address x) {this.homeAddress    = x;}
    public void setBillingAddress(Address x) {this.billingAddress = x;}
}
