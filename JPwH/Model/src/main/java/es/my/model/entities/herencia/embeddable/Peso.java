/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.herencia.embeddable;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Embeddable
@AttributeOverrides({
    @AttributeOverride(name = "nombre",  column = @Column(name = "PESO_NOMBRE")),
    @AttributeOverride(name = "simbolo", column = @Column(name = "PESO_SIMBOLO"))
})
public class Peso extends Medida {

    @NotNull
    @Column(name = "PESO")
    private BigDecimal valor;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Peso() {}

    public Peso(final String nombre, final String simbolo, final BigDecimal peso) {
        super(nombre, simbolo);

        this.valor = peso;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public BigDecimal getValor() {return valor;}

    public void setValor(final BigDecimal x) {this.valor = x;}

    @Override
    public String toString() {
        return "Peso{" + "valor=" + valor + '}';
    }

    /**************************************************************************/
    /*                  Metodos Publicos Estaticos                            */
    /**************************************************************************/
    public static Peso kilogramos(BigDecimal peso) {return new Peso("kilogramos", "Kg", peso);}
    public static Peso toneladas (BigDecimal peso) {return new Peso("toneladas",  "Tn", peso);}
}
