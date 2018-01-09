/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.model.simple;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Embeddable
public class Address {

    @NotNull                  // Ignored for DDL generation
    @Column(nullable = false) // Used for DDL generation
    protected String street;

    @NotNull
    @Column(nullable = false, length = 5) // Override VARCHAR(255)
    protected String zipcode;

    @NotNull
    @Column(nullable = false)
    protected String city;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    protected Address () {}

    public Address (String street, String zipcode, String city) {
        this.street  = street;
        this.zipcode = zipcode;
        this.city    = city;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getStreet()  {return street;}
    public String getZipcode() {return zipcode;}
    public String getCity()    {return city;}

    public void setStreet (String x) {this.street  = x;}
    public void setZipcode(String x) {this.zipcode = x;}
    public void setCity   (String x) {this.city    = x;}
}
