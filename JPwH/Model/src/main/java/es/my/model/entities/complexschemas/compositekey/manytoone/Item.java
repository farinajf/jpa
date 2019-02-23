/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.complexschemas.compositekey.manytoone;

import es.my.model.Constants;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author fran
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "VENDEDOR_NOMBRE",  referencedColumnName = "NOMBRE"),
        @JoinColumn(name = "VENDEDOR_EMPRESA", referencedColumnName = "EMPRESA")
    })
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
    protected Item() {}

    public Item(final String x) {
        this.nombre = x;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long    getId()       {return id;}
    public String  getNombre()   {return nombre;}
    public Usuario getVendedor() {return vendedor;}

    public void setNombre  (final String  x) {this.nombre   = x;}
    public void setVendedor(final Usuario x) {this.vendedor = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", vendedor=" + vendedor + '}';
    }
}
