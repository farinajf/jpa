/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Item2 {
    private static final String _AUCTION = "AUCTION: ";

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id; // Por defecto es FIELD ACCESS

    @NotNull
    private String     desc;
    private BigDecimal precioInicial;
    private String     nombre;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long       getId()            {return id;}
    public String     getDesc()          {return desc;}
    public BigDecimal getPrecioInicial() {return precioInicial;}
    public String     getNombre()        {return nombre;}

    public void setDesc         (final String     x) {this.desc          = x;}
    public void setPrecioInicial(final BigDecimal x) {this.precioInicial = x;}
    public void setNombre       (final String     x) {this.nombre        = x.startsWith(_AUCTION) == false ? _AUCTION + x : x;}

    @Override
    public String toString() {
        return "Item2{" + "id=" + id + ", desc=" + desc + ", precioInicial=" + precioInicial + ", nombre=" + nombre + '}';
    }
}
