/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.tableperclass;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class CuentaBancaria2 extends BillingDetails2 {

    @NotNull
    private String cuenta;

    @NotNull
    private String banco;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public CuentaBancaria2() {super();}

    public CuentaBancaria2(final String owner, final String cuenta, final String banco) {
        super(owner);

        this.cuenta = cuenta;
        this.banco  = banco;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getCuenta() {return cuenta;}
    public String getBanco()  {return banco;}

    public void setCuenta(final String x) {this.cuenta = x;}
    public void setBanco (final String x) {this.banco  = x;}

    @Override
    public String toString() {
        return "CuentaBancaria2{" + super.toString() + ", cuenta=" + cuenta + ", banco=" + banco + '}';
    }
}
