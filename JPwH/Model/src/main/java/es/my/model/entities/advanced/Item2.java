/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.advanced;

import es.my.model.Constants;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    //@org.hibernate.annotations.Type(type = "yes_no")
    @org.hibernate.annotations.Type(type = "boolean")
    private boolean verificado = false;

    // JPA establece que @Temporal es requerido, Hibernate establece TIMESTAMP como defecto
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private java.util.Date fechaCreacion;

    //Java 8
    private java.time.Instant instante;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    private String desc;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 131072)
    private byte[] imagen;




    private BigDecimal precioInicial;

    @Access(AccessType.PROPERTY)
    @Column(name = "ITEM_NAME")
    private String nombre;

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
    public String         getDesc()          {return desc;}
    public java.util.Date getFechaCreacion() {return fechaCreacion;}
    public byte[]         getImagen()        {return imagen;}
    public String         getNombre()        {return nombre;}
    public BigDecimal     getPrecioInicial() {return precioInicial;}

    public boolean isVerificado() {return verificado;}

    public void setDesc         (final String     x) {this.desc          = x;}
    public void setImagen       (final byte[]     x) {this.imagen        = x;}
    public void setNombre       (final String     x) {this.nombre        = x.startsWith(_AUCTION) == false ? _AUCTION + x : x;}
    public void setPrecioInicial(final BigDecimal x) {this.precioInicial = x;}
    public void setVerificado   (final boolean    x) {this.verificado    = x;}

    @Override
    public String toString() {
        return "Item2{" + "id=" + id + ", verificado=" + verificado + ", fechaCreacion=" + fechaCreacion + ", instante=" + instante + ", desc=" + desc + ", precioInicial=" + precioInicial + ", nombre=" + nombre + '}';
    }
}
