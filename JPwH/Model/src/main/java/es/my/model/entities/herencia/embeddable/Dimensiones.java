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
    @AttributeOverride(name = "nombre",  column = @Column(name = "DIMENSIONES_NOMBRE")),
    @AttributeOverride(name = "simbolo", column = @Column(name = "DIMENSIONES_SIMBOLO"))
})
public class Dimensiones extends Medida {

    @NotNull
    private BigDecimal profundidad;
    @NotNull
    private BigDecimal ancho;
    @NotNull
    private BigDecimal alto;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Dimensiones() {}

    public Dimensiones(final String nombre, final String simbolo, final BigDecimal ancho, final BigDecimal alto, final BigDecimal profundidad) {
        super(nombre, simbolo);

        this.profundidad = profundidad;
        this.ancho       = ancho;
        this.alto        = alto;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public BigDecimal getProfundidad() {return profundidad;}
    public BigDecimal getAncho()       {return ancho;}
    public BigDecimal getAlto()        {return alto;}

    public void setProfundidad(final BigDecimal x) {this.profundidad = x;}
    public void setAncho      (final BigDecimal x) {this.ancho       = x;}
    public void setAlto       (final BigDecimal x) {this.alto        = x;}

    @Override
    public String toString() {
        return "Dimensiones{" + "profundidad=" + profundidad + ", ancho=" + ancho + ", alto=" + alto + '}';
    }

    /**************************************************************************/
    /*                  Metodos Publicos Estaticos                            */
    /**************************************************************************/
    public static Dimensiones centimetros(final BigDecimal x, final BigDecimal y, final BigDecimal z) {return new Dimensiones("centimetros", "cm", x, y, z);}
    public static Dimensiones metros     (final BigDecimal x, final BigDecimal y, final BigDecimal z) {return new Dimensiones("metros",      "m",  x, y, z);}
}
