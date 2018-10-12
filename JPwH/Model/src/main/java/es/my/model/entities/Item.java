/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.model.entities;

import es.my.model.Constants;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private long version;

    @NotNull
    @Size(min = 1, max = 10, message = "El nombre es obligatorio!!")
    private String nombre;

    private BigDecimal price;

    @Transient
    private Set<Bid> bids = new HashSet<>();

    private Categoria categoria;

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
    public Long     getId()     {return id;}
    public String   getNombre() {return nombre;}
    public Set<Bid> getBids()   {return bids;}

    public void setNombre(final String   x) {this.nombre = x;}
    public void setBids  (final Set<Bid> x) {this.bids   = x;}

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Articulo{" + "id=" + id +
               ", version=" + version +
                ", nombre=" + nombre +
                ", price=" + price +
                ", categoria=" + categoria +
                ", bids=" + bids +
               '}';
    }
}
