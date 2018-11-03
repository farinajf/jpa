/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced.usertype;

import es.my.model.Constants;
import es.my.model.entities.advanced.ValorMonetario;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class ItemUserType {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @org.hibernate.annotations.Type(type = "valor_monetario_usd")
    @org.hibernate.annotations.Columns(
            columns = {
                @Column(name = "PRECIO_COMPRA_VALOR"),
                @Column(name = "PRECIO_COMPRA_MONEDA", length = 3)
            }
    )
    private ValorMonetario precioCompra;

    @NotNull
    @org.hibernate.annotations.Type(type = "valor_monetario_eur")
    @org.hibernate.annotations.Columns(
            columns = {
                @Column(name = "PRECIO_INICIAL_VALOR"),
                @Column(name = "PRECIO_INICIAL_MONEDA", length = 3)
            }
    )
    private ValorMonetario precioInicial;

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
    public Long           getId()            {return id;}
    public String         getNombre()        {return nombre;}
    public ValorMonetario getPrecioCompra()  {return precioCompra;}
    public ValorMonetario getPrecioInicial() {return precioInicial;}

    public void setNombre       (final String         x) {this.nombre        = x;}
    public void setPrecioCompra (final ValorMonetario x) {this.precioCompra  = x;}
    public void setPrecioInicial(final ValorMonetario x) {this.precioInicial = x;}

    @Override
    public String toString() {
        return "ItemUserType{" + "id=" + id + ", nombre=" + nombre + ", precioCompra=" + precioCompra + ", precioInicial=" + precioInicial + '}';
    }
}
