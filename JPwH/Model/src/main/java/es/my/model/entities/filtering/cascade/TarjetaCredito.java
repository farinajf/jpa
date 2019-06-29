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
public class TarjetaCredito extends BillingDetails {

    @NotNull
    private String numero;

    @NotNull
    private String mes;

    @NotNull
    private String anho;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public TarjetaCredito() {
        super();
    }

    public TarjetaCredito(final String p, final String n, final String m, final String a) {
        super(p);

        this.numero = n;
        this.mes    = m;
        this.anho   = a;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getNumero() {return numero;}
    public String getMes()    {return mes;}
    public String getAnho()   {return anho;}

    public void setNumero(final String x) {this.numero = x;}
    public void setMes   (final String x) {this.mes    = x;}
    public void setAnho  (final String x) {this.anho   = x;}

    @Override
    public String toString() {
        return "TarjetaCredito{" + "numero=" + numero + ", mes=" + mes + ", anho=" + anho + '}';
    }
}
