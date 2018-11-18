/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.model.entities.herencia.mixed.CuentaBancaria;
import es.my.model.entities.herencia.mixed.TarjetaCredito;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Mixed extends HerenciaBaseCRUD {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    @Override
    protected Object _crearCuentaBancaria() {return new CuentaBancaria("SONIA", "123456", "SAN");}

    @Override
    protected Object _crearTarjetaCredito() {return new TarjetaCredito("SONIA", "1234567812345678", "12", "2018");}

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void configurePU() throws Exception {super.configurePU("myMixedPUnit");}

    @Test
    @Override
    public void loadBillingsDetails() throws Exception {super.loadBillingsDetails();}
}
