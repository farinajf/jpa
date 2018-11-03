/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.mappedsuperclass;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class CuentaBancaria extends BillingDetails {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

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
    public CuentaBancaria() {super();}

    public CuentaBancaria(final String owner, final String cuenta, final String banco) {
        super(owner);

        this.cuenta = cuenta;
        this.banco  = banco;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long   getId()     {return id;}
    public String getCuenta() {return cuenta;}
    public String getBanco()  {return banco;}

    public void setCuenta(final String x) {this.cuenta = x;}
    public void setBanco (final String x) {this.banco  = x;}

    @Override
    public String toString() {
        return "CuentaBancaria{" + "id=" + id + ", cuenta=" + cuenta + ", banco=" + banco + '}';
    }
}
