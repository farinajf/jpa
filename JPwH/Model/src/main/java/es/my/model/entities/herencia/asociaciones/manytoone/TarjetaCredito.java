/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.asociaciones.manytoone;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class TarjetaCredito extends BillingDetails {

    @NotNull
    private String numeroTC;

    @NotNull
    private String mesTC;

    @NotNull
    private String anhoTC;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public TarjetaCredito() {super();}

    public TarjetaCredito(final String owner, final String numero, final String mes, final String anho) {
        super(owner);

        this.numeroTC = numero;
        this.mesTC    = mes;
        this.anhoTC   = anho;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public String getNumeroTC() {return numeroTC;}
    public String getMesTC()    {return mesTC;}
    public String getAnhoTC()   {return anhoTC;}

    public void setNumeroTC(final String numeroTC) {this.numeroTC = numeroTC;}
    public void setMesTC   (final String mesTC)    {this.mesTC    = mesTC;}
    public void setAnhoTC  (final String anhoTC)   {this.anhoTC   = anhoTC;}

    @Override
    public String toString() {
        return "TarjetaCredito{" + super.toString() + ", numeroTC=" + numeroTC + ", mesTC=" + mesTC + ", anhoTC=" + anhoTC + '}';
    }
}
