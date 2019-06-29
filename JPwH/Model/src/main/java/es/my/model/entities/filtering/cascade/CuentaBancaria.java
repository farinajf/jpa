/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.cascade;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class CuentaBancaria extends BillingDetails {

    @NotNull
    private String cuenta;

    @NotNull
    private String banco;

    @NotNull
    private String swift;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public CuentaBancaria() {
        super();
    }

    public CuentaBancaria(final String p, final String c, final String b, final String s) {
        super(p);

        this.cuenta = c;
        this.banco    = b;
        this.swift    = s;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCuenta() {return cuenta;}
    public String getBanco()  {return banco;}
    public String getSwift()  {return swift;}

    public void setCuenta(final String x) {this.cuenta = x;}
    public void setBanco (final String x) {this.banco  = x;}
    public void setSwift (final String x) {this.swift  = x;}

    @Override
    public String toString() {
        return "CuentaBancaria{" + "cuenta=" + cuenta + ", banco=" + banco + ", swift=" + swift + '}';
    }
}
