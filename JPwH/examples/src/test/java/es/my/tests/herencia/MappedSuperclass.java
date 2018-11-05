/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import es.my.model.entities.herencia.mappedsuperclass.BillingDetails;
import es.my.model.entities.herencia.mappedsuperclass.CuentaBancaria;
import es.my.model.entities.herencia.mappedsuperclass.TarjetaCredito;
import org.testng.annotations.Test;

/**
 * Se crea una tabla por cada clase concreta.
 *
 * @author fran
 */
public class MappedSuperclass extends HerenciaBaseCRUD {

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

    /**
     * Hibernate soporta consultas polimorficas, pero requiere que el nombre de la
     * clase o la interfaz sea cualificado.
     *
     * @return
     */
    @Override
    protected String _getBillingDetailsQuery() {return "select bd from " + BillingDetails.class.getName() + " bd";}

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void configurePU() throws Exception {super.configurePU("myMappedSuperclassPUnit");}

    //@Test
    public void jdbcSqlCuentaBancaria() throws Exception {
        super.loadBillingsDetails();
        super._doJdbcSqlQuery("herencia/mappedsuperclass/CuentaBancaria.sql.txt", true, new String[]{"\\d*", "SONIA", "123456", "SAN"});
    }

    //@Test
    public void jdbcSqlTarjetaCredito() throws Exception {
    super.loadBillingsDetails();
        super._doJdbcSqlQuery("herencia/mappedsuperclass/TarjetaCredito.sql.txt", true, new String[]{"\\d*", "SONIA", "1234567812345678", "12", "2018"});
    }

    @Test
    @Override
    public void loadBillingsDetails() throws Exception {super.loadBillingsDetails();}
}
