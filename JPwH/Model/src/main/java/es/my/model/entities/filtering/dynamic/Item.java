/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.filtering.dynamic;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
@org.hibernate.annotations.Filter(
        name = "limitByUserRank",
        condition = ":rangoUsuarioActual >= (SELECT u.RANGO FROM USUARIOS u WHERE u.ID = VENDEDOR_ID)"
)
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDEDOR_ID", nullable = false)
    private Usuario vendedor;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public Item() {}

    public Item(final String n, final Categoria c, final Usuario v) {
        this.nombre    = n;
        this.categoria = c;
        this.vendedor  = v;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long      getId()        {return id;}
    public String    getNombre()    {return nombre;}
    public Usuario   getVendedor()  {return vendedor;}
    public Categoria getCategoria() {return categoria;}

    public void setNombre   (final String    x) {this.nombre    = x;}
    public void setVendedor (final Usuario   x) {this.vendedor  = x;}
    public void setCategoria(final Categoria x) {this.categoria = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", categoria=" + categoria + ", vendedor=" + vendedor + '}';
    }
}
