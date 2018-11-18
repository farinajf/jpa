/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.mixed;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@DiscriminatorValue("TC")
@SecondaryTable(
        name = "TARJETA_CREDITO",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "TARJETACREDITO_ID") //Crea una columna en la tabla secundaria que es un FK
)
//create table TARJETA_CREDITO (
//        anhoTC varchar(255) not null,
//        mesTC varchar(255) not null,
//        numeroTC varchar(255) not null,
//        TARJETACREDITO_ID bigint not null,
//        primary key (TARJETACREDITO_ID))
public class TarjetaCredito extends BillingDetails {

    @NotNull
    @Column(table = "TARJETA_CREDITO", nullable = false)
    private String numeroTC;

    @NotNull
    @Column(table = "TARJETA_CREDITO", nullable = false)
    private String mesTC;

    @NotNull
    @Column(table = "TARJETA_CREDITO", nullable = false)
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
        return "TarjetaCredito4{" + super.toString() + ", numeroTC=" + numeroTC + ", mesTC=" + mesTC + ", anhoTC=" + anhoTC + '}';
    }
}
