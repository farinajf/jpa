/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.fetching.interception;

import es.my.model.Constants;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.validator.constraints.Length;

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
    private Date fechaFin;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.LazyToOne(LazyToOneOption.NO_PROXY)
    private Usuario vendedor;

    @NotNull
    @Length(min = 0, max = 100)
    @Basic(fetch = FetchType.LAZY)
    private String descripcion;

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

    public Item(final String n, final Date f, final Usuario v, final String d) {
        this.nombre      = n;
        this.fechaFin    = f;
        this.vendedor    = v;
        this.descripcion = d;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long    getId()          {return id;}
    public String  getNombre()      {return nombre;}
    public Date    getFechaFin()    {return fechaFin;}
    public Usuario getVendedor()    {return vendedor;}
    public String  getDescripcion() {return descripcion;}

    public void setId         (final Long    x) {this.id          = x;}
    public void setNombre     (final String  x) {this.nombre      = x;}
    public void setFechaFin   (final Date    x) {this.fechaFin    = x;}
    public void setVendedor   (final Usuario x) {this.vendedor    = x;}
    public void setDescripcion(final String  x) {this.descripcion = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", nombre=" + nombre + ", fechaFin=" + fechaFin + '}';
    }
}
