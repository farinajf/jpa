/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Clase de tipo valor (value-type).
 *
 * @author fran
 */
public class ValorMonetario implements Serializable {
    private static final String _SEPARADOR = " ";

    private final BigDecimal valor;
    private final Currency   moneda;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public ValorMonetario(final BigDecimal x, final Currency y) {
        this.valor  = x;
        this.moneda = y;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Currency   getMoneda() {return moneda;}
    public BigDecimal getValor()  {return valor;}

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 29*valor.hashCode() + moneda.hashCode();
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public boolean equals(Object x) {
        if (this == x) return true;

        if (!(x instanceof ValorMonetario)) return false;

        final ValorMonetario vm = (ValorMonetario) x;

        if (this.valor.equals(vm.getValor())   == false) return false;
        if (this.moneda.equals(vm.getMoneda()) == false) return false;

        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getValor() + _SEPARADOR + this.getMoneda();
    }

    public static ValorMonetario fromString(final String x) {
        final String[] v = x.split(_SEPARADOR);

        return new ValorMonetario(new BigDecimal(v[0]), Currency.getInstance(v[1]));
    }
}
