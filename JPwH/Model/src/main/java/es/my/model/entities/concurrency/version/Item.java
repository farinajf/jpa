/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities.concurrency.version;

import es.my.model.Constants;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
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

    @Version
    private int version;

    @NotNull
    private String nombre;

    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "IMAGENES",
            joinColumns = @JoinColumn(name = "ITEM_ID")
    )
    @Column(name = "IMAGE_FILE")
    private Set<String> imagenes = new HashSet<>();

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

    public Item(final String n) {
        this.nombre = n;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public Long        getId()        {return id;}
    public int         getVersion()   {return version;}
    public String      getNombre()    {return nombre;}
    public BigDecimal  getPrecio()    {return precio;}
    public Categoria   getCategoria() {return categoria;}
    public Set<String> getImagenes()  {return imagenes;}

    public void setNombre   (final String      x) {this.nombre    = x;}
    public void setPrecio   (final BigDecimal  x) {this.precio    = x;}
    public void setCategoria(final Categoria   x) {this.categoria = x;}
    public void setImagenes (final Set<String> x) {this.imagenes  = x;}

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", precio=" + precio + ", categoria=" + categoria + ", imagenes=" + imagenes + '}';
    }
}
